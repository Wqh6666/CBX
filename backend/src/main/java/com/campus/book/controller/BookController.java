package com.campus.book.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.book.common.Result;
import com.campus.book.dto.BookCreateDTO;
import com.campus.book.dto.BookQueryDTO;
import com.campus.book.entity.Book;
import com.campus.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public Result<Page<Book>> getBooks(BookQueryDTO dto) {
        Page<Book> page = bookService.getBooks(dto);
        return Result.success(page);
    }

    @GetMapping("/{id}")
    public Result<Book> getBookById(@PathVariable Long id) {
        bookService.incrementViewCount(id);
        Book book = bookService.getBookById(id);
        return Result.success(book);
    }

    @PostMapping
    public Result<Long> createBook(@RequestBody BookCreateDTO dto) {
        Long userId = (Long) StpUtil.getLoginId();
        Long bookId = bookService.createBook(userId, dto);
        return Result.success(bookId);
    }

    @PutMapping("/{id}")
    public Result<Void> updateBook(@PathVariable Long id, @RequestBody BookCreateDTO dto) {
        bookService.updateBook(id, dto);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return Result.success();
    }

    @GetMapping("/my")
    public Result<Page<Book>> getMyBooks() {
        Long userId = (Long) StpUtil.getLoginId();
        Page<Book> page = bookService.getMyBooks(userId);
        return Result.success(page);
    }
}
