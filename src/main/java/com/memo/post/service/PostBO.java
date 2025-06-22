package com.memo.post.service;

import com.memo.common.FileManagerService;
import com.memo.post.domain.Post;
import com.memo.post.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j // lombok
public class PostBO {
    //private Logger log = LoggerFactory.getLogger(PostBO.class);
    private final PostMapper postMapper;
    private final FileManagerService fileManager;
    private final static int POST_MAX_SIZE = 3;

    // i: userId
    // o: List<Post>
    public List<Post> getPostListByUserId(int userId, Integer prevId, Integer nextId) {
        // 게시글 10 9 8 | 7 6 5 | 4 3 2 | 1
        // 만약 4 3 2 페이지에 있을 때
        // 1) 페이징 없음(prev, next 없음): 최신순 3개 desc
        // 2) 다음(nextId 있음): 2보다 작은 3개 desc
        // 3) 이전(prevId 없음): 4보다 큰 3개 asc => 5 6 7 => reverse list 7 6 5

        Integer standardId = null; // 기준 postId(prev or next)
        String direction = null; // 방향
        if (prevId != null) { // 3) 이전
            standardId = prevId;
            direction = "prev";

            // 예: [5, 6, 7] => [7, 6, 5]
            List<Post> postList = postMapper.selectPostListByUserId(userId, direction, standardId, POST_MAX_SIZE);

            // reverse list
            Collections.reverse(postList); // 리버스한 후 저장됨
            return postList;
        } else if (nextId != null) { // 2) 다음
            standardId = nextId;
            direction = "next";
        }

        return postMapper.selectPostListByUserId(userId, direction, standardId, POST_MAX_SIZE);
    }

    public boolean isPrevLastPageByUserId(int userId, int prevId) {
        // 제일 큰 숫자 desc
        int maxPostId = postMapper.selectPostIdByUserIdAsSort(userId, "desc");
        return maxPostId == prevId; // true: 마지막
    }

    public boolean isNextLastPageByUserId(int userId, int nextId) {
        // 제일 작은 숫자 asc
        int minPostId = postMapper.selectPostIdByUserIdAsSort(userId, "asc");
        return minPostId == nextId; // true: 마지막
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

    // i: 6개 파라미터
    // o: X
    public void updatePostByPostIdUserId(int userId, String userLoginId,
                                         int postId, String subject,
                                         String content, MultipartFile file) {

        // 기존글 가져오기 1. 이미지 교체시 기존 이미지 제거 2. 대상 존재 확인(생략 가능, 캐시 기대)
        Post post = postMapper.selectPostByPostIdUserId(postId, userId);
        if (post == null) {
            log.info("[글 수정] post is null. postId:{}, userId:{}", postId, userId);
            return;
        }

        // 파일 존재 시 파일 업로드
        String imagePath = null;
        if (file != null) {
            imagePath = fileManager.uploadFile(file, userLoginId);

            // 업로드 성공 & 기존 이미지 있을 때 => 기존 이미지 삭제 (부가적인 로직)
            if (imagePath != null && post.getImagePath() != null) {
                fileManager.deleteFile(post.getImagePath()); // 기존 이미지 삭제
            }
        }
        // 만약 기존 이미지 존재
        // 업로드 성공 => 이미지 교체 => 업로드 된 기존 이미지 삭제
        // 업로드 실패 => 이미지 유지

        // DB 업데이트
        // imagePath가 null이면 mybatis에서 image update 하지 않게 처리
        postMapper.updatePostByPostId(postId, subject, content, imagePath);
    }

    public Post getPostByPostIdUserId(int postId, int userId) {
        return postMapper.selectPostByPostIdUserId(postId, userId);
    }

    // i: postId, userId(기존글 가져오기 위함)
    // o: X
    public boolean deletePostByPostIdUserId(int postId, int userId) {
        // 기존글 가져오기 (이미지가 있을 수도 있음)
        Post post = postMapper.selectPostByPostIdUserId(postId, userId);
        if (post == null) {
            log.warn("[#### 글 삭제] post is null. postId:{}, userId:{}", postId, userId);
            return false;
        }

        // 기존글에 이미지가 있다면 이미지 파일을 삭제
        if (post.getImagePath() != null) {
            fileManager.deleteFile(post.getImagePath());
        }

        // DB delete
        return postMapper.deletePostById(postId);
    }
}