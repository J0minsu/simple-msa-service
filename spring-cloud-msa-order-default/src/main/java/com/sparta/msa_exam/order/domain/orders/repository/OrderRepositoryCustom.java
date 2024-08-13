package com.sparta.msa_exam.order.domain.orders.repository;

import com.sparta.msa_exam.order.domain.orders.dto.req.OrderSearchDto;
import com.sparta.msa_exam.order.domain.orders.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    Page<Order> searchOrders(OrderSearchDto searchDto, Pageable pageable, String role, String userId);
}
