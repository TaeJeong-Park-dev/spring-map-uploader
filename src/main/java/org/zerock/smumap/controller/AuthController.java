package org.zerock.smumap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.zerock.smumap.service.UserService;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/login-error")
    public String loginErrorPage(Model model){
        model.addAttribute("errorMessage", "로그인에 실패했습니다. 다시 시도해주세요.");
        return "login-error";
    }

    @GetMapping("/signup")
    public String signupPage(Model model){
        model.addAttribute("userForm", new UserForm());
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute UserForm userForm, Model model){
        try{
            userService.registerUser(userForm.getUsername(), userForm.getPassword());
            // 회원가입 성공 후 자동 로그인 처리
            userService.autoLogin(userForm.getUsername(), userForm.getPassword());
            return "redirect:/main";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/signup-error";
        }
    }

    @GetMapping("/signup-error")
    public String signupErrorPage(Model model){
        model.addAttribute("errorMessage", "이미 존재하는 아이디 입니다. 다시 시도해주세요.");
        return "signup-error";
    }
}
