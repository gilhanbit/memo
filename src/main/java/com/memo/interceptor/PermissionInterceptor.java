package com.memo.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Slf4j
@Component
public class PermissionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws IOException {
        // 요청 경로(path)
        String uri = request.getRequestURI();
        log.info("[@@@@@@@@@@@@@@@] preHandle. uri:{}", uri);

        // 세션 - 로그인 여부
        HttpSession session = request.getSession();
        Integer userId = (Integer)session.getAttribute("userId");

        // /post... && 비로그인 => 로그인 페이지로 redirect
        if (uri.startsWith("/post") && userId == null) {
            response.sendRedirect("/user/sign-in-view");
            return false; // 원래 가려했던 컨트롤러 수행 방지
        }

        // /user... && 로그인 => 글 목록 페이지로 redirect
        if (uri.startsWith("/user") && userId != null) {
            response.sendRedirect("/post/post-list-view");
            return false; // 원래 가려했던 컨트롤러 수행 방지
        }

        return true; // 원래 요청되었던 컨트롤러 수행
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView mav) {
        // view와 model이 있다는건 html이 해석되기 전
        log.info("[$$$$$$$$$$$$ postHandle]");
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        // html 렌더링 끝난 상태
        log.info("[######### afterCompletion]");
    }
}