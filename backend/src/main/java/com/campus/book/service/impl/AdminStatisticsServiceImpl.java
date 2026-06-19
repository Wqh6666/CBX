package com.campus.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.book.entity.Book;
import com.campus.book.entity.Category;
import com.campus.book.entity.Order;
import com.campus.book.entity.User;
import com.campus.book.mapper.BookMapper;
import com.campus.book.mapper.CategoryMapper;
import com.campus.book.mapper.OrderMapper;
import com.campus.book.mapper.UserMapper;
import com.campus.book.service.AdminStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminStatisticsServiceImpl implements AdminStatisticsService {

    private final UserMapper userMapper;
    private final BookMapper bookMapper;
    private final OrderMapper orderMapper;
    private final CategoryMapper categoryMapper;

    @Override
    public Map<String, Object> getDashboard() {
        Map<String, Object> stats = new HashMap<>();

        // 用户统计
        long totalUsers = userMapper.selectCount(null);
        long activeUsers = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getStatus, 1));
        stats.put("totalUsers", totalUsers);
        stats.put("activeUsers", activeUsers);

        // 书籍统计
        long totalBooks = bookMapper.selectCount(null);
        long onSaleBooks = bookMapper.selectCount(new LambdaQueryWrapper<Book>().eq(Book::getStatus, 1));
        long pendingBooks = bookMapper.selectCount(new LambdaQueryWrapper<Book>().eq(Book::getStatus, 0));
        stats.put("totalBooks", totalBooks);
        stats.put("onSaleBooks", onSaleBooks);
        stats.put("pendingBooks", pendingBooks);

        // 订单统计
        long totalOrders = orderMapper.selectCount(null);
        long completedOrders = orderMapper.selectCount(new LambdaQueryWrapper<Order>().eq(Order::getStatus, 3));
        long pendingOrders = orderMapper.selectCount(new LambdaQueryWrapper<Order>()
                .in(Order::getStatus, 0, 1, 2));
        stats.put("totalOrders", totalOrders);
        stats.put("completedOrders", completedOrders);
        stats.put("pendingOrders", pendingOrders);

        // 计算交易额 (已完成订单的总金额)
        List<Order> completedOrderList = orderMapper.selectList(
                new LambdaQueryWrapper<Order>().eq(Order::getStatus, 3).select(Order::getAmount));
        double totalAmount = completedOrderList.stream()
                .filter(o -> o.getAmount() != null)
                .mapToDouble(o -> o.getAmount().doubleValue())
                .sum();
        stats.put("totalAmount", totalAmount);

        return stats;
    }

    @Override
    public List<Map<String, Object>> getOrderStatsByDate(String startDate, String endDate) {
        // 近7天订单统计
        List<Map<String, Object>> result = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            String dateStr = date.format(DateTimeFormatter.ISO_DATE);

            long count = orderMapper.selectCount(
                    new LambdaQueryWrapper<Order>()
                            .like(Order::getCreateTime, dateStr)
                            .eq(Order::getStatus, 3));

            double amount = orderMapper.selectList(
                    new LambdaQueryWrapper<Order>()
                            .like(Order::getCreateTime, dateStr)
                            .eq(Order::getStatus, 3)
                            .select(Order::getAmount))
                    .stream()
                    .filter(o -> o.getAmount() != null)
                    .mapToDouble(o -> o.getAmount().doubleValue())
                    .sum();

            Map<String, Object> dayStat = new HashMap<>();
            dayStat.put("date", dateStr);
            dayStat.put("count", count);
            dayStat.put("amount", amount);
            result.add(dayStat);
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> getCategoryBookStats() {
        List<Category> categories = categoryMapper.selectList(
                new LambdaQueryWrapper<Category>().eq(Category::getParentId, 0).eq(Category::getStatus, 1));

        List<Map<String, Object>> result = new ArrayList<>();

        for (Category category : categories) {
            long bookCount = bookMapper.selectCount(
                    new LambdaQueryWrapper<Book>().eq(Book::getCategoryId, category.getId()));

            Map<String, Object> stat = new HashMap<>();
            stat.put("name", category.getName());
            stat.put("value", bookCount);
            result.add(stat);
        }

        return result;
    }
}
