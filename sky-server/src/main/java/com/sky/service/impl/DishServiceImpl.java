package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 分类业务层
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public void save(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dish.setStatus(StatusConstant.ENABLE);
        dishMapper.insert(dish);
        if (dishDTO.getFlavors() != null && !dishDTO.getFlavors().isEmpty() && dish.getId() != null) {
            dishDTO.getFlavors().forEach(dishFlavor -> dishFlavor.setDishId(dish.getId()));
            dishFlavorMapper.insert(dishDTO.getFlavors());
        }
    }

    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> dishPage = dishMapper.pageQuery(dishPageQueryDTO);
        if (!dishPage.isEmpty()) {
            for (DishVO dishVO : dishPage.getResult()) {
                dishVO.setCategoryName(categoryMapper.queryById(dishVO.getCategoryId()));
                dishVO.setFlavors(dishFlavorMapper.queryDishFlavorListByDishId(dishVO.getId()));
            }
        }
        return new PageResult(dishPage.getTotal(), dishPage.getResult());
    }


    @Override
    public void update(Dish dish) {
        Dish dishRes = dishMapper.queryById(dish.getId());
        BeanUtils.copyProperties(dish, dishRes);
        dishMapper.update(dishRes);
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<Category> list(Integer type) {
        return null;
    }

    @Override
    public DishVO queryById(Long id) {
        Dish dish = dishMapper.queryById(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setCategoryName(categoryMapper.queryById(dish.getCategoryId()));
        dishVO.setFlavors(dishFlavorMapper.queryDishFlavorListByDishId(dish.getId()));
        return dishVO;
    }

    @Override
    public void batchDelete(String ids) {
        // 在售状态下不能删除
        String[] idArray = ids.split(",");
        List<Long> idsArr = new ArrayList<>();
        List<Dish> list = new ArrayList<>();
        for (String idStr : idArray) {
            Long id = Long.valueOf(idStr);
            list.add(dishMapper.queryById(id));
            idsArr.add(id);
        }
        for (Dish dish : list) {
            if (Objects.equals(dish.getStatus(), StatusConstant.ENABLE)) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        dishMapper.batchDelete(idsArr);
        // TODO被套餐关联的菜品不能删除
    }
}