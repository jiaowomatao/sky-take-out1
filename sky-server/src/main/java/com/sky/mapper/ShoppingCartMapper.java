package com.sky.mapper;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    ShoppingCart selectShopping(ShoppingCart shoppingCart);

    @Insert("insert into shopping_cart(name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time)" +
            "values (#{name},#{image},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount},#{createTime})")
    void add(ShoppingCart shoppingCart);

    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void update(ShoppingCart shoppingCart);

    @Select("select * from shopping_cart where user_id=#{currentid}")
    List<ShoppingCart> getByUserId(Long currentId);

    @Delete("delete from shopping_cart where user_id=#{currentId}")
    void deleteByUserId(Long currentId);

    void deleteOne(ShoppingCart shoppingCart1);
}
