-- =========================================
-- 校园二手书交易平台 数据库建表脚本
-- 数据库: campus_book
-- =========================================

CREATE DATABASE IF NOT EXISTS `campus_book` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `campus_book`;

-- =========================================
-- 1. 用户表 (sys_user)
-- =========================================
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
    `id` BIGINT NOT NULL COMMENT '用户ID',
    `phone` VARCHAR(20) NOT NULL COMMENT '手机号',
    `password` VARCHAR(100) NOT NULL COMMENT '密码(BCrypt加密)',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `gender` TINYINT DEFAULT 0 COMMENT '性别: 0未知 1男 2女',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 1正常 0禁用',
    `disabled_reason` VARCHAR(255) DEFAULT NULL COMMENT '禁用原因',
    `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
    `last_login_ip` VARCHAR(50) DEFAULT NULL COMMENT '最后登录IP',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除: 0未删除 1已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- =========================================
-- 2. 管理员表 (sys_admin)
-- =========================================
DROP TABLE IF EXISTS `sys_admin`;
CREATE TABLE `sys_admin` (
    `id` BIGINT NOT NULL COMMENT '管理员ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码(BCrypt加密)',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 1正常 0禁用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员表';

-- =========================================
-- 3. 书籍分类表 (category)
-- =========================================
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
    `id` BIGINT NOT NULL COMMENT '分类ID',
    `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父级ID: 0表示顶级分类',
    `level` TINYINT DEFAULT 1 COMMENT '层级: 1一级 2二级',
    `sort` INT DEFAULT 0 COMMENT '排序: 数字越小越靠前',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 1启用 0禁用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除: 0未删除 1已删除',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='书籍分类表';

