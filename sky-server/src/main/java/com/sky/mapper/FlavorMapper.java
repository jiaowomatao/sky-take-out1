package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FlavorMapper {

    @Select("select * from dish_flavor where dish_id = #{id}")
    public List<DishFlavor> getByDishId(Long id);


    void insertBatch(List<DishFlavor> flavors);

    void deleteByDishBatchId(List<Long> ids);
}
