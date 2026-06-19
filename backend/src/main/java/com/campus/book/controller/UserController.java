package com.campus.book.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.campus.book.common.Result;
import com.campus.book.entity.User;
import com.campus.book.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/v1/profile")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public Result<User> getUserInfo() {
        Long userId = (Long) StpUtil.getLoginId();
        User user = userService.getUserInfo(userId);
        return Result.success(user);
    }

    @PutMapping
    public Result<Void> updateUserInfo(@RequestBody User user) {
        Long userId = (Long) StpUtil.getLoginId();
        userService.updateUserInfo(userId, user);
        return Result.success();
    }
}
