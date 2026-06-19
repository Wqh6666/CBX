package com.campus.book.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.book.common.Result;
import com.campus.book.entity.Order;
import com.campus.book.service.AdminOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/v1/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    @GetMapping
    public Result<Page<Order>> getOrders(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status) {
        Page<Order> pageResult = adminOrderService.getOrders(page, size, status);
        return Result.success(pageResult);
    }

    @PutMapping("/{id}/ship")
    public Result<Void> shipOrder(@PathVariable Long id, @RequestBody Map<String, String> params) {
        String expressNo = params.get("expressNo");
        String expressCompany = params.get("expressCompany");
        adminOrderService.shipOrder(id, expressNo, expressCompany);
        return Result.success();
    }

    @PutMapping("/{id}/refund")
    public Result<Void> refundOrder(@PathVariable Long id) {
        adminOrderService.refundOrder(id);
        return Result.success();
    }
}
