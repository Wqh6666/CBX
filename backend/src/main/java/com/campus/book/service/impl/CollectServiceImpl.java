package com.campus.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.book.entity.Book;
import com.campus.book.entity.Collect;
import com.campus.book.mapper.BookMapper;
import com.campus.book.mapper.CollectMapper;
import com.campus.book.service.CollectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectServiceImpl implements CollectService {

    private final CollectMapper collectMapper;
    private final BookMapper bookMapper;

    @Override
    public List<Collect> getCollects(Long userId) {
        List<Collect> collects = collectMapper.selectList(new LambdaQueryWrapper<Collect>()
                .eq(Collect::getUserId, userId)
                .orderByDesc(Collect::getCreateTime));
        for (Collect collect : collects) {
            Book book = bookMapper.selectById(collect.getBookId());
            collect.setBook(book);
        }
        return collects;
    }

    @Override
    public void addCollect(Long userId, Long bookId) {
        Collect exist = collectMapper.selectOne(new LambdaQueryWrapper<Collect>()
                .eq(Collect::getUserId, userId)
                .eq(Collect::getBookId, bookId));
        if (exist != null) {
            return;
        }
        Collect collect = new Collect();
        collect.setUserId(userId);
        collect.setBookId(bookId);
        collectMapper.insert(collect);

        // 更新书籍收藏数
        Book book = bookMapper.selectById(bookId);
        if (book != null) {
            book.setCollectCount(book.getCollectCount() + 1);
            bookMapper.updateById(book);
        }
    }

    @Override
    public void removeCollect(Long userId, Long bookId) {
        Collect collect = collectMapper.selectOne(new LambdaQueryWrapper<Collect>()
                .eq(Collect::getUserId, userId)
                .eq(Collect::getBookId, bookId));
        if (collect != null) {
            collectMapper.deleteById(collect.getId());
            // 更新书籍收藏数
            Book book = bookMapper.selectById(bookId);
            if (book != null && book.getCollectCount() > 0) {
                book.setCollectCount(book.getCollectCount() - 1);
                bookMapper.updateById(book);
            }
        }
    }

    @Override
    public boolean isCollected(Long userId, Long bookId) {
        return collectMapper.selectOne(new LambdaQueryWrapper<Collect>()
                .eq(Collect::getUserId, userId)
                .eq(Collect::getBookId, bookId)) != null;
    }
}
