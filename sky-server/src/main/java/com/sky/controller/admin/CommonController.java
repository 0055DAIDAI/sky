package com.sky.controller.admin;

import com.sky.result.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//通用接口文档

@RestController
@RequestMapping("/admin/common")
public class CommonController {
    @RequestMapping("/upload")
    public Result<String> upload(){
        return null;
    }
}
