package com.memo.post;

import com.memo.post.domain.Post;
import com.memo.post.service.PostBO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/post")
@Controller
public class PostController {
    private final PostBO postBO;

    @GetMapping("/post-list-view")
    public String postListView(
            @RequestParam(value = "prevId", required = false) Integer prevIdParam, // 이전 (이 값이 들어오면 이전 값을 요청)
            @RequestParam(value = "nextId", required = false) Integer nextIdParam, // 다음 (다음 값을 요청)
            HttpSession session, Model model) {
        // 로그인 된 사용자인지 검사
        Integer userId = (Integer)session.getAttribute("userId");
        if (userId == null) {
            // 비로그인이면 로그인 화면으로 이동시킨다.
            return "redirect:/user/sign-in-view";
        }

        // db select
        List<Post> postList = postBO.getPostListByUserId(userId, prevIdParam, nextIdParam);

        // postList가 비어있지 않을 때 페이징 정보 채우기
        int prevId = 0;
        int nextId = 0;
        if (postList.isEmpty() == false) {
            // [10, 9, 8]
            prevId = postList.get(0).getId(); // 첫번째칸 postId
            nextId = postList.get(postList.size() - 1).getId(); // 마지막칸 postId

            // 이전이 없나? 없으면 0
            // 유저가 쓴 글들 중 제일 큰 글번호가 prevId와 같으면 이전 없음
            if (postBO.isPrevLastPageByUserId(userId, prevId)) {
                prevId = 0;
            }

            // 다음이 없나? 없으면 0
            if (postBO.isNextLastPageByUserId(userId, nextId)) {
                nextId = 0;
            }
        }

        model.addAttribute("postList", postList);
        model.addAttribute("prevId", prevId);
        model.addAttribute("nextId", nextId);

        return "post/postList";
    }

    /**
     * 글쓰기 화면
     * @return
     */
    @GetMapping("/post-create-view")
    public String postCreateView() {
        return "post/postCreate";
    }

    @GetMapping("/post-detail-view")
    public String postDetailView(
            @RequestParam("postId") int postId,
            Model model,
            HttpSession session
    ) {
        int userId = (int)session.getAttribute("userId");

        // db select
        Post post = postBO.getPostByPostIdUserId(postId, userId);

        // model
        model.addAttribute("post", post);

        return "post/postDetail";
    }
}