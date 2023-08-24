package com.sky.controller.user;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("userSetMealController")
@RequestMapping("/user/setmeal")
@Api(tags = "套餐相关接口")
public class SetMealController {
    @Autowired
    private SetMealService setMealService;

    @GetMapping("/list")
    public Result<List<Setmeal>> getByCategoryId(Long categoryId){
        List<Setmeal> list =setMealService.getByCategoryId(categoryId);
        return Result.success(list);
    }
    @GetMapping("/dish/{id}")
    public Result<List<SetmealDish>> getById(@PathVariable Long id){
        SetmealVO byId = setMealService.getById(id);
        List<SetmealDish> setmealDishes = byId.getSetmealDishes();
        return Result.success(setmealDishes);
    }
}
