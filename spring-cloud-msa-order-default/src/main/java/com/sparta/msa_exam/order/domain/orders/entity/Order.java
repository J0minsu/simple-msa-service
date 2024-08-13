package com.sparta.msa_exam.order.domain.orders.entity;

import com.sparta.msa_exam.order.domain.orders.dto.res.OrderResponseDto;
import com.sparta.msa_exam.order.domain.orders.dto.vo.OrderVO;
import com.sparta.msa_exam.order.domain.orders.entity.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "orders")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ElementCollection
    @CollectionTable(name = "order_items", joinColumns = @JoinColumn(name = "order_id"))
    @Column(name = "order_item_id")
    private List<Long> orderItemIds;

    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private LocalDateTime deletedAt;
    private String deletedBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = OrderStatus.CREATED;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void updateStatus(OrderStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    // 팩토리 메서드
    public static Order of(List<Long> orderItemIds, String createdBy) {
        return Order.builder()
                .orderItemIds(orderItemIds)
                .createdBy(createdBy)
                .status(OrderStatus.CREATED)
                .build();
    }

    // 업데이트 메서드
    public void update(List<Long> orderItemIds, String updatedBy, OrderStatus status) {
        this.orderItemIds = orderItemIds;
        this.updatedBy = updatedBy;
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public void makeDisabled(String deletedBy) {
        this.deletedBy = deletedBy;
        this.deletedAt = LocalDateTime.now();
    }

    public OrderVO toVO() {
        return new OrderVO(
                this.id,
                this.status,
                this.orderItemIds,
                this.createdAt,
                this.createdBy,
                this.updatedAt,
                this.updatedBy,
                this.deletedAt,
                this.deletedBy
                );
    }

}

