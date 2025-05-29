package com.memo.user;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
@Controller
public class UserController {

    /**
     * 회원가입 화면
     */
    @GetMapping("/sign-up-view")
    public String signUpView() {
        return "user/signUp";
    }

    /**
     * 로그인 화면
     * @return
     */
    @GetMapping("/sign-in-view")
    public String signInView() {
        return "user/signIn";
    }

    @GetMapping("/sign-out")
    public String signOut(HttpSession session) {
        // 세션을 비운다.
        session.removeAttribute("userId");
        session.removeAttribute("userName");
        session.removeAttribute("userLoginId");

        // 화면 이동 (Redirect)
        return "redirect:/user/sign-in-view";
    }
}