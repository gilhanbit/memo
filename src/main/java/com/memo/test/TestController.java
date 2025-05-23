package com.memo.test;

import com.memo.post.mapper.PostMapper;
import com.memo.post.mapper.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// memo: user(jpa), post(mybatis)
// sns: user/post(jpa), comment/like(mybatis)
@Controller
public class TestController {

    @Autowired
    private PostMapper postMapper;

    @ResponseBody
    @GetMapping("/test1")
    public String test1(){
        return "<h2>test</h2>";
    }

    @ResponseBody
    @GetMapping("/test2")
    public Map<String, Object> test2(){
        Map<String, Object> map = new HashMap<>();
        map.put("a", 333);
        map.put("b", 2333);
        map.put("c", 3334);
        return map;
    }

    @GetMapping("/test3")
    public String test3(){
        return "test/ex01";
    }

    @ResponseBody
    @GetMapping("/test4")
    public List<Post> test4(){
        return postMapper.selectPostListTest();
    }
}
