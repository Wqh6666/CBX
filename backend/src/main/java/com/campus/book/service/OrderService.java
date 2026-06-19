package com.campus.book.service;

import com.campus.book.dto.OrderCreateDTO;
import com.campus.book.dto.OrderQueryDTO;
import com.campus.book.entity.Order;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface OrderService {
    Long createOrder(Long buyerId, OrderCreateDTO dto);
    Page<Order> getOrders(Long userId, OrderQueryDTO dto);
    Order getOrderById(Long id);
    void payOrder(Long orderId, Long userId);
    void cancelOrder(Long orderId, Long userId, String reason);
    void receiveOrder(Long orderId, Long userId);
}
