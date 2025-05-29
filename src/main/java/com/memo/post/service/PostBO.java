package com.memo.post.service;

import com.memo.common.FileManagerService;
import com.memo.post.domain.Post;
import com.memo.post.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostBO {
    private final PostMapper postMapper;
    private final FileManagerService fileManager;

    // i: userId
    // o: List<Post>
    public List<Post> getPostListByUserId(int userId) {
        return postMapper.selectPostListByUserId(userId);
    }

    // i: 4개
    // o: int(행 개수)
    public int addPost(int userId, String userLoginId,
                       String subject, String content, MultipartFile file) {

        String imagePath = null;
//        // 파일이 존재할 때만 업로드
        if (file != null) {
            imagePath = fileManager.uploadFile(file, userLoginId);
        }

        return postMapper.insertPost(userId, subject, content, imagePath);
    }
}