package com.campus.book.service;

import java.util.List;
import java.util.Map;

public interface AdminStatisticsService {
    Map<String, Object> getDashboard();
    List<Map<String, Object>> getOrderStatsByDate(String startDate, String endDate);
    List<Map<String, Object>> getCategoryBookStats();
}
