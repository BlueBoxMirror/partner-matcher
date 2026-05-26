package com.partner.partnermatch.controller;

import ai.onnxruntime.OrtException;
import com.partner.partnermatch.common.Result;
import com.partner.partnermatch.dto.TagUpdateRequest;
import com.partner.partnermatch.pojo.LuceneSearchResult;
import com.partner.partnermatch.service.TagService;
import org.apache.lucene.search.ScoreDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @PutMapping("/{userId}")
    public Result<String> updateTags(@PathVariable Long userId, @RequestBody TagUpdateRequest request) throws IOException, OrtException {
        tagService.updateTags(userId, request.getTagIds());
        return Result.success("ok");
    }

    @GetMapping("/search/exact")
    public Result<LuceneSearchResult> searchExact(@RequestParam String[] tags, @RequestParam int count) throws IOException {
        return Result.success(tagService.searchExactByTags(tags, count));
    }
    @GetMapping("/search/exact")
    public Result<LuceneSearchResult> searchExact(@RequestParam String[] tags, @RequestParam int count, @RequestParam ScoreDoc lastScoreDoc) throws IOException {
        return Result.success(tagService.searchExactByTags(tags, count, lastScoreDoc));
    }

    @GetMapping("/search/fuzzy")
    public Result<LuceneSearchResult> searchFuzzy(@RequestParam String[] tags, @RequestParam int count) throws IOException {
        return Result.success(tagService.searchFuzzyByTags(tags, count));
    }
    @GetMapping("/search/fuzzy")
    public Result<LuceneSearchResult> searchFuzzy(@RequestParam String[] tags, @RequestParam int count, @RequestParam ScoreDoc lastScoreDoc) throws IOException {
        return Result.success(tagService.searchFuzzyByTags(tags, count, lastScoreDoc));
    }

    @GetMapping("/search/embedding")
    public Result<LuceneSearchResult> searchEmbedding(@RequestParam String[] tags, @RequestParam int count) throws IOException, OrtException {
        return Result.success(tagService.searchByEmbedding(tags, count));
    }
    @GetMapping("/search/embedding")
    public Result<LuceneSearchResult> searchEmbedding(@RequestParam String[] tags, @RequestParam int count, @RequestParam ScoreDoc lastScoreDoc) throws IOException, OrtException {
        return Result.success(tagService.searchByEmbedding(tags, count, lastScoreDoc));
    }

}
