package com.campus.book.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.book.common.BusinessException;
import com.campus.book.dto.LoginDTO;
import com.campus.book.entity.User;
import com.campus.book.mapper.UserMapper;
import com.campus.book.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public String login(LoginDTO dto) {
        // 验证验证码
        String codeKey = "login:code:" + dto.getPhone();
        String cachedCode = redisTemplate.opsForValue().get(codeKey);
        if (cachedCode == null || !cachedCode.equals(dto.getCode())) {
            throw new BusinessException("验证码错误");
        }

        // 查找或创建用户
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getPhone, dto.getPhone()));
        if (user == null) {
            user = new User();
            user.setPhone(dto.getPhone());
            user.setNickname("用户" + dto.getPhone().substring(dto.getPhone().length() - 4));
            user.setStatus(1);
            userMapper.insert(user);
        }

        if (user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }

        // 删除验证码
        redisTemplate.delete(codeKey);

        // 生成Token并登录
        StpUtil.login(user.getId());
        return (String) StpUtil.getTokenValue();
    }

    @Override
    public User getUserInfo(Long userId) {
        return getUserById(userId);
    }

    @Override
    public User getUserById(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return user;
    }

    @Override
    public void updateUserInfo(Long userId, User user) {
        User existUser = getUserById(userId);
        if (user.getNickname() != null) {
            existUser.setNickname(user.getNickname());
        }
        if (user.getAvatar() != null) {
            existUser.setAvatar(user.getAvatar());
        }
        userMapper.updateById(existUser);
    }

    @Override
    public String sendCode(String phone) {
        // 生成6位验证码
        String code = RandomUtil.randomNumbers(6);
        String codeKey = "login:code:" + phone;
        redisTemplate.opsForValue().set(codeKey, code, 5, TimeUnit.MINUTES);
        // TODO: 调用短信服务发送验证码
        return code;
    }
}
