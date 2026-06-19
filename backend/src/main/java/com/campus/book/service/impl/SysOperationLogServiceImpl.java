package com.campus.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.book.entity.SysOperationLog;
import com.campus.book.mapper.SysOperationLogMapper;
import com.campus.book.service.SysOperationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SysOperationLogServiceImpl implements SysOperationLogService {

    private final SysOperationLogMapper sysOperationLogMapper;

    @Override
    public void saveLog(Long adminId, String module, String action, Long targetId, String detail, String ip) {
        SysOperationLog log = new SysOperationLog();
        log.setAdminId(adminId);
        log.setModule(module);
        log.setAction(action);
        log.setTargetId(targetId);
        log.setDetail(detail);
        log.setIp(ip);
        sysOperationLogMapper.insert(log);
    }

    @Override
    public Page<SysOperationLog> getLogs(Integer page, Integer size, String module, String keyword) {
        Page<SysOperationLog> pageResult = new Page<>(page, size);
        LambdaQueryWrapper<SysOperationLog> wrapper = new LambdaQueryWrapper<>();

        if (module != null && !module.isEmpty()) {
            wrapper.eq(SysOperationLog::getModule, module);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(SysOperationLog::getDetail, keyword);
        }

        wrapper.orderByDesc(SysOperationLog::getCreateTime);
        return sysOperationLogMapper.selectPage(pageResult, wrapper);
    }
}
