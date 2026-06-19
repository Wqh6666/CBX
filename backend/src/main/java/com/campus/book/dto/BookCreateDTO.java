package com.campus.book.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BookCreateDTO {
    private String title;
    private String author;
    private String isbn;
    private String publisher;
    private Long categoryId;
    private String coverImage;
    private String images;
    private String description;
    private BigDecimal originalPrice;
    private BigDecimal price;
    private Integer condition;
}
