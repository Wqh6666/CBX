package com.campus.book.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.book.entity.User;

public interface AdminUserService {
    Page<User> getUsers(Integer page, Integer size, Integer status, String keyword);
    void updateUserStatus(Long id, Integer status);
}
