package com.campus.book.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("sys_operation_log")
public class SysOperationLog implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private Long adminId;
    private String module;
    private String action;
    private Long targetId;
    private String detail;
    private String ip;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
