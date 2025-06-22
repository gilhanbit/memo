package com.memo.post.mapper;

import com.memo.post.domain.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface PostMapper {
    // input: X
    // output: List<Post>
    public List<Post> selectPostListTest();

    public List<Post> selectPostListByUserId(
            @Param("userId") int userId,
            @Param("direction") String direction,
            @Param("standardId") Integer standardId,
            @Param("limit") int limit);
    public int selectPostIdByUserIdAsSort(
            @Param("userId") int userId,
            @Param("sort") String sort
    );

    public int insertPost(
            @Param("userId") int userId,
            @Param("subject") String subject,
            @Param("content") String content,
            @Param("imagePath")  String imagePath);
    public Post selectPostByPostIdUserId(
            @Param("postId") int postId,
            @Param("userId") int userId
    );

    public void updatePostByPostId(
            @Param("postId") int postId,
            @Param("subject") String subject,
            @Param("content") String content,
            @Param("imagePath")  String imagePath
    );

    public boolean deletePostById(int postId);
}