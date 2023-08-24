package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class Task {
    @Autowired
    private OrderMapper orderMapper;

    @Scheduled(cron = "0 * * * * ?")
    public void processTimeOut() {
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(-15);
        List<Orders> lists = orderMapper.getByStatusAndOrderTimeLt(1, localDateTime);
        if (lists != null && lists.size() > 0){
            for (Orders list : lists) {
                list.setStatus(6);
                list.setCancelTime(LocalDateTime.now());
                list.setCancelReason("订单超时");
                orderMapper.update(list);
            }
        }
    }
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliver(){
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(-60);
        List<Orders> lists = orderMapper.getByStatusAndOrderTimeLt(4, localDateTime);
        if (lists != null && lists.size() > 0){
            for (Orders list : lists) {
                list.setStatus(5);
                orderMapper.update(list);
            }
        }
    }
}
