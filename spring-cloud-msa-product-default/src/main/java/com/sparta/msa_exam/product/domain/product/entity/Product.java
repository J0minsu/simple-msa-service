package com.sparta.msa_exam.product.domain.product.entity;



import com.sparta.msa_exam.product.domain.product.dto.req.ProductRequestDto;
import com.sparta.msa_exam.product.domain.product.dto.vo.ProductVO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Integer price;
    private Integer quantity;

    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private LocalDateTime deletedAt;
    private String deletedBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public static Product of(ProductRequestDto requestDto, String userId) {
        return Product.builder()
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .price(requestDto.getPrice())
                .quantity(requestDto.getQuantity())
                .createdBy(userId)
                .build();
    }

    public void update(String name, String description, Integer price, Integer quantity, String updatedBy) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.updatedBy = updatedBy;
        this.updatedAt = LocalDateTime.now();
    }

    public void makeDisabled(String deletedBy) {
        this.deletedBy = deletedBy;
        this.deletedAt = LocalDateTime.now();
    }



    public void reduceQuantity(int i) {
        this.quantity = this.quantity - i;
    }

    public ProductVO toVO() {

        return new ProductVO(
                this.id,
                this.name,
                this.description,
                this.price,
                this.quantity,
                this.createdAt,
                this.createdBy,
                this.updatedAt,
                this.updatedBy,
                this.deletedAt,
                this.deletedBy
        );
    }

}