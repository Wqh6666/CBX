package com.campus.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.book.common.BusinessException;
import com.campus.book.entity.Book;
import com.campus.book.entity.Category;
import com.campus.book.entity.User;
import com.campus.book.mapper.BookMapper;
import com.campus.book.mapper.CategoryMapper;
import com.campus.book.mapper.UserMapper;
import com.campus.book.service.AdminBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminBookServiceImpl implements AdminBookService {

    private final BookMapper bookMapper;
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;

    @Override
    public Page<Book> getBooks(Integer page, Integer size, Integer status, String keyword) {
        Page<Book> pageResult = new Page<>(page, size);
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();

        if (status != null) {
            wrapper.eq(Book::getStatus, status);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Book::getTitle, keyword)
                    .or()
                    .like(Book::getAuthor, keyword));
        }

        wrapper.orderByDesc(Book::getCreateTime);
        Page<Book> result = bookMapper.selectPage(pageResult, wrapper);

        for (Book book : result.getRecords()) {
            fillBookInfo(book);
        }

        return result;
    }

    @Override
    public Page<Book> getPendingBooks(Integer page, Integer size) {
        return getBooks(page, size, 0, null);
    }

    @Override
    public void auditBook(Long id, Integer status, String reason) {
        Book book = bookMapper.selectById(id);
        if (book == null) {
            throw new BusinessException("书籍不存在");
        }

        if (status == 1) {
            // 审核通过，上架
            book.setStatus(1);
        } else {
            // 审核拒绝
            book.setStatus(3);
            book.setRejectReason(reason);
        }
        bookMapper.updateById(book);
    }

    @Override
    public void offshelfBook(Long id, String reason) {
        Book book = bookMapper.selectById(id);
        if (book == null) {
            throw new BusinessException("书籍不存在");
        }
        book.setStatus(2);
        book.setRejectReason(reason);
        bookMapper.updateById(book);
    }

    private void fillBookInfo(Book book) {
        if (book.getCategoryId() != null) {
            Category category = categoryMapper.selectById(book.getCategoryId());
            if (category != null) {
                book.setCategoryName(category.getName());
            }
        }
        if (book.getSellerId() != null) {
            User seller = userMapper.selectById(book.getSellerId());
            if (seller != null) {
                book.setSellerName(seller.getNickname());
                book.setSellerAvatar(seller.getAvatar());
            }
        }
    }
}
