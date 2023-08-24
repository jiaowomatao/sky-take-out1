package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
public class DishController {
    @Autowired
    private DishService dishService;

    @GetMapping("/{id}")
    @ApiOperation("按id查询")
    public Result<DishDTO> getById(@PathVariable Long id) {
        DishDTO dto = dishService.getById(id);
        return Result.success(dto);
    }
    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dto){
        dishService.save(dto);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> page(DishPageQueryDTO dto) {
        PageResult pageResult = dishService.selectPage(dto);
        return Result.success(pageResult);
    }

    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dto) {
        dishService.update(dto);
        return Result.success();
    }
    /**
     * 修改菜品售卖状态
     */
    @PostMapping("/status/{status}")
    @ApiOperation("修改菜品状态")
    public Result updateStatus(@PathVariable Integer status,Long id){
        dishService.updateStatus(status,id);
        return Result.success();
    }
    /**
     * 删除菜品
     */
    @DeleteMapping
    @ApiOperation("删除菜品")
    public Result deleteBatch(@RequestParam List<Long> ids){
        dishService.deleteBatch(ids);
        return Result.success();
    }
    /**
     * 根据分类id查询菜品
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> getListByCategoryId(Long categoryId){
        List<Dish> list = dishService.getListByCategoryId(categoryId);
        return Result.success(list);
    }
}
