package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private SetMealMapper setMealMapper;
    @Autowired
    private SetMealDishMapper setMealDishMapper;

    /**
     * 新增套餐
     *
     * @param setmealDTO
     */
    @Override
    @CacheEvict(cacheNames = "setMealCache",allEntries = true)
    public void save(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setMealMapper.save(setmeal);
        List<SetmealDish> list = setmealDTO.getSetmealDishes();
        list.forEach(l -> {
            l.setSetmealId(setmeal.getId());
        });
        setMealDishMapper.insertBySetmeal(list);

    }

    @Override
    public PageResult selectPage(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setMealMapper.selectPage(setmealPageQueryDTO);
        return new PageResult(page.getTotal(),
                page.getResult());
    }

    @Override
    public SetmealVO getById(Long id) {
        Setmeal setmeal=setMealMapper.getById(id);
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal,setmealVO);
        List<SetmealDish> list = setMealDishMapper.getBySetmelId(id);
        setmealVO.setSetmealDishes(list);
        return setmealVO;
    }

    @Override
    @CacheEvict(cacheNames = "setMealCache",allEntries = true)
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setMealMapper.update(setmeal);
        List<SetmealDish> list = setmealDTO.getSetmealDishes();
        list.forEach(l->{
            l.setSetmealId(setmealDTO.getId());
        });
        setMealDishMapper.deletebySetmeal(setmeal.getId());
        setMealDishMapper.insertBySetmeal(list);
    }

    /**
     * 更改状态
     * @param status
     */
    @Override
    @CacheEvict(cacheNames = "setMealCache",allEntries = true)
    public void updateStatus(Integer status,Long id) {
        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(status);
        setmeal.setId(id);
        setMealMapper.update(setmeal);
    }

    /**
     * 批量删除
     * @param ids
     */
    @Override
    @CacheEvict(cacheNames = "setMealCache",allEntries = true)
    public void deleteBatch(List<Long> ids) {
        ids.forEach(id->{
            Setmeal byId = setMealMapper.getById(id);
            if (byId.getStatus()==1){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });
        setMealMapper.deleteBatch(ids);
        ids.forEach(id->{
            setMealDishMapper.deletebySetmeal(id);
        });

    }

    @Override
    @Cacheable(cacheNames = "setMealCache",key = "#categoryId")
    public List<Setmeal> getByCategoryId(Long categoryId) {
        List<Setmeal> list=setMealMapper.getByCategoryId(categoryId);
        return list;
    }
}
