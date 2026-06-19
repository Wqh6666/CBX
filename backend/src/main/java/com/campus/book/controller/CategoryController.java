package com.campus.book.controller;

import com.campus.book.common.Result;
import com.campus.book.entity.Category;
import com.campus.book.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public Result<List<Category>> getCategories() {
        List<Category> categories = categoryService.getCategories();
        return Result.success(categories);
    }

    @GetMapping("/tree")
    public Result<List<Category>> getCategoryTree() {
        List<Category> tree = categoryService.getCategoryTree();
        return Result.success(tree);
    }
}
