package com.campus.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.book.common.BusinessException;
import com.campus.book.entity.*;
import com.campus.book.mapper.*;
import com.campus.book.service.AdminOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService {

    private final OrderMapper orderMapper;
    private final BookMapper bookMapper;
    private final AddressMapper addressMapper;
    private final UserMapper userMapper;

    @Override
    public Page<Order> getOrders(Integer page, Integer size, Integer status) {
        Page<Order> pageResult = new Page<>(page, size);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();

        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }

        wrapper.orderByDesc(Order::getCreateTime);
        Page<Order> result = orderMapper.selectPage(pageResult, wrapper);

        for (Order order : result.getRecords()) {
            fillOrderInfo(order);
        }

        return result;
    }

    @Override
    @Transactional
    public void shipOrder(Long id, String expressNo, String expressCompany) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (order.getStatus() != 1) {
            throw new BusinessException("订单状态不允许发货");
        }

        order.setStatus(2);
        order.setStatusDesc("待收货");
        order.setExpressNo(expressNo);
        order.setExpressCompany(expressCompany);
        order.setShipTime(LocalDateTime.now().toString());
        orderMapper.updateById(order);
    }

    @Override
    @Transactional
    public void refundOrder(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (order.getStatus() != 1 && order.getStatus() != 5) {
            throw new BusinessException("订单状态不允许退款");
        }

        order.setStatus(6);
        order.setStatusDesc("已退款");
        orderMapper.updateById(order);

        // 恢复书籍状态
        Book book = bookMapper.selectById(order.getBookId());
        if (book != null) {
            book.setBuyerId(null);
            book.setStatus(1);
            bookMapper.updateById(book);
        }
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

        User buyer = userMapper.selectById(order.getBuyerId());
        if (buyer != null) {
            order.setBuyer(buyer);
        }

        User seller = userMapper.selectById(order.getSellerId());
        if (seller != null) {
            order.setSeller(seller);
        }
    }
}
