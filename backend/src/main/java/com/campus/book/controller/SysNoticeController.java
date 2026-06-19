package com.campus.book.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.campus.book.common.Result;
import com.campus.book.entity.SysNotice;
import com.campus.book.service.SysNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/v1/notices")
@RequiredArgsConstructor
public class SysNoticeController {

    private final SysNoticeService sysNoticeService;

    @GetMapping
    public Result<List<SysNotice>> getNotices() {
        List<SysNotice> notices = sysNoticeService.getNotices();
        return Result.success(notices);
    }

    @GetMapping("/{id}")
    public Result<SysNotice> getNoticeById(@PathVariable Long id) {
        SysNotice notice = sysNoticeService.getNoticeById(id);
        return Result.success(notice);
    }

    @PostMapping
    public Result<Long> createNotice(@RequestBody SysNotice notice) {
        Long id = sysNoticeService.createNotice(notice);
        return Result.success(id);
    }

    @PutMapping("/{id}")
    public Result<Void> updateNotice(@PathVariable Long id, @RequestBody SysNotice notice) {
        sysNoticeService.updateNotice(id, notice);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteNotice(@PathVariable Long id) {
        sysNoticeService.deleteNotice(id);
        return Result.success();
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody SysNotice notice) {
        sysNoticeService.updateStatus(id, notice.getStatus());
        return Result.success();
    }
}
