package com.sky.service;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {

    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTo) throws Exception;

    void paySuccess(String outTradeNo) ;

    // 管理端接口
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderVO details(Long id);

    void confirm(Long id);

    void rejection(OrdersCancelDTO ordersCancelDTO);

    void cancel(OrdersCancelDTO ordersCancelDTO);

    void delivery(Long id);

    void complete(Long id);

    OrderStatisticsVO statistics();

    // 用户端接口
    PageResult userPage(OrdersPageQueryDTO ordersPageQueryDTO);

    void userCancelById(Long id);

    void repetition(Long id);

    void reminder(Long id);

}
