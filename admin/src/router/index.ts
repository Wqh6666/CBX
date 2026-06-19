import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import { useAdminStore } from '@/stores/admin'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/pages/login/index.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('@/layouts/MainLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/pages/dashboard/index.vue'),
        meta: { title: '仪表盘' }
      },
      {
        path: 'book',
        name: 'BookManage',
        component: () => import('@/pages/book/index.vue'),
        meta: { title: '书籍管理' }
      },
      {
        path: 'book/pending',
        name: 'BookPending',
        component: () => import('@/pages/book/pending.vue'),
        meta: { title: '书籍审核' }
      },
      {
        path: 'order',
        name: 'OrderManage',
        component: () => import('@/pages/order/index.vue'),
        meta: { title: '订单管理' }
      },
      {
        path: 'user',
        name: 'UserManage',
        component: () => import('@/pages/user/index.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'category',
        name: 'CategoryManage',
        component: () => import('@/pages/category/index.vue'),
        meta: { title: '分类管理' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const adminStore = useAdminStore()

  if (to.path !== '/login' && !adminStore.isLoggedIn()) {
    next('/login')
  } else if (to.path === '/login' && adminStore.isLoggedIn()) {
    next('/dashboard')
  } else {
    next()
  }
})

export default router
