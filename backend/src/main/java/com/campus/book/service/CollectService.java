package com.campus.book.service;

import com.campus.book.entity.Collect;
import java.util.List;

public interface CollectService {
    List<Collect> getCollects(Long userId);
    void addCollect(Long userId, Long bookId);
    void removeCollect(Long userId, Long bookId);
    boolean isCollected(Long userId, Long bookId);
}
