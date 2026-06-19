package com.campus.book.controller;

import com.campus.book.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/user/v1/upload")
@RequiredArgsConstructor
public class FileController {

    @Value("${aliyun.oss.url-prefix}")
    private String ossUrlPrefix;

    @PostMapping("/image")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("请选择要上传的文件");
        }

        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";

        // 按日期分组存储
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String fileName = UUID.randomUUID().toString().replace("-", "") + suffix;
        String relativePath = "images/" + datePath + "/" + fileName;

        try {
            // TODO: 上传到阿里云OSS
            // 这里暂时保存在本地，实际使用时替换为OSS上传
            // File localFile = new File("/tmp/uploads/" + relativePath);
            // localFile.getParentFile().mkdirs();
            // file.transferTo(localFile);

            // 模拟返回URL
            String url = ossUrlPrefix + "/" + relativePath;
            return Result.success(url);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("上传失败: " + e.getMessage());
        }
    }
}
