package cxlab.partnermatcher.controller;

import cxlab.partnermatcher.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("/avatar")
    public Map<String, String> uploadAvatar(@RequestParam("file") MultipartFile file,
                                            HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        String url = uploadService.uploadToOss(file, userId);
        return Map.of("avatarUrl", url);
    }

    //临时方案：返回固定用户ID（假设数据库中有 id=1 的用户）
    //等认证模块完成后替换为真实逻辑
    private Long getCurrentUserId(HttpServletRequest request) {
        return 1L;
    }
}