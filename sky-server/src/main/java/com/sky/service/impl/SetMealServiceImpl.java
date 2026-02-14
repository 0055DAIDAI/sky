package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.vo.SetMealPageVo;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;


@Slf4j
@Service
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealMapper setMealMapper;
    @Autowired
    private SetMealDishMapper setMealDishMapper;

    @Override
    public SetmealVO getById(Long id) {
        SetmealVO setmealVO = new SetmealVO();
        Setmeal setmeal = setMealMapper.getById(id);
        BeanUtils.copyProperties(setmeal,setmealVO);
        setmealVO.setSetmealDishes(setMealMapper.getSetmealDishBySetmealId(id));
        return setmealVO;
    }

    @Transactional
    @Override
    public void saveSetMeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setMealMapper.insert(setmeal);
        setmealDTO.getSetmealDishes().forEach(dish -> dish.setSetmealId(setmeal.getId()));
        log.info("保存套餐信息：{}",setmeal);
        log.info("保存套餐详情：{}",setmealDTO.getSetmealDishes());
        setMealDishMapper.insertBatch(setmealDTO.getSetmealDishes());

    }

    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<SetMealPageVo> pageResult = setMealMapper.page(setmealPageQueryDTO);

        return new PageResult(pageResult.getTotal(),pageResult.getResult());
    }

    @Transactional
    @Override
    public void update(SetmealDTO setmealDTO) {
        setMealMapper.update(setmealDTO);
        setMealDishMapper.deleteBySetmealIds(Arrays.asList(setmealDTO.getId()));
        setmealDTO.getSetmealDishes().forEach(dish -> dish.setSetmealId(setmealDTO.getId()));
        setMealDishMapper.insertBatch(setmealDTO.getSetmealDishes());


    }

    @Override
    public void deleteSetMeals(List<Long> ids) {
        setMealMapper.deleteSetMeals(ids);
        setMealDishMapper.deleteBySetmealIds(ids);
    }

    @Override
    public void update(Integer status, Long id) {
        SetmealDTO setmealDTO = new SetmealDTO();
        setmealDTO.setStatus(status);
        setmealDTO.setId(id);
        setMealMapper.update(setmealDTO);
    }
}
