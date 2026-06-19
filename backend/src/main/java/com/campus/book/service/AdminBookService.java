package com.campus.book.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.book.entity.Book;

public interface AdminBookService {
    Page<Book> getBooks(Integer page, Integer size, Integer status, String keyword);
    Page<Book> getPendingBooks(Integer page, Integer size);
    void auditBook(Long id, Integer status, String reason);
    void offshelfBook(Long id, String reason);
}
