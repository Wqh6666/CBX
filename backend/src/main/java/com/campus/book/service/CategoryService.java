package com.campus.book.service;

import com.campus.book.entity.Category;
import java.util.List;

public interface CategoryService {
    List<Category> getCategories();
    List<Category> getCategoryTree();
    Long createCategory(Category category);
    void updateCategory(Long id, Category category);
    void deleteCategory(Long id);
}
