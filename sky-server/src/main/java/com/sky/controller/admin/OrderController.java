package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "订单管理")
@Component("adminOrderController")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/conditionSearch")
    @ApiOperation("订单搜索")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("订单搜索：{}", ordersPageQueryDTO);
        PageResult pageResult = orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/details/{id}")
    @ApiOperation("订单详情")
    public Result<OrderVO> details(@PathVariable("id") Long id) {
        log.info("订单详情：{}", id);
        OrderVO orderVO = orderService.details(id);
        return Result.success(orderVO);
    }

    @PutMapping("/confirm")
    @ApiOperation("确认订单")
    public Result confirm(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        log.info("确认订单：{}", ordersCancelDTO);
        orderService.confirm(ordersCancelDTO.getId());
        return Result.success();
    }

    @PutMapping("/rejection")
    @ApiOperation("拒绝订单")
    public Result rejection(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        log.info("拒绝订单：{}，原因：{}", ordersCancelDTO.getId(), ordersCancelDTO.getCancelReason());
        orderService.rejection(ordersCancelDTO);
        return Result.success();
    }

    @PutMapping("/cancel")
    @ApiOperation("取消订单")
    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        log.info("取消订单：{}，原因：{}", ordersCancelDTO.getId(), ordersCancelDTO.getCancelReason());
        orderService.cancel(ordersCancelDTO);
        return Result.success();
    }

    @PutMapping("/delivery/{id}")
    @ApiOperation("订单派送")
    public Result deliver(@PathVariable("id") Long id) {
        log.info("订单派送：{}", id);
        orderService.delivery(id);
        return Result.success();
    }

    @PutMapping("/complete/{id}")
    @ApiOperation("完成订单")
    public Result complete(@PathVariable("id") Long id) {
        log.info("完成订单：{}", id);
        orderService.complete(id);
        return Result.success();
    }

    @GetMapping("/statistics")
    @ApiOperation("统计订单数据")
    public Result<OrderStatisticsVO> statistics() {
        log.info("统计订单数据");
        OrderStatisticsVO orderStatisticsVO = orderService.statistics();
        return Result.success(orderStatisticsVO);
    }
}
