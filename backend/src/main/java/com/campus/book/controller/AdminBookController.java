package com.campus.book.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.book.common.Result;
import com.campus.book.entity.Book;
import com.campus.book.service.AdminBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/v1/books")
@RequiredArgsConstructor
public class AdminBookController {

    private final AdminBookService adminBookService;

    @GetMapping
    public Result<Page<Book>> getBooks(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword) {
        Page<Book> pageResult = adminBookService.getBooks(page, size, status, keyword);
        return Result.success(pageResult);
    }

    @GetMapping("/pending")
    public Result<Page<Book>> getPendingBooks(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<Book> pageResult = adminBookService.getPendingBooks(page, size);
        return Result.success(pageResult);
    }

    @PutMapping("/{id}/audit")
    public Result<Void> auditBook(@PathVariable Long id, @RequestBody Map<String, Object> params) {
        Integer status = (Integer) params.get("status");
        String reason = (String) params.get("reason");
        adminBookService.auditBook(id, status, reason);
        return Result.success();
    }

    @PutMapping("/{id}/offshelf")
    public Result<Void> offshelfBook(@PathVariable Long id, @RequestBody Map<String, String> params) {
        String reason = params.get("reason");
        adminBookService.offshelfBook(id, reason);
        return Result.success();
    }
}
