package com.sparta.msa_exam.product.domain.product.controller;

import com.sparta.msa_exam.product.domain.product.dto.req.ProductRequestDto;
import com.sparta.msa_exam.product.domain.product.dto.res.ProductResponseDto;
import com.sparta.msa_exam.product.domain.product.dto.req.ProductSearchDto;
import com.sparta.msa_exam.product.domain.product.dto.vo.ProductVO;
import com.sparta.msa_exam.product.domain.product.mapper.ProductMapper;
import com.sparta.msa_exam.product.domain.product.service.ProductService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @CachePut(cacheNames = "productRes", key = "#result.id")
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto productRequestDto,
                                            @RequestHeader(value = "X-User-Id", required = true) String userId,
                                            @RequestHeader(value = "X-Role", required = true) String role) {
        if (!"MANAGER".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied. User role is not MANAGER.");
        }

        ProductVO createProduct = productService.createProduct(productRequestDto, userId);

        ProductResponseDto result = ProductMapper.toResponseDto(createProduct);

        return result;
    }

    @GetMapping
    public Page<ProductResponseDto> getProducts(ProductSearchDto searchDto, Pageable pageable) {

        Page<ProductVO> products = productService.getProducts(searchDto, pageable);

        Page<ProductResponseDto> result = products.map(ProductMapper::toResponseDto);

        return result;
    }

    @GetMapping("/{productId}")
    @Cacheable(cacheNames = "productRes", key = "args[0]")
    public ProductResponseDto getProductById(@PathVariable Long productId) {

        ProductVO findProduct = productService.getProductById(productId);

        ProductResponseDto result = ProductMapper.toResponseDto(findProduct);

        return result;
    }

    @PutMapping("/{productId}")
    public ProductResponseDto updateProduct(@PathVariable Long productId,
                                            @RequestBody ProductRequestDto orderRequestDto,
                                            @RequestHeader(value = "X-User-Id", required = true) String userId,
                                            @RequestHeader(value = "X-Role", required = true) String role) {

        ProductVO updatedProduct = productService.updateProduct(productId, orderRequestDto, userId);

        ProductResponseDto result = ProductMapper.toResponseDto(updatedProduct);

        return result;
    }

    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable Long productId, @RequestParam String deletedBy) {
        productService.deleteProduct(productId, deletedBy);
    }

    @GetMapping("/{id}/reduceQuantity")
    public void reduceProductQuantity(@PathVariable Long id, @RequestParam int quantity) {
        productService.reduceProductQuantity(id, quantity);
    }
}
