package com.sky.controller.admin;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;
    /**
     * 查询订单
     *
     * @param id
     * @return
     */
    @GetMapping("/details/{id}")
    public Result<OrderVO> getByOrderId(@PathVariable Long id) {
        OrderVO orderVO = orderService.getByOrderId(id);
        return Result.success(orderVO);
    }

    /**
     * 分页查询
     * @param ordersPageQueryDTO
     * @return
     */
    @GetMapping("/conditionSearch")
    public Result<PageResult> selectPage(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageResult pageResult = orderService.selectPage(ordersPageQueryDTO);
        return Result.success(pageResult);
    }
    /**
     * 取消订单
     * @param ordersCancelDTO
     * @return
     */
    @PutMapping("/cancel")
    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        orderService.adminCancel(ordersCancelDTO);
        return Result.success();
    }
    /**
     * 各个状态订单统计
     * @return
     */
    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> selectStatistics() {
        OrderStatisticsVO orderStatisticsVO = orderService.selectStatistics();
        return Result.success(orderStatisticsVO);
    }
    /**
     * 接单
     * @param ordersDTO
     * @return
     */
    @PutMapping("/confirm")
    public Result confirm(@RequestBody OrdersDTO ordersDTO){
        orderService.confirm(ordersDTO.getId());
        return Result.success();
    }
    /**
     * 拒单
     * @param ordersRejectionDTO
     * @return
     */
    @PutMapping("/rejection")
    public Result rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO){
        orderService.rejection(ordersRejectionDTO);
        return Result.success();
    }

    /**
     * 派送订单
     * @param id
     * @return
     */
    @PutMapping("/delivery/{id}")
    public Result delivery(@PathVariable Long id){
        orderService.delivery(id);
        return Result.success();
    }
    @PutMapping("/complete/{id}")
    public Result complete(@PathVariable Long id){
        orderService.complete(id);
        return Result.success();
    }
}
