package com.partner.partnermatch;

import ai.onnxruntime.OrtException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.partner.partnermatch.entity.ai.AIUser;
import com.partner.partnermatch.mapper.UserMapper;
import com.partner.partnermatch.service.TagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
public class TagServiceTest {
    @Autowired private TagService tagService;
    @Autowired private UserMapper userMapper;
    @Test
    public void testGetAllTags() {
        tagService.getAllTags().forEach(System.out::println);

    }
    @Test
    public void testEmbedding() {
        try {
            System.out.println(tagService.searchByEmbedding(new String[]{"细心", "内向"}, 10));
        } catch (IOException | OrtException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void fakeData(){

        userMapper.delete(new QueryWrapper<AIUser>().like("username", "小_"));
        userMapper.insert(List.of(
                AIUser.builder().username("小A").qqEmail("123@qq.com").password("123".getBytes()).build(),
                AIUser.builder().username("小B").qqEmail("124@qq.com").password("123".getBytes()).build(),
                AIUser.builder().username("小C").qqEmail("125@qq.com").password("123".getBytes()).build(),
                AIUser.builder().username("小D").qqEmail("126@qq.com").password("123".getBytes()).build()
        ));
        Random random = new Random(123);
        for (AIUser user : userMapper.selectList(new QueryWrapper<AIUser>().like("username", "小_"))) {
            ArrayList<Integer> list=new ArrayList<>();
            int times=random.nextInt(5);
            for (int i = 0; i < times; i++) {
                list.add(random.nextInt(10)+1);
            }
            try {
                tagService.updateTags(user.getId(), list);
            } catch (OrtException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
