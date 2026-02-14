package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetMealService {

//    根据id查询
    SetmealVO getById(Long id);

    void saveSetMeal(SetmealDTO setmealDTO);

//    套餐分页查询
    PageResult page(SetmealPageQueryDTO setmealPageQueryDTO);

    void update(SetmealDTO setmealDTO);

    void deleteSetMeals(List<Long> ids);

    void update(Integer status, Long id);
}
