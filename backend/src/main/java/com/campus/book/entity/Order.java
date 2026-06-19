package com.campus.book.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("book_order")
public class Order implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String orderNo;
    private Long bookId;
    private Long buyerId;
    private Long sellerId;
    private BigDecimal amount;
    private Integer status;
    private String statusDesc;
    private String payTime;
    private String payMethod;
    private String payStatus;
    private String expressNo;
    private String expressCompany;
    private String shipTime;
    private String receiveTime;
    private String cancelTime;
    private String cancelReason;
    private Long addressId;
    private String adminReason;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;

    // ========== 非数据库字段 ==========
    private Book book;
    private Address address;
    private User buyer;
    private User seller;
}
