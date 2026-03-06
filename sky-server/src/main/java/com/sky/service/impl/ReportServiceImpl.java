package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
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
    @Autowired
    private WorkspaceService workspaceService;


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

    @Override
    public void exportBusinessData(HttpServletResponse response) {
//        查询数据库，获取营业数据
        LocalDate now = LocalDate.now();
        LocalDate end = now.minusDays(1);
        LocalDate begin = end.minusDays(30);

//        查询概览数据
        BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(begin, LocalTime.MIN), LocalDateTime.of(end, LocalTime.MAX));

//        通过POI将数据导入到excel中
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");
        try {
            XSSFWorkbook excel = new XSSFWorkbook(resourceAsStream);
            XSSFSheet sheet = excel.getSheet("Sheet1");
            sheet.getRow(1).getCell(1).setCellValue("时间：" + begin + "至" + end);
            XSSFRow row2 = sheet.getRow(3);
            row2.getCell(2).setCellValue(businessData.getTurnover());
            row2.getCell(4).setCellValue(businessData.getValidOrderCount());
            row2.getCell(6).setCellValue(businessData.getOrderCompletionRate());
            row2 = sheet.getRow(4);
            row2.getCell(2).setCellValue(businessData.getNewUsers());
            row2.getCell(4).setCellValue(businessData.getUnitPrice());

//            明细数据填充
            for (int i =0;i<30;i++){
                LocalDate date = begin.plusDays(i);
//                查询某一天的数据
                BusinessDataVO dayData = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));

                row2 = sheet.getRow(7 + i);
                row2.getCell(1).setCellValue(date.toString());
                row2.getCell(2).setCellValue(dayData.getTurnover());
                row2.getCell(3).setCellValue(dayData.getValidOrderCount());
                row2.getCell(4).setCellValue(dayData.getOrderCompletionRate());
                row2.getCell(5).setCellValue(dayData.getNewUsers());
                row2.getCell(6).setCellValue(dayData.getUnitPrice());

            }

//        通过输出流将文件下载到客户端
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);
//            关闭资源
            out.close();
            excel.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
