import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/pages/home/index.vue'),
    meta: { title: '首页' }
  },
  {
    path: '/category',
    name: 'Category',
    component: () => import('@/pages/category/index.vue'),
    meta: { title: '分类' }
  },
  {
    path: '/search',
    name: 'Search',
    component: () => import('@/pages/search/index.vue'),
    meta: { title: '搜索' }
  },
  {
    path: '/book/:id',
    name: 'BookDetail',
    component: () => import('@/pages/book/detail.vue'),
    meta: { title: '书籍详情' }
  },
  {
    path: '/book/publish',
    name: 'PublishBook',
    component: () => import('@/pages/book/publish.vue'),
    meta: { title: '发布书籍', requiresAuth: true }
  },
  {
    path: '/book/edit/:id',
    name: 'EditBook',
    component: () => import('@/pages/book/publish.vue'),
    meta: { title: '编辑书籍', requiresAuth: true }
  },
  {
    path: '/cart',
    name: 'Cart',
    component: () => import('@/pages/cart/index.vue'),
    meta: { title: '购物车', requiresAuth: true }
  },
  {
    path: '/order/confirm',
    name: 'ConfirmOrder',
    component: () => import('@/pages/order/confirm.vue'),
    meta: { title: '确认订单', requiresAuth: true }
  },
  {
    path: '/order/list',
    name: 'OrderList',
    component: () => import('@/pages/order/list.vue'),
    meta: { title: '我的订单', requiresAuth: true }
  },
  {
    path: '/order/detail/:id',
    name: 'OrderDetail',
    component: () => import('@/pages/order/detail.vue'),
    meta: { title: '订单详情', requiresAuth: true }
  },
  {
    path: '/order/pay/:id',
    name: 'PayOrder',
    component: () => import('@/pages/order/pay.vue'),
    meta: { title: '支付', requiresAuth: true }
  },
  {
    path: '/user/login',
    name: 'Login',
    component: () => import('@/pages/user/login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/user/center',
    name: 'UserCenter',
    component: () => import('@/pages/user/center.vue'),
    meta: { title: '个人中心', requiresAuth: true }
  },
  {
    path: '/user/address',
    name: 'AddressList',
    component: () => import('@/pages/user/address-list.vue'),
    meta: { title: '地址管理', requiresAuth: true }
  },
  {
    path: '/user/address/edit',
    name: 'AddressEdit',
    component: () => import('@/pages/user/address-edit.vue'),
    meta: { title: '编辑地址', requiresAuth: true }
  },
  {
    path: '/user/my-books',
    name: 'MyBooks',
    component: () => import('@/pages/user/my-books.vue'),
    meta: { title: '我的发布', requiresAuth: true }
  },
  {
    path: '/user/collect',
    name: 'MyCollect',
    component: () => import('@/pages/user/collect.vue'),
    meta: { title: '我的收藏', requiresAuth: true }
  },
  {
    path: '/message',
    name: 'MessageList',
    component: () => import('@/pages/message/index.vue'),
    meta: { title: '消息中心', requiresAuth: true }
  },
  {
    path: '/message/chat/:userId',
    name: 'Chat',
    component: () => import('@/pages/message/chat.vue'),
    meta: { title: '聊天', requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()

  if (to.meta.requiresAuth && !userStore.isLoggedIn()) {
    next({
      path: '/user/login',
      query: { redirect: to.fullPath }
    })
  } else {
    next()
  }
})

export default router
