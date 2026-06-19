<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Swipe, SwipeItem, Cell, CellGroup, Button, showToast, showConfirmDialog } from 'vant'
import { Image as VanImage } from 'vant'
import { formatPrice, getConditionName, formatTimeAgo } from '@/utils/format'
import { useUserStore } from '@/stores/user'
import { useCartStore } from '@/stores/cart'
import type { Book } from '@/types'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const cartStore = useCartStore()

const bookId = parseInt(route.params.id as string)
const book = ref<Book | null>(null)
const collected = ref(false)
const loading = ref(false)

const mockBook: Book = {
  id: 1,
  title: '高等数学第七版上册',
  author: '同济大学数学系',
  isbn: '9787040396614',
  publisher: '高等教育出版社',
  categoryId: 2,
  categoryName: '教材',
  coverImage: 'https://picsum.photos/375/375?random=20',
  images: [
    'https://picsum.photos/375/375?random=21',
    'https://picsum.photos/375/375?random=22',
    'https://picsum.photos/375/375?random=23'
  ],
  description: '《高等数学（第七版上册）》是普通高等985、211工程教材，内容涵盖函数与极限、导数与微分、微分中值定理与导数的应用、不定积分、定积分及其应用等内容。本教材配套习题丰富，适合大学一年级学生使用。\n\n特点：\n1. 内容系统完整，讲解详细\n2. 配套习题丰富，难度适中\n3. 适合期末考试和考研基础复习',
  originalPrice: 45,
  price: 25,
  condition: 3,
  conditionName: '9成新',
  status: 1,
  sellerId: 1,
  sellerName: '小明同学',
  sellerAvatar: 'https://picsum.photos/40/40?random=100',
  viewCount: 128,
  collectCount: 12,
  createTime: '2024-01-15 10:30:00'
}

const isOwnBook = computed(() => {
  return userStore.userInfo?.id === book.value?.sellerId
})

const conditionClass = computed(() => {
  const map: Record<number, string> = {
    1: 'condition-new',
    2: 'condition-95',
    3: 'condition-9',
    4: 'condition-8',
    5: 'condition-old'
  }
  return map[book.value?.condition || 1] || ''
})

const onToggleCollect = () => {
  if (!userStore.isLoggedIn()) {
    router.push('/user/login')
    return
  }
  collected.value = !collected.value
  showToast(collected.value ? '收藏成功' : '取消收藏')
}

const onAddCart = async () => {
  if (!userStore.isLoggedIn()) {
    router.push('/user/login')
    return
  }
  if (isOwnBook.value) {
    showToast('不能购买自己的商品')
    return
  }
  cartStore.addItem({
    id: Date.now(),
    userId: userStore.userInfo!.id,
    bookId: book.value!.id,
    book: book.value!,
    quantity: 1,
    createTime: new Date().toISOString()
  })
  showToast('已加入购物车')
}

const onBuyNow = async () => {
  if (!userStore.isLoggedIn()) {
    router.push('/user/login')
    return
  }
  if (isOwnBook.value) {
    showToast('不能购买自己的商品')
    return
  }
  router.push({ path: '/order/confirm', query: { bookId: book.value!.id.toString() } })
}

const onContactSeller = () => {
  if (!userStore.isLoggedIn()) {
    router.push('/user/login')
    return
  }
  router.push(`/message/chat/${book.value!.sellerId}`)
}

onMounted(() => {
  book.value = mockBook
  document.title = book.value.title
})
</script>

