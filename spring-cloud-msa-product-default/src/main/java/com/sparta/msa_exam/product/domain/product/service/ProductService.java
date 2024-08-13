package com.sparta.msa_exam.product.domain.product.service;


import com.sparta.msa_exam.product.domain.product.dto.req.ProductRequestDto;
import com.sparta.msa_exam.product.domain.product.dto.req.ProductSearchDto;
import com.sparta.msa_exam.product.domain.product.dto.res.ProductResponseDto;
import com.sparta.msa_exam.product.domain.product.dto.vo.ProductVO;
import com.sparta.msa_exam.product.domain.product.entity.Product;
import com.sparta.msa_exam.product.domain.product.mapper.ProductMapper;
import com.sparta.msa_exam.product.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    //이 메서드의 결과는 캐싱이 가능하다
    //cacheNames : 이 매소드로 인해 생성 될 캐시를 지칭하는 이름
    //key : 캐시 데이터를 구분하기 위해 활용하는 값
    public ProductVO createProduct(ProductRequestDto requestDto, String userId) {

        Product product = Product.of(requestDto, userId);

        Product result = productRepository.save(product);

        return result.toVO();
    }

    public Page<ProductVO> getProducts(ProductSearchDto searchDto, Pageable pageable) {

        Page<Product> products = productRepository.searchProducts(searchDto, pageable);

        Page<ProductVO> result = products.map(Product::toVO);

        return result;
    }

    @Transactional(readOnly = true)
    public ProductVO getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .filter(p -> p.getDeletedAt() == null)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found or has been deleted"));
        return product.toVO();
    }

    @Transactional
    public ProductVO updateProduct(Long productId, ProductRequestDto requestDto, String userId) {
        Product product = productRepository.findById(productId)
                .filter(p -> p.getDeletedAt() == null)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found or has been deleted"));

        product.update(requestDto.getName(), requestDto.getDescription(), requestDto.getPrice(), requestDto.getQuantity(), userId);

        Product updatedProduct = productRepository.save(product);

        return updatedProduct.toVO();
    }

    @Transactional
    public void deleteProduct(Long productId, String deletedBy) {
        Product product = productRepository.findById(productId)
                .filter(p -> p.getDeletedAt() == null)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found or has been deleted"));
        product.makeDisabled(deletedBy);
        productRepository.save(product);
    }

    @Transactional
    public void reduceProductQuantity(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

        if (product.getQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough quantity for product ID: " + productId);
        }

        product.reduceQuantity(quantity);

        productRepository.save(product);
    }

}