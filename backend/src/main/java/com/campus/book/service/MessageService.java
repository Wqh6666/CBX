package com.campus.book.service;

import com.campus.book.dto.MessageSendDTO;
import com.campus.book.entity.Message;
import java.util.List;
import java.util.Map;

public interface MessageService {
    Map<String, Object> getConversations(Long userId);
    List<Message> getMessages(Long userId, Long otherUserId, Integer page, Integer size);
    Message sendMessage(Long senderId, MessageSendDTO dto);
}
