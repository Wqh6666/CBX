package com.campus.book.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class MessageSendDTO {
    @NotBlank(message = "接收者不能为空")
    private Long toUserId;

    @NotBlank(message = "消息内容不能为空")
    private String content;
}
