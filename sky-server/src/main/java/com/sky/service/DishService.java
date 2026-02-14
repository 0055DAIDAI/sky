package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
//    添加菜品
    void saveWithFlavor(DishDTO dishDTO);

//    菜品分页查询
    PageResult page(DishPageQueryDTO dishPageQueryDTO);
// 批量删除
    void deleteBatch(List<Long> ids);

    DishVO getByIdWithFlavor(Long id);
//根据id修改菜品和口味
    void updateWithFlavor(DishDTO dishDTO);
//根据分类id查询菜品
    List<DishVO> getDishWithCategoryId(Long categoryId);
}
