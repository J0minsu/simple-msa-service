package com.sparta.msa_exam.order.domain.orders.repository;

import com.sparta.msa_exam.order.domain.orders.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
}
