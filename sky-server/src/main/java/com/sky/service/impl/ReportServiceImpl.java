package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;


    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {

        List<LocalDate> dateList = new ArrayList<>();

        dateList.add(begin);
        while (!begin.isAfter(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);

        }

        List<Double> turnoverList = new ArrayList<>();

        for (LocalDate date : dateList){
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
//            查询date中的营业额-->状态已完成
            Map map = new HashMap();
            map.put("status", Orders.COMPLETED);
            map.put("begin", beginTime);
            map.put("end", endTime);
            Double turnover =  orderMapper.sumByMap(map);
            turnoverList.add(turnover == null ? 0.0 :turnover);

        }

        return TurnoverReportVO.builder().
                dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {

        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while(!begin.isAfter(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

//        新用户总量
        List<Integer> newUserList = new ArrayList<>();

//        用户总量
        List<Integer> totalUserList = new ArrayList<>();

        for (LocalDate date : dateList){
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map = new HashMap();
            map.put("end", endTime);
//            查询总用户数量
            Integer totalUser = userMapper.countByMap(map);
            totalUserList.add(totalUser);
            map.put("begin", beginTime);
//            查询新用户数量
            Integer newUser = userMapper.countByMap(map);
            newUserList.add(newUser);

        }


        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .build();

    }

    @Override
    public OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end) {

        LocalDate beginTime = begin;
        LocalDate endTime = end;

        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while(!begin.isAfter(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
//        获取begin到end之间每一天的订单数量

        List<Map<String, Object>> allOrderData = orderMapper.countOrderByDateRange(beginTime, endTime);

        //        将查询结果转换为 Map 方便匹配
        Map<LocalDate, Integer> allOrderMap = allOrderData.stream()
                .collect(Collectors.toMap(
                        m -> ((java.sql.Date) m.get("date")).toLocalDate(),
                        m -> {
                            Object count = m.get("orderCount");
                            return count == null ? 0 : ((Number) count).intValue();
                        }
                ));

        //        获取每天的订单数量列表
        List<Integer> orderCountList = dateList.stream()
                .map(date -> allOrderMap.getOrDefault(date, 0))
                .collect(Collectors.toList());
//        将订单数量累加得到订单总数
        Integer totalOrders = orderCountList.stream().mapToInt(Integer::intValue).sum();

//        获取begin到end之间每一天的完成订单数量
        List<Map<String, Object>> completeOrderData = orderMapper.countOrderByDateRangeAndStatus(
                beginTime, endTime, Orders.COMPLETED);

        Map<LocalDate, Integer> completeOrderMap = completeOrderData.stream()
                .collect(Collectors.toMap(
                        m -> ((java.sql.Date) m.get("date")).toLocalDate(),
                        m -> {
                            Object count = m.get("orderCount");
                            return count == null ? 0 : ((Number) count).intValue();
                        }
                ));

        List<Integer> completeOrderCountList = dateList.stream()
                .map(date -> completeOrderMap.getOrDefault(date, 0))
                .collect(Collectors.toList());
//        将完成订单数量累加得到完成订单总数
        Integer completeOrders = completeOrderCountList.stream().mapToInt(Integer::intValue).sum();

//        将完成订单总数除以订单总数得到完成订单百分比
        Double orderCompletionRate = totalOrders > 0 ? (double) completeOrders / totalOrders : 0.0;


        return OrderReportVO.builder()
                .orderCountList(StringUtils.join(orderCountList, ","))
                .validOrderCountList(StringUtils.join(completeOrderCountList, ","))
                .totalOrderCount(totalOrders)
                .validOrderCount(completeOrders)
                .orderCompletionRate(orderCompletionRate)
                .dateList(StringUtils.join(dateList, ","))
                .build();
    }

    @Override
    public SalesTop10ReportVO getTop10(LocalDate begin, LocalDate end) {

        List<String> names = new ArrayList<>();
        List<Integer> numbers = new ArrayList<>();

        List<Map<String, Object> > mapList = orderDetailMapper.getTop10(begin, end);

        for (Map<String, Object> map : mapList) {
            names.add((String) map.get("name"));
            numbers.add(((Number) map.get("number")).intValue());
        }

        return SalesTop10ReportVO
                .builder()
                .nameList(StringUtils.join(names, ","))
                .numberList(StringUtils.join(numbers, ","))
                .build();
    }
}
