package com.campus.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.book.common.BusinessException;
import com.campus.book.dto.OrderCreateDTO;
import com.campus.book.dto.OrderQueryDTO;
import com.campus.book.entity.*;
import com.campus.book.mapper.*;
import com.campus.book.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final BookMapper bookMapper;
    private final AddressMapper addressMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public Long createOrder(Long buyerId, OrderCreateDTO dto) {
        Book book = bookMapper.selectById(dto.getBookId());
        if (book == null) {
            throw new BusinessException("书籍不存在");
        }
        if (book.getStatus() != 1) {
            throw new BusinessException("书籍已下架");
        }
        if (book.getSellerId().equals(buyerId)) {
            throw new BusinessException("不能购买自己的书籍");
        }

        Address address = addressMapper.selectById(dto.getAddressId());
        if (address == null || !address.getUserId().equals(buyerId)) {
            throw new BusinessException("收货地址无效");
        }

        String orderNo = "ORD" + System.currentTimeMillis();

        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setBookId(dto.getBookId());
        order.setBuyerId(buyerId);
        order.setSellerId(book.getSellerId());
        order.setAmount(book.getPrice());
        order.setStatus(0);
        order.setStatusDesc("待付款");
        order.setAddressId(dto.getAddressId());

        orderMapper.insert(order);

        book.setBuyerId(buyerId);
        book.setStatus(2);
        bookMapper.updateById(book);

        return order.getId();
    }

    @Override
    public Page<Order> getOrders(Long userId, OrderQueryDTO dto) {
        Page<Order> page = new Page<>(dto.getPage(), dto.getSize());
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w.eq(Order::getBuyerId, userId).or().eq(Order::getSellerId, userId));
        if (dto.getStatus() != null) {
            wrapper.eq(Order::getStatus, dto.getStatus());
        }
        wrapper.orderByDesc(Order::getCreateTime);
        Page<Order> result = orderMapper.selectPage(page, wrapper);
        for (Order order : result.getRecords()) {
            fillOrderInfo(order);
        }
        return result;
    }

    @Override
    public Order getOrderById(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        fillOrderInfo(order);
        return order;
    }

    @Override
    public void payOrder(Long orderId, Long userId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getBuyerId().equals(userId)) {
            throw new BusinessException("订单不存在或无权操作");
        }
        if (order.getStatus() != 0) {
            throw new BusinessException("订单状态不允许支付");
        }
        order.setStatus(1);
        order.setStatusDesc("待发货");
        order.setPayTime(LocalDateTime.now().toString());
        order.setPayMethod("wechat");
        order.setPayStatus("paid");
        orderMapper.updateById(order);
    }

    @Override
    public void cancelOrder(Long orderId, Long userId, String reason) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getBuyerId().equals(userId)) {
            throw new BusinessException("订单不存在或无权操作");
        }
        if (order.getStatus() != 0) {
            throw new BusinessException("订单状态不允许取消");
        }
        order.setStatus(4);
        order.setStatusDesc("已取消");
        order.setCancelTime(LocalDateTime.now().toString());
        order.setCancelReason(reason);
        orderMapper.updateById(order);
    }

    @Override
    public void receiveOrder(Long orderId, Long userId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getBuyerId().equals(userId)) {
            throw new BusinessException("订单不存在或无权操作");
        }
        if (order.getStatus() != 2) {
            throw new BusinessException("订单状态不允许确认收货");
        }
        order.setStatus(3);
        order.setStatusDesc("已完成");
        order.setReceiveTime(LocalDateTime.now().toString());
        orderMapper.updateById(order);
    }

    private void fillOrderInfo(Order order) {
        Book book = bookMapper.selectById(order.getBookId());
        if (book != null) {
            order.setBook(book);
        }
        Address address = addressMapper.selectById(order.getAddressId());
        if (address != null) {
            order.setAddress(address);
        }
    }
}
