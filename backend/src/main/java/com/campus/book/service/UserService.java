package com.campus.book.service;

import com.campus.book.dto.LoginDTO;
import com.campus.book.entity.User;

public interface UserService {
    String login(LoginDTO dto);
    User getUserInfo(Long userId);
    User getUserById(Long userId);
    void updateUserInfo(Long userId, User user);
    String sendCode(String phone);
}
