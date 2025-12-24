package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    /**
     * 新增分类
     * @param dishDTO
     */
    void save(DishDTO dishDTO);

    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    void update(Dish dish);

    void deleteById(Long id);

    List<Category> list(Integer type);

    DishVO queryById(Long id);

    void batchDelete(String ids);
}
