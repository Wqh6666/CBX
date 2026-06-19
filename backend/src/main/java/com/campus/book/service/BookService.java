package com.campus.book.service;

import com.campus.book.dto.BookCreateDTO;
import com.campus.book.dto.BookQueryDTO;
import com.campus.book.entity.Book;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface BookService {
    Page<Book> getBooks(BookQueryDTO dto);
    Book getBookById(Long id);
    Long createBook(Long sellerId, BookCreateDTO dto);
    void updateBook(Long id, BookCreateDTO dto);
    void deleteBook(Long id);
    Page<Book> getMyBooks(Long sellerId);
    void incrementViewCount(Long id);
}
