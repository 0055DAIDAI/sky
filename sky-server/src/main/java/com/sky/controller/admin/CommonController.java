package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

//通用接口文档

@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {


    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file){
        log.info("文件上传：{}",file);
        // 1. 校验文件是否为空
        if (file == null || file.isEmpty()) {
            return Result.error("文件不能为空");
        }

        try {
            // 2. 调用文件服务进行上传
            String filePath = fileService.upload(file);
            return Result.success(filePath);
        } catch (Exception e) {
            // 3. 捕获异常并返回错误信息
            log.error("文件上传失败：{}", e.getMessage(), e);
            return Result.error("文件上传失败：" + e.getMessage());
        }

    }
}
