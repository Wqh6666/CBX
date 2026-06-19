package com.campus.book.service;

import com.campus.book.entity.Cart;
import java.util.List;

public interface CartService {
    List<Cart> getCart(Long userId);
    void addToCart(Long userId, Long bookId, Integer quantity);
    void removeFromCart(Long userId, String ids);
    void clearCart(Long userId);
}
