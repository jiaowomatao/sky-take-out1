package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annoation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetMealMapper {
    @Select("select count(0) from setmeal where category_id=#{id}")
    int getCountByCategory(Long id);

    @AutoFill(value = OperationType.INSERT)
    void save(Setmeal setmeal);

    Page<SetmealVO> selectPage(SetmealPageQueryDTO setmealPageQueryDTO);

    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    @AutoFill(value = OperationType.UPDATE)
    void update(Setmeal setmeal);

    void deleteBatch(List<Long> ids);

    @Select("select * from setmeal where category_id=#{category}")
    List<Setmeal> getByCategoryId(Long categoryId);
}
