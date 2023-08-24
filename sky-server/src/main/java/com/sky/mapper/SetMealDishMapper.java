package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetMealDishMapper {

    Integer getCountByDishBatchId(List<Long> ids);

    void insertBySetmeal(List<SetmealDish> list);

    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> getBySetmelId(Long id);

    @Delete("delete from setmeal_dish where setmeal_id = #{id}")
    void deletebySetmeal(Long id);
}
