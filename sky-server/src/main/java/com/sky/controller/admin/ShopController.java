package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "UserSHOP相关接口")
public class ShopController {
    @Autowired
    private RedisTemplate redisTemplate;
    @PutMapping("/{status}")
    @ApiOperation("修改营业状态")
    public Result setShopStatus(@PathVariable Integer status){
        redisTemplate.opsForValue().set("status",status);
        return Result.success();
    }

    @GetMapping("/status")
    @ApiOperation("获得营业状态")
    public Result<Integer> getShopStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get("status");
        return Result.success(status);
    }
}
