package com.sparta.msa_exam.product.domain.product.dto.res;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto implements Serializable {

    private Long id;
    private String name;
    private String description;
    private Integer price;
    private Integer quantity;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}