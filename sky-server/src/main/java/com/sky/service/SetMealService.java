package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetMealService {
    void save(SetmealDTO setmealDTO);

    PageResult selectPage(SetmealPageQueryDTO setmealPageQueryDTO);

    SetmealVO getById(Long id);

    void update(SetmealDTO setmealDTO);

    void updateStatus(Integer status,Long id);

    void deleteBatch(List<Long> ids);

    List<Setmeal> getByCategoryId(Long categoryId);
}
