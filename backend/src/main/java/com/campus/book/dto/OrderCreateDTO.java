package com.campus.book.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class OrderCreateDTO {
    @NotNull(message = "书籍ID不能为空")
    private Long bookId;

    @NotNull(message = "地址ID不能为空")
    private Long addressId;

    private Integer quantity = 1;
}
