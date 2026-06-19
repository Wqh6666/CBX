package com.campus.book.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("collect")
public class Collect implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private Long userId;
    private Long bookId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
