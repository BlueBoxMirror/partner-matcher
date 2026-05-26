package com.partner.partnermatch.controller;

import com.partner.partnermatch.common.Result;
import com.partner.partnermatch.context.UserContext;
import com.partner.partnermatch.entity.ai.AIUser;
import com.partner.partnermatch.mapper.UserMapper;
import com.partner.partnermatch.pojo.vo.UserVO;
import com.partner.partnermatch.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired private TagService tagService;
    @GetMapping("/recommend")
    public Result<List<UserVO>> recommend(int pageNum, int pageSize){
        long id = UserContext.getUserId();
        return Result.success(tagService.recommend(id,pageNum,pageSize));
    }
}
