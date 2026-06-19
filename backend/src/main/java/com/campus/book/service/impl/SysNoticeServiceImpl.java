package com.campus.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.book.common.BusinessException;
import com.campus.book.entity.SysNotice;
import com.campus.book.mapper.SysNoticeMapper;
import com.campus.book.service.SysNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysNoticeServiceImpl implements SysNoticeService {

    private final SysNoticeMapper sysNoticeMapper;

    @Override
    public List<SysNotice> getNotices() {
        return sysNoticeMapper.selectList(
                new LambdaQueryWrapper<SysNotice>()
                        .eq(SysNotice::getStatus, 1)
                        .orderByDesc(SysNotice::getCreateTime));
    }

    @Override
    public SysNotice getNoticeById(Long id) {
        SysNotice notice = sysNoticeMapper.selectById(id);
        if (notice == null) {
            throw new BusinessException("公告不存在");
        }
        return notice;
    }

    @Override
    public Long createNotice(SysNotice notice) {
        sysNoticeMapper.insert(notice);
        return notice.getId();
    }

    @Override
    public void updateNotice(Long id, SysNotice notice) {
        SysNotice exist = sysNoticeMapper.selectById(id);
        if (exist == null) {
            throw new BusinessException("公告不存在");
        }
        if (notice.getTitle() != null) exist.setTitle(notice.getTitle());
        if (notice.getContent() != null) exist.setContent(notice.getContent());
        if (notice.getStatus() != null) exist.setStatus(notice.getStatus());
        sysNoticeMapper.updateById(exist);
    }

    @Override
    public void deleteNotice(Long id) {
        sysNoticeMapper.deleteById(id);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        SysNotice notice = sysNoticeMapper.selectById(id);
        if (notice == null) {
            throw new BusinessException("公告不存在");
        }
        notice.setStatus(status);
        sysNoticeMapper.updateById(notice);
    }
}
