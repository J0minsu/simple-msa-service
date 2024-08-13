package com.sparta.msa_exam.product.domain.product.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.msa_exam.product.domain.product.dto.req.ProductSearchDto;
import com.sparta.msa_exam.product.domain.product.entity.Product;
import com.sparta.msa_exam.product.domain.product.entity.QProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Product> searchProducts(ProductSearchDto searchDto, Pageable pageable) {
        List<OrderSpecifier<?>> orders = getAllOrderSpecifiers(pageable);

        QueryResults<Product> results = queryFactory
                .selectFrom(QProduct.product)
                .where(
                        nameContains(searchDto.getName()),
                        descriptionContains(searchDto.getDescription()),
                        priceBetween(searchDto.getMinPrice(), searchDto.getMaxPrice()),
                        quantityBetween(searchDto.getMinQuantity(), searchDto.getMaxQuantity())
                )
                .orderBy(orders.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        long total = results.getTotal();

        return new PageImpl<>(results.getResults(), pageable, total);
    }

    private BooleanExpression nameContains(String name) {
        return name != null ? QProduct.product.name.containsIgnoreCase(name) : null;
    }

    private BooleanExpression descriptionContains(String description) {
        return description != null ? QProduct.product.description.containsIgnoreCase(description) : null;
    }

    private BooleanExpression priceBetween(Double minPrice, Double maxPrice) {
        if (minPrice != null && maxPrice != null) {
            return QProduct.product.price.between(minPrice, maxPrice);
        } else if (minPrice != null) {
            return QProduct.product.price.goe(minPrice);
        } else if (maxPrice != null) {
            return QProduct.product.price.loe(maxPrice);
        } else {
            return null;
        }
    }

    private BooleanExpression quantityBetween(Integer minQuantity, Integer maxQuantity) {
        if (minQuantity != null && maxQuantity != null) {
            return QProduct.product.quantity.between(minQuantity, maxQuantity);
        } else if (minQuantity != null) {
            return QProduct.product.quantity.goe(minQuantity);
        } else if (maxQuantity != null) {
            return QProduct.product.quantity.loe(maxQuantity);
        } else {
            return null;
        }
    }

    private List<OrderSpecifier<?>> getAllOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        if (pageable.getSort() != null) {
            for (Sort.Order sortOrder : pageable.getSort()) {
                com.querydsl.core.types.Order direction = sortOrder.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC;
                switch (sortOrder.getProperty()) {
                    case "createdAt":
                        orders.add(new OrderSpecifier<>(direction, QProduct.product.createdAt));
                        break;
                    case "price":
                        orders.add(new OrderSpecifier<>(direction, QProduct.product.price));
                        break;
                    case "quantity":
                        orders.add(new OrderSpecifier<>(direction, QProduct.product.quantity));
                        break;
                    default:
                        break;
                }
            }
        }

        return orders;
    }
}