package com.sparta.msa_exam.order.domain.orders.service;

import com.sparta.msa_exam.order.domain.orders.dto.req.OrderRequestDto;
import com.sparta.msa_exam.order.domain.orders.dto.res.OrderResponseDto;
import com.sparta.msa_exam.order.domain.orders.dto.req.OrderSearchDto;
import com.sparta.msa_exam.order.domain.orders.dto.vo.OrderVO;
import com.sparta.msa_exam.order.domain.orders.entity.Order;
import com.sparta.msa_exam.order.domain.orders.mapper.OrderMapper;
import com.sparta.msa_exam.order.domain.orders.repository.OrderRepository;
import com.sparta.msa_exam.order.domain.product.client.ProductClient;
import com.sparta.msa_exam.order.domain.product.dto.res.ProductResponseDto;
import com.sparta.msa_exam.order.domain.orders.entity.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    @Transactional
    public OrderVO createOrder(OrderRequestDto requestDto, String userId) {
        // Check if products exist and if they have enough quantity

        /**
         * TODO CompletableFuture.asyncSupply
         */

        for (Long productId : requestDto.getOrderItemIds()) {
            ProductResponseDto product = productClient.getProduct(productId);
            log.info("############################ Product 수량 확인 : " + product.getQuantity());
            if (product.getQuantity() < 1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product with ID " + productId + " is out of stock.");
            }
        }

        // Reduce the quantity of each product by 1
        for (Long productId : requestDto.getOrderItemIds()) {
            productClient.reduceProductQuantity(productId, 1);
        }

        Order order = Order.of(requestDto.getOrderItemIds(), userId);

        Order savedOrder = orderRepository.save(order);

        return savedOrder.toVO();
    }

    public Page<OrderVO> getOrders(OrderSearchDto searchDto, Pageable pageable, String role, String userId) {

        Page<Order> orders = orderRepository.searchOrders(searchDto, pageable, role, userId);

        Page<OrderVO> result = orders.map(Order::toVO);

        return result;
    }
    @Transactional(readOnly = true)
    public OrderVO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .filter(o -> o.getDeletedAt() == null)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found or has been deleted"));

        return order.toVO();
    }

    @Transactional
    public OrderVO updateOrder(Long orderId, OrderRequestDto requestDto,String userId) {
        Order order = orderRepository.findById(orderId)
                .filter(o -> o.getDeletedAt() == null)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found or has been deleted"));

        /**
         * TODO product check (from feign -> product-service) => DONE
         *
         */
        List<Long> checkedIds= requestDto.getOrderItemIds().stream().filter(i -> {
            boolean isExist = false;
                try {
                    productClient.getProduct(i).getId();
                    isExist = true;
                }
                catch (RuntimeException e) {
                    log.error("Not Exist Product :: {}", i);
                }
            return isExist;
        }).toList();

        requestDto.setOrderItemIds(checkedIds);

        order.update(requestDto.getOrderItemIds(), userId, OrderStatus.valueOf(requestDto.getStatus()));

        Order updatedOrder = orderRepository.save(order);

        return updatedOrder.toVO();
    }

    @Transactional
    public void deleteOrder(Long orderId, String deletedBy) {

        Order order = orderRepository.findById(orderId)
                .filter(o -> o.getDeletedAt() == null)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found or has been deleted"));

        order.makeDisabled(deletedBy);

        orderRepository.save(order);
    }

}