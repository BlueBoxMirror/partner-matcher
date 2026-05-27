package com.partner.partnermatch.controller;

import com.partner.partnermatch.common.Result;
import com.partner.partnermatch.context.UserContext;
import com.partner.partnermatch.entity.ai.AIUser;
import com.partner.partnermatch.mapper.UserMapper;
import com.partner.partnermatch.pojo.vo.LuceneSearchVO;
import com.partner.partnermatch.pojo.vo.UserVO;
import com.partner.partnermatch.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired private TagService tagService;
    @GetMapping("/recommend")
    public Result<LuceneSearchVO> recommend(@RequestParam(required = false,defaultValue = "0") Integer pageNum,@RequestParam(required = false,defaultValue = "10") Integer pageSize){
        long id = UserContext.getUserId();
        LuceneSearchVO result = tagService.recommend(id,pageNum,pageSize);
        log.info("result:{}",result);
        return Result.success(result);
    }
}
