package com.sparta.msa_exam.product.domain.product.repository;

import com.sparta.msa_exam.product.domain.product.dto.req.ProductSearchDto;
import com.sparta.msa_exam.product.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<Product> searchProducts(ProductSearchDto searchDto, Pageable pageable);
}
