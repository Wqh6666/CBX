package com.campus.book.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.book.common.Result;
import com.campus.book.dto.OrderCreateDTO;
import com.campus.book.dto.OrderQueryDTO;
import com.campus.book.entity.Order;
import com.campus.book.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public Result<Long> createOrder(@Valid @RequestBody OrderCreateDTO dto) {
        Long userId = (Long) StpUtil.getLoginId();
        Long orderId = orderService.createOrder(userId, dto);
        return Result.success(orderId);
    }

    @GetMapping
    public Result<Page<Order>> getOrders(OrderQueryDTO dto) {
        Long userId = (Long) StpUtil.getLoginId();
        Page<Order> page = orderService.getOrders(userId, dto);
        return Result.success(page);
    }

    @GetMapping("/{id}")
    public Result<Order> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return Result.success(order);
    }

    @PostMapping("/{id}/pay")
    public Result<Void> payOrder(@PathVariable Long id) {
        Long userId = (Long) StpUtil.getLoginId();
        orderService.payOrder(id, userId);
        return Result.success();
    }

    @PostMapping("/{id}/cancel")
    public Result<Void> cancelOrder(@PathVariable Long id, @RequestParam String reason) {
        Long userId = (Long) StpUtil.getLoginId();
        orderService.cancelOrder(id, userId, reason);
        return Result.success();
    }

    @PostMapping("/{id}/receive")
    public Result<Void> receiveOrder(@PathVariable Long id) {
        Long userId = (Long) StpUtil.getLoginId();
        orderService.receiveOrder(id, userId);
        return Result.success();
    }
}
