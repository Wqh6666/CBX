package com.campus.book.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.book.entity.Order;

public interface AdminOrderService {
    Page<Order> getOrders(Integer page, Integer size, Integer status);
    void shipOrder(Long id, String expressNo, String expressCompany);
    void refundOrder(Long id);
}
