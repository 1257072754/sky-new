package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * 根据分类id查询菜品数量
     *
     * @param id
     * @return
     */
    @Select("select  COUNT(id)  from dish where category_id = #{id}")
    Integer countByCategoryId(Long id);

    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> queryDishFlavorListByDishId(Long dishId);


    void insert(List<DishFlavor> dishFlavorList);

    void update(List<DishFlavor> dishFlavorList);

    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void delete(Long dishId);
}
