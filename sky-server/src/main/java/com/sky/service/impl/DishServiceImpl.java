package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private SetMealDishMapper setMealDishMapper;
    @Autowired
    private FlavorMapper flavorMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 按ID查询菜品
     *
     * @param id
     * @return
     */
    @Override
    public DishDTO getById(Long id) {
        DishDTO dto = dishMapper.selectOneById(id);
        List<DishFlavor> dishFlavors = flavorMapper.getByDishId(id);
        dto.setFlavors(dishFlavors);
        return dto;
    }

    @Override
    public PageResult selectPage(DishPageQueryDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        Page<DishVO> list = dishMapper.selectPage(dto);
        return new PageResult(list.getTotal(),
                list.getResult());
    }

    @Override
    @CacheEvict(cacheNames = "dishCache",allEntries = true)
    public void update(DishDTO dto) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dto, dish);

        dishMapper.update(dish);
        List<Long> list = new ArrayList<>();
        list.add(dish.getId());
        flavorMapper.deleteByDishBatchId(list);

        List<DishFlavor> flavors = dto.getFlavors();
        flavors.forEach(flavor -> {
            flavor.setDishId(dto.getId());
        });
        flavorMapper.insertBatch(flavors);
    }

    /**
     * 新增菜品
     *
     * @param dto
     */

    @Override
    @CacheEvict(cacheNames = "dishCache",allEntries = true)
    public void save(DishDTO dto) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dto, dish);
        dishMapper.save(dish);

        Long id = dish.getId();
        List<DishFlavor> flavors = dto.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(flavor -> {
                flavor.setDishId(id);
            });
            flavorMapper.insertBatch(flavors);
        }


    }

    /**
     * 修改状态
     *
     * @param status
     */
    @Override
    @CacheEvict(cacheNames = "dishCache",allEntries = true)
    public void updateStatus(Integer status, Long id) {
        Dish dish = new Dish();
        dish.setId(id);
        dish.setStatus(status);
        dishMapper.update(dish);
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    @Override
    @CacheEvict(cacheNames = "dishCache",allEntries = true)
    public void deleteBatch(List<Long> ids) {
        ids.forEach(id -> {
            Integer status = dishMapper.getStatusById(id);
            if (status == 1) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        });

        Integer count = setMealDishMapper.getCountByDishBatchId(ids);
        if (count > 0) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        dishMapper.deleteBatch(ids);
        flavorMapper.deleteByDishBatchId(ids);
    }

    @Override
    public List<Dish> getListByCategoryId(Long categoryId) {
        List<Dish> list=dishMapper.getListByCategoryId(categoryId);
        return list;
    }

    @Override
    @Cacheable(cacheNames = "dishCache",key = "#categoryId")
    public List<DishVO> getDishByCategory(Integer categoryId) {
        List<DishVO> byCategoryId = dishMapper.getByCategoryId(categoryId);
        byCategoryId.forEach(dishVO -> {
            List<DishFlavor> byDishId = flavorMapper.getByDishId(dishVO.getId());
            dishVO.setFlavors(byDishId);
        });
        return byCategoryId;
    }
}
