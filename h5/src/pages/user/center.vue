<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { Cell, CellGroup, Image as VanImage } from 'vant'
import MainLayout from '@/layouts/MainLayout.vue'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const userInfo = computed(() => userStore.userInfo)

const menuItems = [
  { icon: 'orders-o', title: '我的订单', path: '/order/list', badge: 0 },
  { icon: 'bookmark-o', title: '我的发布', path: '/user/my-books', badge: 0 },
  { icon: 'star-o', title: '我的收藏', path: '/user/collect', badge: 0 },
  { icon: 'location-o', title: '收货地址', path: '/user/address', badge: 0 },
  { icon: 'chat-o', title: '消息中心', path: '/message', badge: 0 }
]

const onLogout = () => {
  userStore.logout()
  router.replace('/user/login')
}

const onMenuClick = (path: string) => {
  router.push(path)
}
</script>

<template>
  <MainLayout>
    <div class="page-container">
      <!-- 用户信息头部 -->
      <div class="user-header">
        <div class="user-info" @click="router.push('/user/login')">
          <van-image
            :src="userInfo?.avatar || 'https://picsum.photos/40/40?random=200'"
            round
            width="60"
            height="60"
          />
          <div v-if="userInfo" class="info-text">
            <div class="nickname">{{ userInfo.nickname }}</div>
            <div class="phone text-secondary text-small">{{ userInfo.phone }}</div>
          </div>
          <div v-else class="login-tip">
            <span>点击登录</span>
          </div>
        </div>
      </div>

      <!-- 快捷入口 -->
      <div class="quick-entry">
        <div class="entry-item" @click="router.push('/order/list?status=1')">
          <van-icon name="paid" size="24" color="#FF9800" />
          <span>待付款</span>
        </div>
        <div class="entry-item" @click="router.push('/order/list?status=2')">
          <van-icon name="logistics" size="24" color="#2196F3" />
          <span>待发货</span>
        </div>
        <div class="entry-item" @click="router.push('/order/list?status=3')">
          <van-icon name="todo-list-o" size="24" color="#9C27B0" />
          <span>待收货</span>
        </div>
        <div class="entry-item" @click="router.push('/order/list?status=4')">
          <van-icon name="completed" size="24" color="#4CAF50" />
          <span>已完成</span>
        </div>
      </div>

      <!-- 菜单列表 -->
      <div class="menu-section">
        <van-cell-group inset>
          <van-cell
            v-for="item in menuItems"
            :key="item.path"
            :icon="item.icon"
            :title="item.title"
            is-link
            @click="onMenuClick(item.path)"
          />
        </van-cell-group>
      </div>

      <!-- 退出登录 -->
      <div v-if="userInfo" class="logout-section">
        <van-button block round plain @click="onLogout">退出登录</van-button>
      </div>
    </div>
  </MainLayout>
</template>

<style scoped>
.user-header {
  background: linear-gradient(135deg, #FF6B35, #FF8F65);
  padding: 30px 16px 40px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.info-text {
  flex: 1;
}

.nickname {
  font-size: 18px;
  font-weight: 600;
  color: #fff;
  margin-bottom: 4px;
}

.login-tip {
  flex: 1;
  font-size: 16px;
  color: #fff;
}

.quick-entry {
  display: flex;
  justify-content: space-around;
  background: #fff;
  margin: -20px 12px 12px;
  border-radius: 12px;
  padding: 16px 0;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.entry-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
}

.entry-item span {
  font-size: 12px;
  color: #646566;
}

.menu-section {
  padding: 0 12px;
}

.menu-section :deep(.van-cell-group) {
  margin: 0;
}

.menu-section :deep(.van-cell) {
  padding: 14px 16px;
}

.logout-section {
  padding: 24px 16px;
}

.logout-section .van-button {
  border-color: #969799;
  color: #646566;
}
</style>
