package com.sparta.msa_exam.order.domain.orders.mapper;

import com.sparta.msa_exam.order.domain.orders.dto.res.OrderResponseDto;
import com.sparta.msa_exam.order.domain.orders.dto.vo.OrderVO;
import com.sparta.msa_exam.order.domain.orders.entity.Order;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderMapper {
    
    // DTO로 변환하는 메서드
    public static OrderResponseDto toResponseDto(OrderVO order) {
        return new OrderResponseDto(
                order.getId(),
                order.getStatus().name(),
                order.getCreatedAt(),
                order.getCreatedBy(),
                order.getUpdatedAt(),
                order.getUpdatedBy(),
                order.getOrderItemIds()
        );
    }
    
}
