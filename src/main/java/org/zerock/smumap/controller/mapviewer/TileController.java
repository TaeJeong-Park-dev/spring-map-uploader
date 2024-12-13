package org.zerock.smumap.controller.mapviewer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/mapviewer")
public class TileController {

    @PostMapping("/getTiles")
    public ResponseEntity<TileResponse> getTiles(@RequestBody TileRequest tileRequest) {
        int startX = tileRequest.getStartX();
        int startY = tileRequest.getStartY();
        int endX = tileRequest.getEndX();
        int endY = tileRequest.getEndY();
        double scale = tileRequest.getScale();

        List<Tile> tiles = new ArrayList<>();

        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                // 타일의 유효성 검사 (0 ~ 99 범위)
                if (x >= 0 && x < 100 && y >= 0 && y < 100) {
                    String src = "/map_tile_data/tile_" + y + "_" + x + ".png";
                    tiles.add(new Tile(x, y, src));
                }
            }
        }

        TileResponse response = new TileResponse(tiles);
        return ResponseEntity.ok(response);
    }

    // DTO 클래스
    public static class TileRequest {
        private int startX;
        private int startY;
        private int endX;
        private int endY;
        private double scale;

        // Getters and Setters

        public int getStartX() {
            return startX;
        }

        public void setStartX(int startX) {
            this.startX = startX;
        }

        public int getStartY() {
            return startY;
        }

        public void setStartY(int startY) {
            this.startY = startY;
        }

        public int getEndX() {
            return endX;
        }

        public void setEndX(int endX) {
            this.endX = endX;
        }

        public int getEndY() {
            return endY;
        }

        public void setEndY(int endY) {
            this.endY = endY;
        }

        public double getScale() {
            return scale;
        }

        public void setScale(double scale) {
            this.scale = scale;
        }
    }

    public static class TileResponse {
        private List<Tile> tiles;

        public TileResponse(List<Tile> tiles) {
            this.tiles = tiles;
        }

        public List<Tile> getTiles() {
            return tiles;
        }

        public void setTiles(List<Tile> tiles) {
            this.tiles = tiles;
        }
    }

    public static class Tile {
        private int x;
        private int y;
        private String src;

        public Tile(int x, int y, String src) {
            this.x = x;
            this.y = y;
            this.src = src;
        }

        // Getters and Setters

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }
    }
}
