package com.sky.controller.admin;


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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
@Slf4j
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

//    新增套餐

    @PostMapping
    @ApiOperation("新增套餐")
    public Result save(@RequestBody SetmealDTO setmealDTO){
        setMealService.saveSetMeal(setmealDTO);
        return Result.success();
    }

//    修改订单
    @PutMapping
    @ApiOperation("修改订单")
    public Result update(@RequestBody SetmealDTO setmealDTO){
        setMealService.update(setmealDTO);

        return Result.success();
    }

//    批量删除订单
    @DeleteMapping
    @ApiOperation("批量删除订单")
    public Result delete(@RequestParam List<Long> ids){
        setMealService.deleteSetMeals(ids);
        return Result.success();
    }

//    套餐起售停售
    @PostMapping("/status/{status}")
    @ApiOperation("套餐起售停售")
    public Result startOrStop(@PathVariable Integer status, Long id){
        setMealService.update(status,id);
        return Result.success();
    }


}
