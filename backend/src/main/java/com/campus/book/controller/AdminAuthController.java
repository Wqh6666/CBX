package com.campus.book.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.campus.book.common.Result;
import com.campus.book.entity.Admin;
import com.campus.book.mapper.AdminMapper;
import com.campus.book.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/v1/auth")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminService adminService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        String token = adminService.login(username, password);
        Admin admin = adminService.getAdminByUsername(username);
        return Result.success(Map.of("token", token, "adminInfo", admin));
    }

    @PostMapping("/logout")
    @SaCheckLogin
    public Result<Void> logout() {
        StpUtil.logout();
        return Result.success();
    }

    @GetMapping("/info")
    @SaCheckLogin
    public Result<Admin> getAdminInfo() {
        Long adminId = (Long) StpUtil.getLoginId();
        Admin admin = adminService.getAdminById(adminId);
        return Result.success(admin);
    }
}
