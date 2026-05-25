package com.partner.partnermatch.controller;

import com.partner.partnermatch.common.Result;
import com.partner.partnermatch.dto.TagUpdateRequest;
import com.partner.partnermatch.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @PutMapping("/{userId}")
    public Result<String> updateTags(@PathVariable Long userId, @RequestBody TagUpdateRequest request) {
        tagService.updateTags(userId, request.getTagIds());
        return Result.success("ok");
    }
}
