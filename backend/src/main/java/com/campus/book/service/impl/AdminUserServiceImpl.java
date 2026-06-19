package com.campus.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.book.common.BusinessException;
import com.campus.book.entity.User;
import com.campus.book.mapper.UserMapper;
import com.campus.book.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserMapper userMapper;

    @Override
    public Page<User> getUsers(Integer page, Integer size, Integer status, String keyword) {
        Page<User> pageResult = new Page<>(page, size);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        if (status != null) {
            wrapper.eq(User::getStatus, status);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(User::getNickname, keyword)
                    .or()
                    .like(User::getPhone, keyword));
        }

        wrapper.orderByDesc(User::getCreateTime);
        return userMapper.selectPage(pageResult, wrapper);
    }

    @Override
    public void updateUserStatus(Long id, Integer status) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setStatus(status);
        userMapper.updateById(user);
    }
}
