<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useCartStore } from '@/stores/cart'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const cartStore = useCartStore()
const userStore = useUserStore()

const active = computed(() => {
  const path = route.path
  if (path === '/') return 0
  if (path === '/category') return 1
  if (path === '/cart') return 2
  if (path === '/user/center') return 3
  return 0
})

const cartCount = computed(() => cartStore.getCount())

const onTabChange = (index: number) => {
  const tabs = ['/', '/category', '/cart', '/user/center']
  router.push(tabs[index])
}

const onSearchClick = () => {
  router.push('/search')
}
</script>

<template>
  <div class="layout">
    <div class="content">
      <slot />
    </div>
    <van-tabbar
      :model-value="active"
      @change="onTabChange"
      class="tabbar"
      active-color="#FF6B35"
      inactive-color="#969799"
    >
      <van-tabbar-item icon="wap-home">首页</van-tabbar-item>
      <van-tabbar-item icon="apps-o">分类</van-tabbar-item>
      <van-tabbar-item icon="shopping-cart-o" :badge="cartCount > 0 ? cartCount : ''">购物车</van-tabbar-item>
      <van-tabbar-item icon="user-o">我的</van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<style scoped>
.layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.content {
  flex: 1;
  padding-bottom: 50px;
}

.tabbar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 50px;
  padding-bottom: env(safe-area-inset-bottom);
}
</style>
