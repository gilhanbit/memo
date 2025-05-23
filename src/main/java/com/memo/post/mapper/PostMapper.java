package com.memo.post.mapper;

import com.memo.post.mapper.domain.Post;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PostMapper {
    public List<Post> selectPostListTest();
}
