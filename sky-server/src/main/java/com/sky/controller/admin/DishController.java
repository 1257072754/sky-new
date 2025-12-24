package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import com.sky.service.DishFlavorService;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜单相关接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增分类
     *
     * @param dishDTO
     * @return
     */
    @PostMapping()
    @ApiOperation(value = "新增菜单")
    public Result insertDish(@RequestBody DishDTO dishDTO) {
        log.info("新增菜单：{}", dishDTO);
        dishService.save(dishDTO);
        return Result.success();
    }

    /**
     * 菜品分页
     */
    @GetMapping("/page")
    @ApiOperation(value = "菜品分页")
    public Result<PageResult> page(DishPageQueryDTO pageQueryDTO) {
        log.info("分页查询参数：{}", pageQueryDTO);
        PageResult pageResult = dishService.pageQuery(pageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启停分类
     */
    @PostMapping("/status/{status}")
    @ApiOperation(value = "启停分类")
    public Result<String> update(@PathVariable Integer status, Long id) {
        Dish dish = new Dish();
        dish.setId(id);
        dish.setStatus(status);
        dishService.update(dish);
        return Result.success();
    }

    /**
     * 启停分类
     */
    @PutMapping()
    @ApiOperation(value = "编辑")
    public Result<String> update(@RequestBody DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishService.update(dish);
        // 更新口味，删除关联的口味，再插入
        dishFlavorService.update(dishDTO);
        return Result.success();
    }

    /**
     * 根据ID获取菜单
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID获取菜单")
    public Result<DishVO> getById(@PathVariable Long id) {
        DishVO dishVO = dishService.queryById(id);
        return Result.success(dishVO);
    }

    /**
     * 删除菜品
     */
    @DeleteMapping()
    @ApiOperation(value = "删除菜品")
    public Result<String> batchDelete(String ids) {
        log.info("批量删除菜品：{}", ids);
        dishService.batchDelete(ids);
        return Result.success();
    }
}
