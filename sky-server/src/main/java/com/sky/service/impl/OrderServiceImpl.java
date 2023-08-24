package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;

    @Override
    @Transactional
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        List<ShoppingCart> lists = shoppingCartMapper.getByUserId(BaseContext.getCurrentId());
        if (lists == null || lists.size() == 0) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setUserId(BaseContext.getCurrentId());
        orders.setOrderTime(LocalDateTime.now());

        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        String address = addressBook.getProvinceName() + addressBook.getCityName() + addressBook.getDistrictName() + addressBook.getDetail();
        orders.setAddress(address);
        orders.setStatus(1);
        orders.setPayStatus(0);
        orderMapper.insert(orders);
        List<OrderDetail> details = new ArrayList<>();
        lists.forEach(list -> {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(list, orderDetail);
            orderDetail.setOrderId(orders.getId());
            details.add(orderDetail);
        });
        orderDetailMapper.insert(details);

        shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());
        OrderSubmitVO build = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .build();
        return build;
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        //调用微信支付接口，生成预支付交易单
        /*JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal(0.01), //支付金额，单位 元
                "苍穹外卖订单", //商品描述
                user.getOpenid() //微信用户的openid
        );*/
        JSONObject jsonObject = new JSONObject();
        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }

    @Override
    public OrderVO getByOrderId(Long id) {
        Orders orders = orderMapper.getByOrderId(id);
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        List<OrderDetail> list = orderDetailMapper.getByOrderId(id);
        orderVO.setOrderDetailList(list);
        return orderVO;
    }

    /**
     * 分页查询
     *
     * @param ordersPageQueryDTO
     * @return
     */
    @Override
    public PageResult selectPage(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        Page<OrderVO> page = orderMapper.selectPage(ordersPageQueryDTO);
        StringBuilder stringBuilder = new StringBuilder();
        for (OrderVO o : page) {
            List<OrderDetail> byOrderId = orderDetailMapper.getByOrderId(o.getId());
            o.setOrderDetailList(byOrderId);
            for (int i = 0; i < byOrderId.size(); i++) {
                stringBuilder.append(byOrderId.get(i).getName()+"*"+byOrderId.get(i).getNumber()+",");
                if (i==byOrderId.size()-1){
                    stringBuilder.append(byOrderId.get(i).getName()+"*"+byOrderId.get(i).getNumber()+";");
                }
            }
            /*for (OrderDetail od:byOrderId) {
                stringBuilder.append(od.getName()+"*"+od.getNumber()+);
            }*/
            String s = stringBuilder.toString();
            o.setOrderDishes(s);
        }
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void cancel(Long id) {
        Orders orders = orderMapper.getByOrderId(id);
        if (orders.getStatus()==1){
            orders.setStatus(6);
            orderMapper.update(orders);
        }
        if (orders.getStatus()==2){
            orders.setStatus(7);
            orders.setPayStatus(2);
            orderMapper.update(orders);
        }
    }

    @Override
    public void repetition(Long id) {
        List<OrderDetail> byOrderId = orderDetailMapper.getByOrderId(id);
        ShoppingCart shoppingCart = new ShoppingCart();
        for (OrderDetail o:byOrderId) {
            BeanUtils.copyProperties(o,shoppingCart);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setUserId(BaseContext.getCurrentId());
            shoppingCartMapper.add(shoppingCart);
        }
    }

    @Override
    public OrderStatisticsVO selectStatistics() {
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setToBeConfirmed(orderMapper.selectCount(2));
        orderStatisticsVO.setConfirmed(orderMapper.selectCount(3));
        orderStatisticsVO.setDeliveryInProgress(orderMapper.selectCount(4));

        return orderStatisticsVO;
    }

    @Override
    public void adminCancel(OrdersCancelDTO ordersCancelDTO) {
        Orders byOrderId = orderMapper.getByOrderId(ordersCancelDTO.getId());
        byOrderId.setStatus(6);
        byOrderId.setCancelReason(ordersCancelDTO.getCancelReason());
        byOrderId.setCancelTime(LocalDateTime.now());
        orderMapper.update(byOrderId);
    }

    @Override
    public void confirm(Long id) {
        Orders byOrderId = orderMapper.getByOrderId(id);
        byOrderId.setStatus(3);
        orderMapper.update(byOrderId);
    }

    @Override
    public void rejection(OrdersRejectionDTO ordersRejectionDTO) {
        Orders byOrderId = orderMapper.getByOrderId(ordersRejectionDTO.getId());
        byOrderId.setStatus(6);
        byOrderId.setRejectionReason(ordersRejectionDTO.getRejectionReason());
        orderMapper.update(byOrderId);
    }

    @Override
    public void delivery(Long id) {
        Orders byOrderId = orderMapper.getByOrderId(id);
        byOrderId.setStatus(4);
        orderMapper.update(byOrderId);
    }

    @Override
    public void complete(Long id) {
        Orders byOrderId = orderMapper.getByOrderId(id);
        byOrderId.setStatus(5);
        byOrderId.setDeliveryTime(LocalDateTime.now());
        orderMapper.update(byOrderId);
    }
}
