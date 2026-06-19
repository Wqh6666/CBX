package com.campus.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.book.common.BusinessException;
import com.campus.book.entity.Book;
import com.campus.book.entity.Cart;
import com.campus.book.mapper.BookMapper;
import com.campus.book.mapper.CartMapper;
import com.campus.book.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;
    private final BookMapper bookMapper;

    @Override
    public List<Cart> getCart(Long userId) {
        List<Cart> carts = cartMapper.selectList(new LambdaQueryWrapper<Cart>()
                .eq(Cart::getUserId, userId));
        for (Cart cart : carts) {
            Book book = bookMapper.selectById(cart.getBookId());
            cart.setBook(book);
        }
        return carts;
    }

    @Override
    public void addToCart(Long userId, Long bookId, Integer quantity) {
        Book book = bookMapper.selectById(bookId);
        if (book == null) {
            throw new BusinessException("书籍不存在");
        }
        if (book.getStatus() != 1) {
            throw new BusinessException("书籍已下架");
        }

        // 检查是否已在购物车
        Cart existCart = cartMapper.selectOne(new LambdaQueryWrapper<Cart>()
                .eq(Cart::getUserId, userId)
                .eq(Cart::getBookId, bookId));

        if (existCart != null) {
            existCart.setQuantity(existCart.getQuantity() + quantity);
            cartMapper.updateById(existCart);
        } else {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setBookId(bookId);
            cart.setQuantity(quantity);
            cartMapper.insert(cart);
        }
    }

    @Override
    public void removeFromCart(Long userId, String ids) {
        String[] idArray = ids.split(",");
        for (String idStr : idArray) {
            Long id = Long.parseLong(idStr.trim());
            Cart cart = cartMapper.selectOne(new LambdaQueryWrapper<Cart>()
                    .eq(Cart::getUserId, userId)
                    .eq(Cart::getId, id));
            if (cart != null) {
                cartMapper.deleteById(cart.getId());
            }
        }
    }

    @Override
    public void clearCart(Long userId) {
        cartMapper.delete(new LambdaQueryWrapper<Cart>().eq(Cart::getUserId, userId));
    }
}
