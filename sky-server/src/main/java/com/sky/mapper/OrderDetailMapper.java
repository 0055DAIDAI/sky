package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderDetailMapper {
    void insertBatch(List<OrderDetail> orderDetailList);

    List<OrderDetail> getByOrderId(Long orderId);

    @MapKey("name")
    List<Map<String, Object>> getTop10(LocalDate begin, LocalDate end);
}
