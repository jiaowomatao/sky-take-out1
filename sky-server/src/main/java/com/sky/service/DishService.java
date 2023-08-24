package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    DishDTO getById(Long id);

    PageResult selectPage(DishPageQueryDTO dto);

    void update(DishDTO dto);

    void save(DishDTO dto);

    void updateStatus(Integer status, Long id);

    void deleteBatch(List<Long> ids);

    List<Dish> getListByCategoryId(Long categoryId);

    List<DishVO> getDishByCategory(Integer categoryId);
}
