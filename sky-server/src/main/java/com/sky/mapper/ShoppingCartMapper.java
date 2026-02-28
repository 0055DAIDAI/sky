package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ShoppingCartMapper {

//
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    void updateNumberById(ShoppingCart cart);

    void insert(ShoppingCart shoppingCart);

    void deleteByUserId(Long currentId);

    ShoppingCart getByDishIdAndUserId(Long dishId,Long userId);

    void deleteByUserIdAndDishId(Long userId,Long dishId);

    ShoppingCart getBySetmealIdAndUserId(Long setmealId,Long userId);

    void deleteByUserIdAndSetmealId(Long userId, Long setmealId);
}
