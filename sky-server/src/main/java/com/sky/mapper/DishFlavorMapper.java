package com.sky.mapper;


import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
//    批量插入菜品口味数据
    void insertBatch(List<DishFlavor> dishFlavors);

//    根据菜品id删除菜品口味数据
    void deleteByDishId(Long id);

    void deleteByDishIds(List<Long> ids);
//查询菜品口味数据
    List<DishFlavor> getByDishId(Long id);
}
