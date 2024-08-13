package com.sparta.msa_exam.order.domain.orders.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.msa_exam.order.domain.orders.dto.req.OrderSearchDto;
import com.sparta.msa_exam.order.domain.orders.entity.Order;
import com.sparta.msa_exam.order.domain.orders.entity.QOrder;
import com.sparta.msa_exam.order.domain.orders.entity.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Order> searchOrders(OrderSearchDto searchDto, Pageable pageable, String role, String userId) {

        List<OrderSpecifier<?>> orders = getAllOrderSpecifiers(pageable);

        QueryResults<Order> results = queryFactory
                .selectFrom(QOrder.order)
                .where(
                        statusEq(searchDto.getStatus()),
                        orderItemIdsIn(searchDto.getOrderItemIds()),
                        userCheck(role, userId)
                )
                .orderBy(orders.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        long total = results.getTotal();

        return new PageImpl<>(results.getResults(), pageable, total);
    }

    private BooleanExpression statusEq(OrderStatus status) {
        return status != null ? QOrder.order.status.eq(status) : null;
    }
    private BooleanExpression userCheck(String role, String userId) {
        return role.equals("MEMBER")? QOrder.order.createdBy.eq(userId): null;
    }

    private BooleanExpression orderItemIdsIn(List<Long> orderItemIds) {
        return orderItemIds != null && !orderItemIds.isEmpty() ? QOrder.order.orderItemIds.any().in(orderItemIds) : null;
    }

    private List<OrderSpecifier<?>> getAllOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        if (pageable.getSort() != null) {
            for (Sort.Order sortOrder : pageable.getSort()) {
                com.querydsl.core.types.Order direction = sortOrder.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC;
                switch (sortOrder.getProperty()) {
                    case "createdAt":
                        orders.add(new OrderSpecifier<>(direction, QOrder.order.createdAt));
                        break;
                    case "status":
                        orders.add(new OrderSpecifier<>(direction, QOrder.order.status));
                        break;
                    default:
                        break;
                }
            }
        }

        return orders;
    }
}