-- =========================================
-- 4. 书籍表 (book)
-- =========================================
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
    `id` BIGINT NOT NULL COMMENT '书籍ID',
    `title` VARCHAR(200) NOT NULL COMMENT '书名',
    `author` VARCHAR(100) DEFAULT NULL COMMENT '作者',
    `isbn` VARCHAR(50) DEFAULT NULL COMMENT 'ISBN',
    `publisher` VARCHAR(100) DEFAULT NULL COMMENT '出版社',
    `category_id` BIGINT DEFAULT NULL COMMENT '分类ID',
    `cover_image` VARCHAR(500) DEFAULT NULL COMMENT '封面图片URL',
    `images` VARCHAR(2000) DEFAULT NULL COMMENT '详情图片URLs(JSON数组)',
    `description` TEXT DEFAULT NULL COMMENT '书籍描述',
    `original_price` DECIMAL(10,2) DEFAULT NULL COMMENT '原价',
    `price` DECIMAL(10,2) NOT NULL COMMENT '售价',
    `condition` TINYINT NOT NULL COMMENT '成色: 1全新 2九五新 3九成新 4八成新 5七成新及以下',
    `status` TINYINT DEFAULT 0 COMMENT '状态: 0草稿 1上架 2下架 3违规下架',
    `reject_reason` VARCHAR(255) DEFAULT NULL COMMENT '审核拒绝原因/下架原因',
    `seller_id` BIGINT NOT NULL COMMENT '卖家ID',
    `buyer_id` BIGINT DEFAULT NULL COMMENT '买家ID(有订单时记录)',
    `view_count` INT DEFAULT 0 COMMENT '浏览次数',
    `collect_count` INT DEFAULT 0 COMMENT '收藏次数',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除: 0未删除 1已删除',
    PRIMARY KEY (`id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_seller_id` (`seller_id`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='书籍表';

-- =========================================
-- 5. 收货地址表 (address)
-- =========================================
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address` (
    `id` BIGINT NOT NULL COMMENT '地址ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `consignee` VARCHAR(50) NOT NULL COMMENT '收货人姓名',
    `phone` VARCHAR(20) NOT NULL COMMENT '手机号',
    `province` VARCHAR(50) NOT NULL COMMENT '省份',
    `city` VARCHAR(50) NOT NULL COMMENT '城市',
    `district` VARCHAR(50) NOT NULL COMMENT '区县',
    `detail` VARCHAR(200) NOT NULL COMMENT '详细地址',
    `is_default` TINYINT DEFAULT 0 COMMENT '是否默认: 0否 1是',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收货地址表';

-- =========================================
-- 6. 购物车表 (cart)
-- =========================================
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart` (
    `id` BIGINT NOT NULL COMMENT '购物车ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `book_id` BIGINT NOT NULL COMMENT '书籍ID',
    `quantity` INT DEFAULT 1 COMMENT '数量',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_book` (`user_id`, `book_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_book_id` (`book_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='购物车表';

-- =========================================
-- 7. 订单表 (book_order)
-- =========================================
DROP TABLE IF EXISTS `book_order`;
CREATE TABLE `book_order` (
    `id` BIGINT NOT NULL COMMENT '订单ID',
    `order_no` VARCHAR(32) NOT NULL COMMENT '订单号',
    `book_id` BIGINT NOT NULL COMMENT '书籍ID',
    `buyer_id` BIGINT NOT NULL COMMENT '买家ID',
    `seller_id` BIGINT NOT NULL COMMENT '卖家ID',
    `amount` DECIMAL(10,2) NOT NULL COMMENT '订单金额',
    `status` TINYINT DEFAULT 0 COMMENT '状态: 0待付款 1待发货 2待收货 3已完成 4已取消 5退款中 6已退款',
    `status_desc` VARCHAR(50) DEFAULT NULL COMMENT '状态描述',
    `pay_time` DATETIME DEFAULT NULL COMMENT '支付时间',
    `pay_method` VARCHAR(20) DEFAULT NULL COMMENT '支付方式: wechat/alipay',
    `pay_status` VARCHAR(20) DEFAULT NULL COMMENT '支付状态',
    `express_no` VARCHAR(50) DEFAULT NULL COMMENT '快递单号',
    `express_company` VARCHAR(50) DEFAULT NULL COMMENT '快递公司',
    `ship_time` DATETIME DEFAULT NULL COMMENT '发货时间',
    `receive_time` DATETIME DEFAULT NULL COMMENT '收货时间',
    `cancel_time` DATETIME DEFAULT NULL COMMENT '取消时间',
    `cancel_reason` VARCHAR(255) DEFAULT NULL COMMENT '取消原因',
    `address_id` BIGINT NOT NULL COMMENT '收货地址ID',
    `admin_reason` VARCHAR(255) DEFAULT NULL COMMENT '管理员操作原因',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除: 0未删除 1已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_buyer_id` (`buyer_id`),
    KEY `idx_seller_id` (`seller_id`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- =========================================
-- 8. 收藏表 (collect)
-- =========================================
DROP TABLE IF EXISTS `collect`;
CREATE TABLE `collect` (
    `id` BIGINT NOT NULL COMMENT '收藏ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `book_id` BIGINT NOT NULL COMMENT '书籍ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_book` (`user_id`, `book_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_book_id` (`book_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收藏表';

-- =========================================
-- 9. 消息表 (message)
-- =========================================
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
    `id` BIGINT NOT NULL COMMENT '消息ID',
    `sender_id` BIGINT NOT NULL COMMENT '发送者ID',
    `receiver_id` BIGINT NOT NULL COMMENT '接收者ID',
    `content` VARCHAR(500) NOT NULL COMMENT '消息内容',
    `read_status` TINYINT DEFAULT 0 COMMENT '阅读状态: 0未读 1已读',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_sender_id` (`sender_id`),
    KEY `idx_receiver_id` (`receiver_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息表';

-- =========================================
-- 10. 系统公告表 (sys_notice)
-- =========================================
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice` (
    `id` BIGINT NOT NULL COMMENT '公告ID',
    `title` VARCHAR(100) NOT NULL COMMENT '公告标题',
    `content` TEXT DEFAULT NULL COMMENT '公告内容',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 1上线 0下线',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统公告表';

-- =========================================
-- 11. 操作日志表 (sys_operation_log)
-- =========================================
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log` (
    `id` BIGINT NOT NULL COMMENT '日志ID',
    `admin_id` BIGINT NOT NULL COMMENT '管理员ID',
    `module` VARCHAR(50) DEFAULT NULL COMMENT '操作模块',
    `action` VARCHAR(50) DEFAULT NULL COMMENT '操作动作',
    `target_id` BIGINT DEFAULT NULL COMMENT '目标ID',
    `detail` VARCHAR(500) DEFAULT NULL COMMENT '操作详情',
    `ip` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_admin_id` (`admin_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- =========================================
-- 初始化数据
-- =========================================

-- 插入超级管理员 (密码: admin123)
INSERT INTO `sys_admin` (`id`, `username`, `password`, `nickname`, `status`) VALUES
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '超级管理员', 1);

-- 插入测试用户 (密码: 123456)
INSERT INTO `sys_user` (`id`, `phone`, `password`, `nickname`, `avatar`, `gender`, `status`) VALUES
(1, '13800138000', '$2a$10$EqKcp1WFKVQISheBcxqBeuA.vPqXxqD5U7U5aJ0u5qWc5xXOZYEKm', '小明同学', 'https://picsum.photos/40/40?random=100', 1, 1),
(2, '13900139000', '$2a$10$EqKcp1WFKVQISheBcxqBeuA.vPqXxqD5U7U5aJ0u5qWc5xXOZYEKm', '学霸小李', 'https://picsum.photos/40/40?random=101', 1, 1),
(3, '13700137000', '$2a$10$EqKcp1WFKVQISheBcxqBeuA.vPqXxqD5U7U5aJ0u5qWc5xXOZYEKm', '文艺小王', 'https://picsum.photos/40/40?random=102', 2, 1);

-- 插入分类数据
INSERT INTO `category` (`id`, `name`, `parent_id`, `level`, `sort`, `status`) VALUES
(1, '教材', 0, 1, 1, 1),
(2, '小说', 0, 1, 2, 1),
(3, '专业书', 0, 1, 3, 1),
(4, '杂志', 0, 1, 4, 1),
(5, '工具书', 0, 1, 5, 1),
(6, '其他', 0, 1, 6, 1),
-- 二级分类
(11, '大学英语', 1, 2, 1, 1),
(12, '高等数学', 1, 2, 2, 1),
(13, '线性代数', 1, 2, 3, 1),
(14, '计算机', 3, 2, 1, 1),
(15, '经管', 3, 2, 2, 1);

-- 插入测试书籍
INSERT INTO `book` (`id`, `title`, `author`, `isbn`, `publisher`, `category_id`, `cover_image`, `description`, `original_price`, `price`, `condition`, `status`, `seller_id`, `view_count`, `collect_count`) VALUES
(1, '高等数学第七版上册', '同济大学数学系', '9787040396614', '高等教育出版社', 12, 'https://picsum.photos/375/500?random=10', '经典高数教材，适合大一学生使用，内容包括函数与极限、导数与微分等', 45.00, 25.00, 3, 1, 1, 128, 12),
(2, '算法导论第四版', 'Thomas H. Cormen', '9787111407010', '机械工业出版社', 14, 'https://picsum.photos/375/500?random=11', '计算机专业经典算法教材，详细介绍了各种算法设计思想', 128.00, 65.00, 2, 1, 2, 256, 34),
(3, '平凡的世界', '路遥', '9787530216780', '北京十月文艺出版社', 2, 'https://picsum.photos/375/500?random=12', '茅盾文学奖获奖作品，讲述普通人的奋斗历程', 68.00, 30.00, 4, 1, 3, 89, 8),
(4, 'Python编程：从入门到实践', 'Eric Matthes', '9787115428028', '人民邮电出版社', 14, 'https://picsum.photos/375/500?random=13', 'Python入门经典书籍，适合零基础学习', 89.00, 45.00, 1, 1, 1, 312, 56);

-- 插入测试地址
INSERT INTO `address` (`id`, `user_id`, `consignee`, `phone`, `province`, `city`, `district`, `detail`, `is_default`) VALUES
(1, 1, '张三', '13800138000', '上海市', '上海市', '杨浦区', '复旦大学邯郸路220号', 1),
(2, 1, '李四', '13900139000', '上海市', '上海市', '浦东新区', '张江高科技园区', 0);
