package org.zerock.smumap.controller.mapviewer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@RestController
@RequestMapping("/mapviewer")
public class TileUploadController {
    
    @Value("${file.upload-dir}")
    private String uploadDir;
    
    @PostMapping("/upload/zip")
    public ResponseEntity<String> uploadZipTiles(@RequestParam("file") MultipartFile zipFile) {
        try {
            // 임시 디렉토리 생성
            Path tempDir = Files.createTempDirectory("temp_tiles");
            
            // ZIP 파일 임시 저장
            Path tempZip = tempDir.resolve("tiles.zip");
            Files.copy(zipFile.getInputStream(), tempZip);
            
            // ZIP 파일 압축 해제
            try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(tempZip))) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    if (!entry.isDirectory() && entry.getName().matches("tile_\\d+_\\d+\\.png")) {
                        Path targetPath = Paths.get(uploadDir, entry.getName());
                        Files.createDirectories(targetPath.getParent());
                        Files.copy(zis, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
            
            // 임시 파일 정리
            Files.deleteIfExists(tempZip);
            Files.deleteIfExists(tempDir);
            
            return ResponseEntity.ok("ZIP 파일 업로드 및 압축 해제 성공");
            
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("파일 처리 실패: " + e.getMessage());
        }
    }
    
    @PostMapping("/upload/directory")
    public ResponseEntity<String> uploadDirectory(@RequestParam("files") MultipartFile[] files) {
        try {
            for (MultipartFile file : files) {
                String fileName = file.getOriginalFilename();
                if (fileName != null && fileName.matches("tile_\\d+_\\d+\\.png")) {
                    Path targetPath = Paths.get(uploadDir, fileName);
                    Files.createDirectories(targetPath.getParent());
                    Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
            
            return ResponseEntity.ok("디렉토리 업로드 성공");
            
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("파일 처리 실패: " + e.getMessage());
        }
    }
} 