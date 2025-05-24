package com.memo.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
@Controller
public class UserController {

    // user(jpa)
    // 회원가입
    @GetMapping("/sign-up-view")
    public String signUpView() {
        return "user/signUp";
    }
}