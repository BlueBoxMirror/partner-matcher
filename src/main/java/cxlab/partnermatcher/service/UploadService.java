package cxlab.partnermatcher.service;

import com.aliyun.oss.OSS;
import cxlab.partnermatcher.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadService {

    @Autowired
    private OSS ossClient;

    @Autowired
    private UserMapper userMapper;

    @Value("${aliyun.oss.bucket-name}")
    private String bucketName;

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Transactional
    public String uploadToOss(MultipartFile file, Long userId) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }
        String contentType = file.getContentType();
        if (!"image/jpeg".equals(contentType) && !"image/png".equals(contentType)) {
            throw new RuntimeException("只支持 JPG 或 PNG 格式");
        }
        if (file.getSize() > 2 * 1024 * 1024) {
            throw new RuntimeException("图片不能超过 2MB");
        }
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = "avatars/" + userId + "_" + System.currentTimeMillis() + extension;
        try {
            ossClient.putObject(bucketName, fileName, file.getInputStream());
        } catch (Exception e) {
            throw new RuntimeException("OSS 上传失败: " + e.getMessage(), e);
        }
        String url = "https://" + bucketName + "." + endpoint + "/" + fileName;

        userMapper.updateAvatarUri(userId, url);

        return url;
    }
}