package com.campus.book.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.campus.book.common.Result;
import com.campus.book.dto.LoginDTO;
import com.campus.book.entity.User;
import com.campus.book.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginDTO dto) {
        String token = userService.login(dto);
        User userInfo = userService.getUserInfo((Long) StpUtil.getLoginId());
        return Result.success(Map.of("token", token, "userInfo", userInfo));
    }

    @PostMapping("/send-code")
    public Result<String> sendCode(@RequestParam String phone) {
        String code = userService.sendCode(phone);
        return Result.success(code);
    }
}
