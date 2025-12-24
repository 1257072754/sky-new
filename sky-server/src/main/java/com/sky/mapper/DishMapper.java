package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {
    /**
     * 根据分类id查询菜品数量
     *
     * @param id
     * @return
     */
    @Select("select  COUNT(id)  from dish where category_id = #{id}")
    Integer countByCategoryId(Long id);


    @Insert("insert into dish(name, category_id, price, image, description,status, create_time, update_time, create_user, update_user)" + "VALUES" + " (#{name}, #{categoryId}, #{price}, #{image}, #{description}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @AutoFill(value = OperationType.INSERT)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Dish dish);

    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    @Select("select * from dish where id =#{id}")
    Dish queryById(Long id);

    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dishRes);

    void batchDelete(List<Long> ids);
}
