package com.sparta.msa_exam.product.domain.product.mapper;

import com.sparta.msa_exam.product.domain.product.dto.res.ProductResponseDto;
import com.sparta.msa_exam.product.domain.product.dto.vo.ProductVO;
import com.sparta.msa_exam.product.domain.product.entity.Product;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductMapper {
    
    // DTO로 변환하는 메서드
    public static ProductResponseDto toResponseDto(ProductVO product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getCreatedAt(),
                product.getCreatedBy(),
                product.getUpdatedAt(),
                product.getUpdatedBy()
        );
    }
    
}