<template>
  <div class="page-container">
    <!-- 顶部导航 -->
    <div class="detail-nav">
      <van-icon name="arrow-left" size="20" @click="router.back()" />
      <van-icon name="ellipsis" size="20" />
    </div>

    <!-- 图片轮播 -->
    <div class="detail-swipe">
      <van-swipe :autoplay="0" lazy-render>
        <van-swipe-item>
          <van-image :src="book?.coverImage" width="100%" height="375" fit="cover" />
        </van-swipe-item>
        <van-swipe-item v-for="(img, index) in book?.images" :key="index">
          <van-image :src="img" width="100%" height="375" fit="cover" />
        </van-swipe-item>
      </van-swipe>
    </div>

    <!-- 价格和标题 -->
    <div class="detail-info">
      <div class="price-row">
        <span class="current-price">{{ formatPrice(book?.price || 0) }}</span>
        <span v-if="book?.originalPrice" class="original-price">
          {{ formatPrice(book?.originalPrice || 0) }}
        </span>
        <span class="condition-tag" :class="conditionClass">{{ getConditionName(book?.condition || 1) }}</span>
      </div>
      <h1 class="title">{{ book?.title }}</h1>
      <div class="author text-secondary">{{ book?.author }}</div>
      <div class="meta text-secondary text-small">
        <span>{{ book?.publisher }}</span>
        <span>ISBN: {{ book?.isbn }}</span>
      </div>
    </div>

    <!-- 书籍描述 -->
    <div class="detail-desc">
      <div class="section-title">书籍描述</div>
      <div class="desc-content">{{ book?.description }}</div>
    </div>

    <!-- 卖家信息 -->
    <div class="seller-info">
      <div class="seller-header">
        <span class="section-title">卖家信息</span>
        <span class="view-count text-small text-secondary">浏览 {{ book?.viewCount }}</span>
      </div>
      <div class="seller-card">
        <van-image :src="book?.sellerAvatar" round width="48" height="48" />
        <div class="seller-detail">
          <div class="seller-name">{{ book?.sellerName }}</div>
          <div class="seller-meta text-small text-secondary">
            发布于 {{ formatTimeAgo(book?.createTime || '') }}
          </div>
        </div>
        <van-button size="small" round type="primary" plain @click="onContactSeller">
          私信
        </van-button>
      </div>
    </div>

    <!-- 底部操作栏 -->
    <div class="action-bar">
      <div class="action-icons">
        <div class="action-icon-item" @click="onToggleCollect">
          <van-icon :name="collected ? 'star' : 'star-o'" :color="collected ? '#FF6B35' : '#969799'" size="20" />
          <span class="icon-text">{{ book?.collectCount }}</span>
        </div>
      </div>
      <div class="action-buttons">
        <van-button size="small" round @click="onAddCart">加入购物车</van-button>
        <van-button size="small" round type="primary" @click="onBuyNow">立即购买</van-button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.detail-nav {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background: linear-gradient(to bottom, rgba(0,0,0,0.5), transparent);
  z-index: 100;
  color: #fff;
}

.detail-swipe {
  background: #fff;
}

.detail-info {
  padding: 16px;
  background: #fff;
}

.price-row {
  display: flex;
  align-items: baseline;
  gap: 8px;
  margin-bottom: 8px;
}

.current-price {
  font-size: 24px;
  font-weight: 700;
  color: #FF6B35;
}

.original-price {
  font-size: 14px;
  color: #969799;
  text-decoration: line-through;
}

.condition-tag {
  font-size: 12px;
  padding: 4px 8px;
  border-radius: 4px;
  background: #FFF7E6;
  color: #FF6B35;
}

.condition-new {
  background: #E8F8E8;
  color: #07C160;
}

.condition-95 {
  background: #E8F4FF;
  color: #2196F3;
}

.condition-9 {
  background: #FFF7E6;
  color: #FF9800;
}

.condition-8 {
  background: #FFF0F0;
  color: #F44336;
}

.condition-old {
  background: #F5F5F5;
  color: #9E9E9E;
}

.title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 6px;
}

.author {
  margin-bottom: 8px;
}

.meta {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.detail-desc {
  margin-top: 8px;
  padding: 16px;
  background: #fff;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 12px;
}

.desc-content {
  font-size: 14px;
  line-height: 1.8;
  color: #646566;
  white-space: pre-wrap;
}

.seller-info {
  margin-top: 8px;
  padding: 16px;
  background: #fff;
}

.seller-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.seller-card {
  display: flex;
  align-items: center;
  gap: 12px;
}

.seller-detail {
  flex: 1;
}

.seller-name {
  font-size: 15px;
  font-weight: 500;
  margin-bottom: 4px;
}

.action-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 16px;
  padding-bottom: calc(8px + env(safe-area-inset-bottom));
  background: #fff;
  border-top: 1px solid #ebedf0;
  z-index: 100;
}

.action-icons {
  display: flex;
  gap: 20px;
}

.action-icon-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
}

.icon-text {
  font-size: 10px;
  color: #969799;
}

.action-buttons {
  display: flex;
  gap: 10px;
}

.action-buttons .van-button {
  min-width: 100px;
}
</style>
