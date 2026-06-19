package com.campus.book.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BookQueryDTO {
    private Long categoryId;
    private String keyword;
    private Integer page = 1;
    private Integer size = 20;
    private String sort;
}
