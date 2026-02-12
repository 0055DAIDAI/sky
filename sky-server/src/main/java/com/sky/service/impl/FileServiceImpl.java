package com.sky.service.impl;

import com.sky.service.FileService;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;


@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Autowired
    private AliOssUtil aliOssUtil;
    @Override
    public String upload(MultipartFile file) {
        try {
            //            原始文件名
            String originalFilename = file.getOriginalFilename();
//            截取原始文件名的后缀 sdf.xxx
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
//            构造新的文件名
            String objectName = UUID.randomUUID().toString() + extension;
//            文件的请求路径
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);

            return filePath;
        } catch (IOException e) {
            log.error("文件上传失败: {}",e);
            throw new RuntimeException(e);
        }
    }
}
