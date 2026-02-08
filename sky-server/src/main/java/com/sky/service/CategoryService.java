package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {

//    对菜品分类进行修改
    void update(CategoryDTO categoryDTO);

    void addCategory(CategoryDTO categoryDTO);

    void startOrStop(Integer status, Long id);

    void deleteById(Long id);

    List<Category> list(Integer type);

    PageResult page(CategoryPageQueryDTO categoryPageQueryDTO);
}
