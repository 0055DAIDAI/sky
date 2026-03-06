package com.sky.mapper;


import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    void add(Orders orders);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据id查询订单
     * @param id
     * @return
     */
    Orders getById(Long id);

    /**
     * 根据状态统计订单数量
     * @param status
     * @return
     */
    Integer countStatus(Integer status);

    List<Orders> getByStatusAndOrderTimeOut(Integer status, LocalDateTime orderTime);

    Double sumByMap(Map map);

    @MapKey("date")
    List<Map<String, Object>> countOrderByDateRange(LocalDate begin, LocalDate end);

    @MapKey("date")
    List<Map<String, Object>> countOrderByDateRangeAndStatus(LocalDate begin, LocalDate end, Integer status);

    Integer countByMap(Map map);
}
