package com.campus.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.book.common.BusinessException;
import com.campus.book.common.Constants;
import com.campus.book.dto.BookCreateDTO;
import com.campus.book.dto.BookQueryDTO;
import com.campus.book.entity.Book;
import com.campus.book.entity.Category;
import com.campus.book.entity.User;
import com.campus.book.mapper.BookMapper;
import com.campus.book.mapper.CategoryMapper;
import com.campus.book.mapper.UserMapper;
import com.campus.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;

    @Override
    public Page<Book> getBooks(BookQueryDTO dto) {
        Page<Book> page = new Page<>(dto.getPage(), dto.getSize());
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Book::getStatus, Constants.BOOK_STATUS_ON_SALE);

        if (dto.getCategoryId() != null) {
            wrapper.eq(Book::getCategoryId, dto.getCategoryId());
        }
        if (StringUtils.hasText(dto.getKeyword())) {
            wrapper.and(w -> w.like(Book::getTitle, dto.getKeyword())
                    .or()
                    .like(Book::getAuthor, dto.getKeyword())
                    .or()
                    .like(Book::getIsbn, dto.getKeyword()));
        }

        // 排序
        if ("latest".equals(dto.getSort())) {
            wrapper.orderByDesc(Book::getCreateTime);
        } else if ("price_asc".equals(dto.getSort())) {
            wrapper.orderByAsc(Book::getPrice);
        } else if ("price_desc".equals(dto.getSort())) {
            wrapper.orderByDesc(Book::getPrice);
        } else {
            wrapper.orderByDesc(Book::getCreateTime);
        }

        Page<Book> result = bookMapper.selectPage(page, wrapper);

        // 填充卖家信息
        for (Book book : result.getRecords()) {
            fillBookInfo(book);
        }

        return result;
    }

    @Override
    public Book getBookById(Long id) {
        Book book = bookMapper.selectById(id);
        if (book == null) {
            throw new BusinessException("书籍不存在");
        }
        fillBookInfo(book);
        return book;
    }

    @Override
    public Long createBook(Long sellerId, BookCreateDTO dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setIsbn(dto.getIsbn());
        book.setPublisher(dto.getPublisher());
        book.setCategoryId(dto.getCategoryId());
        book.setCoverImage(dto.getCoverImage());
        book.setImages(dto.getImages());
        book.setDescription(dto.getDescription());
        book.setOriginalPrice(dto.getOriginalPrice());
        book.setPrice(dto.getPrice());
        book.setCondition(dto.getCondition());
        book.setStatus(Constants.BOOK_STATUS_ON_SALE);
        book.setSellerId(sellerId);

        bookMapper.insert(book);
        return book.getId();
    }

    @Override
    public void updateBook(Long id, BookCreateDTO dto) {
        Book book = bookMapper.selectById(id);
        if (book == null) {
            throw new BusinessException("书籍不存在");
        }
        if (dto.getTitle() != null) book.setTitle(dto.getTitle());
        if (dto.getAuthor() != null) book.setAuthor(dto.getAuthor());
        if (dto.getIsbn() != null) book.setIsbn(dto.getIsbn());
        if (dto.getPublisher() != null) book.setPublisher(dto.getPublisher());
        if (dto.getCategoryId() != null) book.setCategoryId(dto.getCategoryId());
        if (dto.getCoverImage() != null) book.setCoverImage(dto.getCoverImage());
        if (dto.getImages() != null) book.setImages(dto.getImages());
        if (dto.getDescription() != null) book.setDescription(dto.getDescription());
        if (dto.getOriginalPrice() != null) book.setOriginalPrice(dto.getOriginalPrice());
        if (dto.getPrice() != null) book.setPrice(dto.getPrice());
        if (dto.getCondition() != null) book.setCondition(dto.getCondition());

        bookMapper.updateById(book);
    }

    @Override
    public void deleteBook(Long id) {
        bookMapper.deleteById(id);
    }

    @Override
    public Page<Book> getMyBooks(Long sellerId) {
        Page<Book> page = new Page<>(1, 100);
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Book::getSellerId, sellerId);
        wrapper.orderByDesc(Book::getCreateTime);

        Page<Book> result = bookMapper.selectPage(page, wrapper);
        for (Book book : result.getRecords()) {
            fillBookInfo(book);
        }
        return result;
    }

    @Override
    public void incrementViewCount(Long id) {
        Book book = bookMapper.selectById(id);
        if (book != null) {
            book.setViewCount(book.getViewCount() + 1);
            bookMapper.updateById(book);
        }
    }

    private void fillBookInfo(Book book) {
        // 填充成色名称
        if (book.getCondition() != null && book.getCondition() >= 1 && book.getCondition() <= 5) {
            book.setConditionName(Constants.CONDITION_NAMES[book.getCondition()]);
        }

        // 填充分类名称
        if (book.getCategoryId() != null) {
            Category category = categoryMapper.selectById(book.getCategoryId());
            if (category != null) {
                book.setCategoryName(category.getName());
            }
        }

        // 填充卖家信息
        if (book.getSellerId() != null) {
            User seller = userMapper.selectById(book.getSellerId());
            if (seller != null) {
                book.setSellerName(seller.getNickname());
                book.setSellerAvatar(seller.getAvatar());
            }
        }
    }
}
