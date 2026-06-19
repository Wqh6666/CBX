package com.campus.book.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.campus.book.common.Result;
import com.campus.book.dto.MessageSendDTO;
import com.campus.book.entity.Message;
import com.campus.book.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/v1/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/conversations")
    public Result<Map<String, Object>> getConversations() {
        Long userId = (Long) StpUtil.getLoginId();
        Map<String, Object> conversations = messageService.getConversations(userId);
        return Result.success(conversations);
    }

    @GetMapping("/{userId}")
    public Result<List<Message>> getMessages(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        Long currentUserId = (Long) StpUtil.getLoginId();
        List<Message> messages = messageService.getMessages(currentUserId, userId, page, size);
        return Result.success(messages);
    }

    @PostMapping
    public Result<Message> sendMessage(@Valid @RequestBody MessageSendDTO dto) {
        Long userId = (Long) StpUtil.getLoginId();
        Message message = messageService.sendMessage(userId, dto);
        return Result.success(message);
    }
}
