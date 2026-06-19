package com.campus.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.book.entity.Category;
import com.campus.book.mapper.CategoryMapper;
import com.campus.book.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public List<Category> getCategories() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus, 1);
        wrapper.orderByAsc(Category::getSort);
        return categoryMapper.selectList(wrapper);
    }

    @Override
    public List<Category> getCategoryTree() {
        List<Category> allCategories = getCategories();
        List<Category> tree = new ArrayList<>();

        for (Category category : allCategories) {
            if (category.getParentId() == 0) {
                tree.add(category);
            }
        }

        for (Category parent : tree) {
            List<Category> children = new ArrayList<>();
            for (Category category : allCategories) {
                if (category.getParentId().equals(parent.getId())) {
                    children.add(category);
                }
            }
            parent.setChildren(children);
        }

        return tree;
    }

    @Override
    public Long createCategory(Category category) {
        categoryMapper.insert(category);
        return category.getId();
    }

    @Override
    public void updateCategory(Long id, Category category) {
        Category exist = categoryMapper.selectById(id);
        if (exist != null) {
            if (category.getName() != null) exist.setName(category.getName());
            if (category.getParentId() != null) exist.setParentId(category.getParentId());
            if (category.getSort() != null) exist.setSort(category.getSort());
            if (category.getStatus() != null) exist.setStatus(category.getStatus());
            categoryMapper.updateById(exist);
        }
    }

    @Override
    public void deleteCategory(Long id) {
        // 检查是否有子分类
        long childCount = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                .eq(Category::getParentId, id));
        if (childCount > 0) {
            throw new RuntimeException("请先删除子分类");
        }
        // 检查是否有上架中的书籍
        // TODO: 检查书籍数量
        categoryMapper.deleteById(id);
    }
}
