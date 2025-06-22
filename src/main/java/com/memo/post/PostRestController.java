package com.memo.post;

import com.memo.post.service.PostBO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/post")
@RestController
public class PostRestController {
    private final PostBO postBO;

    /**
     * 글쓰기 API
     * @param subject
     * @param content
     * @param file
     * @param session
     * @return
     */
    @PostMapping("/create")
    public Map<String, Object> create(
            @RequestParam("subject") String subject,
            @RequestParam("content") String content,
            @RequestParam(value = "file", required = false) MultipartFile file,
            HttpSession session
    ) {
        // userId 꺼내기
        int userId = (int)session.getAttribute("userId");
        String userLoginId = (String)session.getAttribute("userLoginId");

        // db insert
        int rowCount = postBO.addPost(userId, userLoginId, subject, content, file);

        // 응답값
        Map<String, Object> result = new HashMap<>();
        if (rowCount > 0) {
            result.put("code", 200);
            result.put("result", "성공");
        } else {
            result.put("code", 500);
            result.put("error_message", "메모를 저장하지 못했습니다.");
        }
        return result;
    }

    // 글 수정
    @PutMapping("/update")
    public Map<String, Object> update(
            @RequestParam("postId") int postId,
            @RequestParam("subject") String subject,
            @RequestParam("content") String content,
            @RequestParam(value = "file", required = false) MultipartFile file,
            HttpSession session
    ) {
        // 세션 userId(검증), userLoginId(폴더)
        int userId = (int)session.getAttribute("userId");
        String userLoginId = (String)session.getAttribute("userLoginId");

        // 수정 => DB 업데이트, 파일업로드
        postBO.updatePostByPostIdUserId(userId, userLoginId,
                postId, subject, content, file);

        // 응답값
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("result", "성공");
        return result;
    }

    // 글 삭제
    @DeleteMapping("/delete")
    public Map<String, Object> delete(
            @RequestParam("postId") int postId,
            HttpSession session
    ){
        int userId = (int)session.getAttribute("userId");

        boolean deleteCheck = postBO.deletePostByPostIdUserId(postId, userId);

        Map<String, Object> result = new HashMap<>();
        if(deleteCheck){
            result.put("code", 200);
            result.put("result", "성공");
        } else {
            result.put("code", 500);
            result.put("error_message", "관리자에게 문의 바랍니다.");
        }
        return result;
    }
}