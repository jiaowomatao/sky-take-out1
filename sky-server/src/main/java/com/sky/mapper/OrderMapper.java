package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {
    @Insert("insert into orders(number, status, user_id, address_book_id, order_time, checkout_time, " +
            "pay_method, pay_status, amount, remark, phone, address, " +
            "user_name, consignee, cancel_reason, rejection_reason, cancel_time, estimated_delivery_time, " +
            "delivery_status, delivery_time, pack_amount, tableware_number, tableware_status) " +
            "values (#{number},#{status},#{userId},#{addressBookId},#{orderTime},#{checkoutTime}," +
            "#{payMethod},#{payStatus},#{amount},#{remark},#{phone},#{address}," +
            "#{userName},#{consignee},#{cancelReason},#{rejectionReason},#{cancelTime},#{estimatedDeliveryTime}," +
            "#{deliveryStatus},#{deliveryTime},#{packAmount},#{tablewareNumber},#{tablewareStatus})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     *
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     *
     * @param orders
     */
    void update(Orders orders);

    @Select("select * from orders where id = #{id}")
    Orders getByOrderId(Long id);

    Page<OrderVO> selectPage(OrdersPageQueryDTO ordersPageQueryDTO);

    @Select("select count(0) from orders where status = #{i}")
    Integer selectCount(int i);

    @Select("select * from orders where status=#{status} and order_time<#{localDateTime}")
    List<Orders> getByStatusAndOrderTimeLt(Integer status, LocalDateTime localDateTime);
}
