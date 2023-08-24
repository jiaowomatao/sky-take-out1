package com.sky.controller.user;

import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.service.UserService;
import com.sky.vo.DishVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @GetMapping("/list")
    public Result<List<DishVO>> getDishByCategoryId(Integer categoryId){
        List<DishVO> list=dishService.getDishByCategory(categoryId);
        return Result.success(list);
    }
}
