package com.sparta.msa_exam.order.domain.orders.dto.vo;


import com.sparta.msa_exam.order.domain.orders.entity.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public final class OrderVO {

    private final Long id;
    private final OrderStatus status;
    private final List<Long> orderItemIds;
    private final LocalDateTime createdAt;
    private final String createdBy;
    private final LocalDateTime updatedAt;
    private final String updatedBy;
    private final LocalDateTime deletedAt;
    private final String deletedBy;

}
