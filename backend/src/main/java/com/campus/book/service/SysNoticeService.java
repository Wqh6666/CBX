package com.campus.book.service;

import com.campus.book.entity.SysNotice;
import java.util.List;

public interface SysNoticeService {
    List<SysNotice> getNotices();
    SysNotice getNoticeById(Long id);
    Long createNotice(SysNotice notice);
    void updateNotice(Long id, SysNotice notice);
    void deleteNotice(Long id);
    void updateStatus(Long id, Integer status);
}
