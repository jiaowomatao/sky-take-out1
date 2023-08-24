package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annoation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper
public interface DishMapper {
    @Select("select * from dish where id = #{id}")
    DishDTO selectOneById(Long id);

    Page<DishVO> selectPage(DishPageQueryDTO dto);

    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);


    @AutoFill(value = OperationType.INSERT)
    void save(Dish dish);

    @Select("select count(0) from dish where category_id=#{id}")
    int getCountByCategoryId(Long id);

    void deleteBatch(List<Long> ids);

    @Select("select status from dish where id = #{id}")
    Integer getStatusById(Long id);

    @Select("select * from dish where category_id = #{categoryId}")
    List<Dish> getListByCategoryId(Long categoryId);

    @Select("select d.id,d.name,d.category_id,d.price,d.image,d.description,d.status,d.update_time,c.name categoryName\n" +
            "from dish d inner join category c on d.category_id = c.id " +
            "where d.category_id= #{category}")
    List<DishVO> getByCategoryId(Integer category);
}
