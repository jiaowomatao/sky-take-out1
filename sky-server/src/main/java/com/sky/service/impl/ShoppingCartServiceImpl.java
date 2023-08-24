package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.DishDTO;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetMealMapper setMealMapper;

    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        //判断购物车库中有没有当前商品
        ShoppingCart shoppingCart = new ShoppingCart();

        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());

        ShoppingCart shoppingCart1 = shoppingCartMapper.selectShopping(shoppingCart);
        if (shoppingCart1 == null) {
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setNumber(1);
            if (shoppingCartDTO.getDishId() != null) {
                DishDTO dto = dishMapper.selectOneById(shoppingCartDTO.getDishId());
                shoppingCart.setName(dto.getName());
                shoppingCart.setImage(dto.getImage());
                shoppingCart.setAmount(dto.getPrice());
            } else {
                Setmeal byId = setMealMapper.getById(shoppingCartDTO.getSetmealId());
                shoppingCart.setName(byId.getName());
                shoppingCart.setImage(byId.getImage());
                shoppingCart.setAmount(byId.getPrice());
            }
            shoppingCartMapper.add(shoppingCart);
        }else {
            shoppingCart1.setNumber(shoppingCart1.getNumber()+1);
            shoppingCartMapper.update(shoppingCart1);
        }


    }

    @Override
    public List<ShoppingCart> getByUserId() {
        Long currentId = BaseContext.getCurrentId();
        List<ShoppingCart> list=shoppingCartMapper.getByUserId(currentId);
        return list;
    }

    @Override
    public void delete() {
        shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());
    }

    @Override
    public void sub(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        ShoppingCart shoppingCart1 = shoppingCartMapper.selectShopping(shoppingCart);
        if (shoppingCart1.getNumber()>1){
            shoppingCart1.setNumber(shoppingCart1.getNumber()-1);
            shoppingCartMapper.update(shoppingCart1);
        }else {
            shoppingCartMapper.deleteOne(shoppingCart1);
        }
    }
}
