package com.sky.controller.user;


import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/setmeal")
@Api(tags = "套餐相关接口")
@Slf4j
@Component("userSetMealController")
public class SetMealController {

    @Autowired
    private SetMealService setMealService;


//    根据id查询套餐

    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐")
    public Result<SetmealVO> getById(@PathVariable Long id){
        SetmealVO setmealVO = setMealService.getById(id);
        return Result.success(setmealVO);
    }

//    分页查询

    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("分页查询：{}",setmealPageQueryDTO);
        PageResult pageResult = setMealService.page(setmealPageQueryDTO);
        log.info("分页查询结果：{}",pageResult);
        return Result.success(pageResult);
    }

    //    根据分类id查询套餐列表
    @GetMapping("/list")
    @ApiOperation("根据分类id查询套餐")
    public Result<List<SetmealVO>> list(Long categoryId){
        log.info("根据分类id查询套餐:{}",categoryId);
        List<SetmealVO> list = setMealService.getListByCategoryId(categoryId);
        return Result.success(list);
    }


}
