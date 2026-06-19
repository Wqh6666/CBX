package com.campus.book.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.campus.book.common.Result;
import com.campus.book.entity.Collect;
import com.campus.book.service.CollectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/v1/collects")
@RequiredArgsConstructor
public class CollectController {

    private final CollectService collectService;

    @GetMapping
    public Result<List<Collect>> getCollects() {
        Long userId = (Long) StpUtil.getLoginId();
        List<Collect> collects = collectService.getCollects(userId);
        return Result.success(collects);
    }

    @PostMapping
    public Result<Void> addCollect(@RequestBody Map<String, Long> params) {
        Long userId = (Long) StpUtil.getLoginId();
        Long bookId = params.get("bookId");
        collectService.addCollect(userId, bookId);
        return Result.success();
    }

    @DeleteMapping("/{bookId}")
    public Result<Void> removeCollect(@PathVariable Long bookId) {
        Long userId = (Long) StpUtil.getLoginId();
        collectService.removeCollect(userId, bookId);
        return Result.success();
    }
}
