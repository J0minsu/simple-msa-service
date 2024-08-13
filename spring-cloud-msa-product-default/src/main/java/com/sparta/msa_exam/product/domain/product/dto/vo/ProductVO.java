package com.sparta.msa_exam.product.domain.product.dto.vo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public final class ProductVO {

    private final Long id;

    private final String name;
    private final String description;
    private final Integer price;
    private final Integer quantity;

    private final LocalDateTime createdAt;
    private final String createdBy;
    private final LocalDateTime updatedAt;
    private final String updatedBy;
    private final LocalDateTime deletedAt;
    private final String deletedBy;

}
