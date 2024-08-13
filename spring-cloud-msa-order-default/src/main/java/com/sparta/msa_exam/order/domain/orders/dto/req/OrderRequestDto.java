package com.sparta.msa_exam.order.domain.orders.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    private List<Long> orderItemIds;
    private String status;
}
