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
    public List<Post> selectPostListByUserId(int userId);
    public int insertPost(
            @Param("userId") int userId,
            @Param("subject") String subject,
            @Param("content") String content,
            @Param("imagePath")  String imagePath);
}