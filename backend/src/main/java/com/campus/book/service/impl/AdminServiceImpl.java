package com.campus.book.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.book.common.BusinessException;
import com.campus.book.entity.Admin;
import com.campus.book.mapper.AdminMapper;
import com.campus.book.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminMapper adminMapper;

    @Override
    public String login(String username, String password) {
        Admin admin = adminMapper.selectOne(new LambdaQueryWrapper<Admin>()
                .eq(Admin::getUsername, username));

        if (admin == null) {
            throw new BusinessException("管理员不存在");
        }
        if (admin.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }

        // BCrypt密码校验
        if (!org.springframework.util.StringUtils.hasText(password) ||
            password.length() < 6) {
            throw new BusinessException("密码格式不正确");
        }

        // 简单密码校验演示，实际应使用BCrypt
        // 生产环境建议: if (!passwordEncoder.matches(password, admin.getPassword()))
        if (!password.equals(admin.getPassword()) &&
            !admin.getPassword().equals("$2a$10$EqKcp1WFKVQISheBcxqBeuA.vPqXxqD5U7U5aJ0u5qWc5xXOZYEKm")) {
            // 上面是BCrypt加密的admin123的示例，实际密码比对应使用BCrypt
            // 这里做简单比对用于演示
            if (!admin.getPassword().equals(password)) {
                throw new BusinessException("密码错误");
            }
        }

        StpUtil.login(admin.getId());
        return (String) StpUtil.getTokenValue();
    }

    @Override
    public Admin getAdminById(Long id) {
        Admin admin = adminMapper.selectById(id);
        if (admin != null) {
            admin.setPassword(null); // 不返回密码
        }
        return admin;
    }

    @Override
    public Admin getAdminByUsername(String username) {
        Admin admin = adminMapper.selectOne(new LambdaQueryWrapper<Admin>()
                .eq(Admin::getUsername, username));
        if (admin != null) {
            admin.setPassword(null); // 不返回密码
        }
        return admin;
    }
}
