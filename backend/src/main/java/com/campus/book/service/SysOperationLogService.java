package com.campus.book.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.book.entity.SysOperationLog;

public interface SysOperationLogService {
    void saveLog(Long adminId, String module, String action, Long targetId, String detail, String ip);
    Page<SysOperationLog> getLogs(Integer page, Integer size, String module, String keyword);
}
