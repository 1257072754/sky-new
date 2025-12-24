package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishFlavorService;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 分类业务层
 */
@Service
@Slf4j
public class DishFlavorServiceImpl implements DishFlavorService {

    @Autowired
    private DishFlavorMapper dishFlavorMapper;


    @Override
    @Transactional
    public void update(DishDTO dishDTO) {
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(dishDTO.getId()));
            dishFlavorMapper.insert(flavors);
        }
        dishFlavorMapper.delete(dishDTO.getId());
        dishFlavorMapper.insert(flavors);
    }

    @Override
    @Transactional
    public void delete(Long dishId) {
        dishFlavorMapper.delete(dishId);
    }

    @Override
    @Transactional
    public void save(DishDTO dishDTO) {
        dishFlavorMapper.insert(dishDTO.getFlavors());
    }




}