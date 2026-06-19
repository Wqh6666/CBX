package com.campus.book.service;

import com.campus.book.entity.Admin;

public interface AdminService {
    String login(String username, String password);
    Admin getAdminById(Long id);
    Admin getAdminByUsername(String username);
}
