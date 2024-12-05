package com.dingyabin.cache.controller;

import com.dingyabin.response.Result;
import com.dingyabin.utils.AliYunOssUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/file")
public class FileUploadController {



    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file")MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        InputStream inputStream = multipartFile.getInputStream();
        String url = AliYunOssUtil.putObject(fileName, inputStream);
        return Result.success(url);
    }

}
