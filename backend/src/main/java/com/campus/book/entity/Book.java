package com.campus.book.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("book")
public class Book implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
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
    private Integer status;
    private String rejectReason;
    private Long sellerId;
    private Long buyerId;
    private Integer viewCount;
    private Integer collectCount;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;

    // ========== 非数据库字段 ==========
    private String categoryName;
    private String sellerName;
    private String sellerAvatar;
    private String conditionName;
}
