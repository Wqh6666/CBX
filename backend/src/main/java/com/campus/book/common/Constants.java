package com.campus.book.common;

public class Constants {

    // 书籍成色
    public static final String[] CONDITION_NAMES = {
            "", "全新", "95新", "9成新", "8成新", "7成新及以下"
    };

    // 书籍状态
    public static final int BOOK_STATUS_DRAFT = 0;
    public static final int BOOK_STATUS_ON_SALE = 1;
    public static final int BOOK_STATUS_OFF_SALE = 2;
    public static final int BOOK_STATUS_VIOLATION = 3;

    public static final String[] BOOK_STATUS_NAMES = {
            "待审核", "上架中", "已下架", "违规下架"
    };

    // 订单状态
    public static final int ORDER_STATUS_PENDING_PAY = 0;
    public static final int ORDER_STATUS_PENDING_SHIP = 1;
    public static final int ORDER_STATUS_PENDING_RECEIVE = 2;
    public static final int ORDER_STATUS_COMPLETED = 3;
    public static final int ORDER_STATUS_CANCELLED = 4;
    public static final int ORDER_STATUS_REFUNDING = 5;
    public static final int ORDER_STATUS_REFUNDED = 6;

    public static final String[] ORDER_STATUS_NAMES = {
            "待付款", "待发货", "待收货", "已完成", "已取消", "退款中", "已退款"
    };

    // 用户状态
    public static final int USER_STATUS_NORMAL = 1;
    public static final int USER_STATUS_DISABLED = 0;

    // 管理员状态
    public static final int ADMIN_STATUS_NORMAL = 1;
    public static final int ADMIN_STATUS_DISABLED = 0;

    // 性别
    public static final int GENDER_UNKNOWN = 0;
    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 2;

    // 收藏状态
    public static final int COLLECT_STATUS_NORMAL = 1;
    public static final int COLLECT_STATUS_DELETED = 0;

    // 操作日志模块
    public static final String MODULE_BOOK = "书籍管理";
    public static final String MODULE_ORDER = "订单管理";
    public static final String MODULE_USER = "用户管理";
    public static final String MODULE_CATEGORY = "分类管理";
    public static final String MODULE_NOTICE = "公告管理";
    public static final String MODULE_SYSTEM = "系统设置";
}
