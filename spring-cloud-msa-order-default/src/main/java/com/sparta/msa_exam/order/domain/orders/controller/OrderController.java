package com.sparta.msa_exam.order.domain.orders.controller;

import com.sparta.msa_exam.order.domain.orders.dto.req.OrderRequestDto;
import com.sparta.msa_exam.order.domain.orders.dto.res.OrderResponseDto;
import com.sparta.msa_exam.order.domain.orders.dto.req.OrderSearchDto;
import com.sparta.msa_exam.order.domain.orders.dto.vo.OrderVO;
import com.sparta.msa_exam.order.domain.orders.mapper.OrderMapper;
import com.sparta.msa_exam.order.domain.orders.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    
    private final OrderService orderService;


    @PostMapping
    @CachePut(cacheNames = "orderRes", key = "#result.orderId")
    public OrderResponseDto createOrder(@RequestBody OrderRequestDto orderRequestDto,
                                        @RequestHeader(value = "X-User-Id", required = true) String userId,
                                        @RequestHeader(value = "X-Role", required = true) String role) {

        OrderVO createdOrder = orderService.createOrder(orderRequestDto, userId);

        OrderResponseDto result = OrderMapper.toResponseDto(createdOrder);

        return result;
    }

    @GetMapping
    public Page<OrderResponseDto> getOrders(OrderSearchDto searchDto, Pageable pageable,
                                            @RequestHeader(value = "X-User-Id", required = true) String userId,
                                            @RequestHeader(value = "X-Role", required = true) String role) {
        // 역할이 MANAGER인지 확인
        if (!"MANAGER".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied. User role is not MANAGER.");
        }

        Page<OrderVO> orders = orderService.getOrders(searchDto, pageable, role, userId);

        Page<OrderResponseDto> result = orders.map(OrderMapper::toResponseDto);

        return result;
    }

    @GetMapping("/{id}")
    @Cacheable(cacheNames = "orderRes", key = "args[0]")
    public OrderResponseDto getOrderById(@PathVariable Long id) {

        OrderVO findOrder = orderService.getOrderById(id);

        OrderResponseDto result = OrderMapper.toResponseDto(findOrder);

        return result;
    }

    @PutMapping("/{orderId}")
    @CachePut(cacheNames = "orderRes", key = "args[0]")
    public OrderResponseDto updateOrder(@PathVariable Long orderId,
                                        @RequestBody OrderRequestDto orderRequestDto,
                                        @RequestHeader(value = "X-User-Id", required = true) String userId,
                                        @RequestHeader(value = "X-Role", required = true) String role) {

        OrderVO updatedOrder = orderService.updateOrder(orderId, orderRequestDto, userId);

        OrderResponseDto result = OrderMapper.toResponseDto(updatedOrder);

        return result;
    }

    @DeleteMapping("/{orderId}")
    @CacheEvict(value = "orderRes", key = "args[0]")
    public void deleteOrder(@PathVariable Long orderId, @RequestParam String deletedBy) {
        orderService.deleteOrder(orderId, deletedBy);
    }
}
