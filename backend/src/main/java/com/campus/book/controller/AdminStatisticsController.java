package com.campus.book.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.campus.book.common.Result;
import com.campus.book.service.AdminStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/v1/statistics")
@RequiredArgsConstructor
public class AdminStatisticsController {

    private final AdminStatisticsService adminStatisticsService;

    @GetMapping("/dashboard")
    public Result<Map<String, Object>> getDashboard() {
        Map<String, Object> stats = adminStatisticsService.getDashboard();
        return Result.success(stats);
    }

    @GetMapping("/order-stats")
    public Result<List<Map<String, Object>>> getOrderStats(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        List<Map<String, Object>> stats = adminStatisticsService.getOrderStatsByDate(startDate, endDate);
        return Result.success(stats);
    }

    @GetMapping("/category-stats")
    public Result<List<Map<String, Object>>> getCategoryStats() {
        List<Map<String, Object>> stats = adminStatisticsService.getCategoryBookStats();
        return Result.success(stats);
    }
}
