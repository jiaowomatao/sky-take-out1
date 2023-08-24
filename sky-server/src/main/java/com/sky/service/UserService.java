package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.Category;
import com.sky.entity.User;
import com.sky.vo.DishVO;

import java.util.List;

public interface UserService {
    User login(UserLoginDTO userLoginDTO);

}
