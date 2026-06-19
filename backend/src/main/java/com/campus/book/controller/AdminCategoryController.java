package com.campus.book.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.campus.book.common.Result;
import com.campus.book.entity.Category;
import com.campus.book.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/v1/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public Result<List<Category>> getCategories() {
        List<Category> categories = categoryService.getCategories();
        return Result.success(categories);
    }

    @PostMapping
    public Result<Long> createCategory(@RequestBody Category category) {
        Long id = categoryService.createCategory(category);
        return Result.success(id);
    }

    @PutMapping("/{id}")
    public Result<Void> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        categoryService.updateCategory(id, category);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return Result.success();
    }
}
