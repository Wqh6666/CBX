package com.campus.book.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.campus.book.common.Result;
import com.campus.book.entity.Cart;
import com.campus.book.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public Result<List<Cart>> getCart() {
        Long userId = (Long) StpUtil.getLoginId();
        List<Cart> cart = cartService.getCart(userId);
        return Result.success(cart);
    }

    @PostMapping
    public Result<Void> addToCart(@RequestBody Map<String, Object> params) {
        Long userId = (Long) StpUtil.getLoginId();
        Long bookId = Long.valueOf(params.get("bookId").toString());
        Integer quantity = params.get("quantity") != null ? Integer.valueOf(params.get("quantity").toString()) : 1;
        cartService.addToCart(userId, bookId, quantity);
        return Result.success();
    }

    @DeleteMapping("/{ids}")
    public Result<Void> removeFromCart(@PathVariable String ids) {
        Long userId = (Long) StpUtil.getLoginId();
        cartService.removeFromCart(userId, ids);
        return Result.success();
    }

    @DeleteMapping("/clear")
    public Result<Void> clearCart() {
        Long userId = (Long) StpUtil.getLoginId();
        cartService.clearCart(userId);
        return Result.success();
    }
}
