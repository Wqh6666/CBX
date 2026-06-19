package com.campus.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.book.dto.MessageSendDTO;
import com.campus.book.entity.Message;
import com.campus.book.entity.User;
import com.campus.book.mapper.MessageMapper;
import com.campus.book.mapper.UserMapper;
import com.campus.book.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageMapper messageMapper;
    private final UserMapper userMapper;

    @Override
    public Map<String, Object> getConversations(Long userId) {
        List<Message> allMessages = messageMapper.selectList(
                new LambdaQueryWrapper<Message>()
                        .eq(Message::getSenderId, userId)
                        .or()
                        .eq(Message::getReceiverId, userId)
                        .orderByDesc(Message::getCreateTime));

        Map<Long, Map<String, Object>> conversationMap = new LinkedHashMap<>();

        for (Message msg : allMessages) {
            Long otherUserId = msg.getSenderId().equals(userId) ? msg.getReceiverId() : msg.getSenderId();

            if (!conversationMap.containsKey(otherUserId)) {
                User otherUser = userMapper.selectById(otherUserId);
                Map<String, Object> conv = new HashMap<>();
                conv.put("userId", otherUserId);
                conv.put("userName", otherUser != null ? otherUser.getNickname() : "");
                conv.put("userAvatar", otherUser != null ? otherUser.getAvatar() : "");
                conv.put("lastMessage", msg.getContent());
                conv.put("lastMessageTime", msg.getCreateTime());
                conv.put("unreadCount", 0);
                conversationMap.put(otherUserId, conv);
            }

            // 统计未读消息
            if (msg.getReceiverId().equals(userId) && msg.getReadStatus() == 0) {
                Map<String, Object> conv = conversationMap.get(otherUserId);
                conv.put("unreadCount", (Integer) conv.get("unreadCount") + 1);
            }
        }

        List<Map<String, Object>> result = new ArrayList<>(conversationMap.values());
        return Map.of("list", result);
    }

    @Override
    public List<Message> getMessages(Long userId, Long otherUserId, Integer page, Integer size) {
        Page<Message> messagePage = new Page<>(page, size);
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w
                .eq(Message::getSenderId, userId).eq(Message::getReceiverId, otherUserId)
                .or()
                .eq(Message::getSenderId, otherUserId).eq(Message::getReceiverId, userId));
        wrapper.orderByAsc(Message::getCreateTime);

        Page<Message> result = messageMapper.selectPage(messagePage, wrapper);

        // 标记已读
        messageMapper.update(null, new LambdaQueryWrapper<Message>()
                .eq(Message::getSenderId, otherUserId)
                .eq(Message::getReceiverId, userId)
                .eq(Message::getReadStatus, 0)
                .set(Message::getReadStatus, 1));

        return result.getRecords();
    }

    @Override
    public Message sendMessage(Long senderId, MessageSendDTO dto) {
        Message message = new Message();
        message.setSenderId(senderId);
        message.setReceiverId(dto.getToUserId());
        message.setContent(dto.getContent());
        message.setReadStatus(0);
        messageMapper.insert(message);
        return message;
    }
}
