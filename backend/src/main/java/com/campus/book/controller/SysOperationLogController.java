package com.campus.book.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.book.common.Result;
import com.campus.book.entity.SysOperationLog;
import com.campus.book.service.SysOperationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/v1/logs")
@RequiredArgsConstructor
public class SysOperationLogController {

    private final SysOperationLogService sysOperationLogService;

    @GetMapping
    public Result<Page<SysOperationLog>> getLogs(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String keyword) {
        Page<SysOperationLog> pageResult = sysOperationLogService.getLogs(page, size, module, keyword);
        return Result.success(pageResult);
    }
}
