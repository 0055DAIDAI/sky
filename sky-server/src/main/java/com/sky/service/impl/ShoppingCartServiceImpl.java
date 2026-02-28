package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetMealMapper setMealMapper;
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
//        判断当前加入到购物车的商品是否存在
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

//        如果已经存在，只需将数量+1
        if (list != null && list.size() > 0){
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.updateNumberById(cart);
        }else{
//        如果不存在，需要插入一条购物车数据
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null){
//                本次添加购物车的是菜品
                Dish byId = dishMapper.getById(dishId);
                shoppingCart.setName(byId.getName());
                shoppingCart.setImage(byId.getImage());
                shoppingCart.setAmount(byId.getPrice());


            }else {
//                本次添加购物车的是套餐
                Long setmealId = shoppingCart.getSetmealId();
                Setmeal setmeal = setMealMapper.getById(setmealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());


            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());

            shoppingCartMapper.insert(shoppingCart);

        }


    }

    @Override
    public List<ShoppingCart> showShoppingCart() {
        Long userId = BaseContext.getCurrentId();
        ShoppingCart build = ShoppingCart.builder().userId(userId).build();
        List<ShoppingCart> list = shoppingCartMapper.list(build);
        return list;
    }

    @Override
    public void clean() {
        Long currentId = BaseContext.getCurrentId();
        shoppingCartMapper.deleteByUserId(currentId);

    }

    @Override
    public void sub(ShoppingCartDTO shoppingCartDTO) {
        Long userId = BaseContext.getCurrentId();
        Long dishId = shoppingCartDTO.getDishId();
        Long setmealId = shoppingCartDTO.getSetmealId();

        ShoppingCart shoppingCart;

        // 统一查询逻辑
        if (dishId != null) {
            // 查询购物车中的菜品数据（应该按用户和菜品ID查询）
            shoppingCart = shoppingCartMapper.getByDishIdAndUserId(dishId, userId);
        } else if (setmealId != null) {
            // 查询购物车中的套餐数据（应该按用户和套餐ID查询）
            shoppingCart = shoppingCartMapper.getBySetmealIdAndUserId(setmealId, userId);
        } else {
            return; // 无效参数
        }

        // 统一处理逻辑
        if (shoppingCart != null) {
            if (shoppingCart.getNumber() > 1) {
                // 数量大于1，减1
                shoppingCart.setNumber(shoppingCart.getNumber() - 1);
                shoppingCartMapper.updateNumberById(shoppingCart);
            } else {
                // 数量为1，直接删除
                if (dishId != null) {
                    shoppingCartMapper.deleteByUserIdAndDishId(userId, dishId);
                } else {
                    shoppingCartMapper.deleteByUserIdAndSetmealId(userId, setmealId);
                }
            }
        }

////         判断是套餐还是菜品
//        if (dishId != null){
////            查询购物车中的菜品数据
//            shoppingCart = shoppingCartMapper.getByDishIdAndUserId(dishId, );
////            判断购物车中的数据是否大于1，大于1则进行更新，对number-1,否则进行删除
//            if (shoppingCart != null && shoppingCart.getNumber() > 1){
//                shoppingCart.setNumber(shoppingCart.getNumber() - 1);
//                shoppingCartMapper.updateNumberById(shoppingCart);
//            }else {
//                shoppingCartMapper.deleteByUserIdAndDishId(shoppingCart);
//            }
//        }else {
//
//            shoppingCart = shoppingCartMapper.getBySetmealIdAndUserId(setmealId, );
//            if (shoppingCart != null && shoppingCart.getNumber() > 1){
//                shoppingCart.setNumber(shoppingCart.getNumber() - 1);
//                shoppingCartMapper.updateNumberById(shoppingCart);
//            }else {
//                shoppingCartMapper.deleteByUserIdAndDishId(shoppingCart);
//            }
//
//        }
    }
}
