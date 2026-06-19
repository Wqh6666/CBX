<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { Checkbox, Button, showToast } from 'vant'
import { Image as VanImage } from 'vant'
import MainLayout from '@/layouts/MainLayout.vue'
import EmptyState from '@/components/EmptyState.vue'
import { formatPrice } from '@/utils/format'
import { useCartStore } from '@/stores/cart'
import type { CartItem } from '@/types'

const router = useRouter()
const cartStore = useCartStore()

const selectedIds = ref<number[]>([])

const allSelected = computed({
  get: () => selectedIds.value.length === cartStore.items.length && cartStore.items.length > 0,
  set: (val) => {
    if (val) {
      selectedIds.value = cartStore.items.map(i => i.id)
    } else {
      selectedIds.value = []
    }
  }
})

const totalPrice = computed(() => {
  const selected = cartStore.items.filter(i => selectedIds.value.includes(i.id))
  return selected.reduce((sum, item) => sum + item.book.price * item.quantity, 0)
})

const onToggleItem = (id: number) => {
  const index = selectedIds.value.indexOf(id)
  if (index > -1) {
    selectedIds.value.splice(index, 1)
  } else {
    selectedIds.value.push(id)
  }
}

const onRemoveItem = (id: number) => {
  cartStore.removeItem(id)
  selectedIds.value = selectedIds.value.filter(i => i !== id)
  showToast('已删除')
}

const onSettle = () => {
  if (selectedIds.value.length === 0) {
    showToast('请选择商品')
    return
  }
  router.push('/order/confirm')
}

// 初始化mock数据
const initMockData = () => {
  if (cartStore.items.length === 0) {
    cartStore.updateCart([
      {
        id: 1,
        userId: 1,
        bookId: 1,
        quantity: 1,
        createTime: '2024-01-15 10:00:00',
        book: {
          id: 1,
          title: '高等数学第七版上册',
          author: '同济大学数学系',
          isbn: '9787040396614',
          publisher: '高等教育出版社',
          categoryId: 1,
          categoryName: '教材',
          coverImage: 'https://picsum.photos/160/200?random=10',
          images: [],
          description: '',
          originalPrice: 45,
          price: 25,
          condition: 3,
          conditionName: '9成新',
          status: 1,
          sellerId: 2,
          sellerName: '小明同学',
          sellerAvatar: '',
          viewCount: 128,
          collectCount: 12,
          createTime: '2024-01-15 10:30:00'
        }
      },
      {
        id: 2,
        userId: 1,
        bookId: 2,
        quantity: 1,
        createTime: '2024-01-15 11:00:00',
        book: {
          id: 2,
          title: '算法导论第四版',
          author: 'Thomas H. Cormen',
          isbn: '9787111407010',
          publisher: '机械工业出版社',
          categoryId: 3,
          categoryName: '专业书',
          coverImage: 'https://picsum.photos/160/200?random=11',
          images: [],
          description: '',
          originalPrice: 128,
          price: 65,
          condition: 2,
          conditionName: '95新',
          status: 1,
          sellerId: 3,
          sellerName: '学霸小李',
          sellerAvatar: '',
          viewCount: 256,
          collectCount: 34,
          createTime: '2024-01-14 15:20:00'
        }
      }
    ])
  }
}

initMockData()
</script>

<template>
  <MainLayout>
    <div class="page-container">
      <van-nav-bar title="购物车" />

      <div v-if="cartStore.items.length === 0" class="empty-wrap">
        <EmptyState message="购物车是空的" image="https://picsum.photos/200/200?random=50" />
        <div class="empty-action">
          <van-button size="small" round type="primary" @click="router.push('/')">
            去逛逛
          </van-button>
        </div>
      </div>

      <div v-else class="cart-content">
        <div class="cart-list">
          <div v-for="item in cartStore.items" :key="item.id" class="cart-item">
            <div class="item-checkbox">
              <van-checkbox
                :model-value="selectedIds.includes(item.id)"
                @click="onToggleItem(item.id)"
              />
            </div>
            <div class="item-cover">
              <van-image :src="item.book.coverImage" fit="cover" width="70" height="90" radius="4" />
            </div>
            <div class="item-info">
              <div class="item-title">{{ item.book.title }}</div>
              <div class="item-author text-secondary text-small">{{ item.book.author }}</div>
              <div class="item-bottom">
                <span class="item-price">{{ formatPrice(item.book.price) }}</span>
                <van-button size="mini" plain hairline @click="onRemoveItem(item.id)">删除</van-button>
              </div>
            </div>
          </div>
        </div>

        <!-- 底部结算栏 -->
        <div class="settle-bar">
          <div class="settle-left">
            <van-checkbox v-model="allSelected">全选</van-checkbox>
          </div>
          <div class="settle-right">
            <div class="total-price">
              合计: <span class="price">{{ formatPrice(totalPrice) }}</span>
            </div>
            <van-button size="small" round type="primary" @click="onSettle">
              结算 ({{ selectedIds.length }})
            </van-button>
          </div>
        </div>
      </div>
    </div>
  </MainLayout>
</template>

<style scoped>
.empty-wrap {
  padding: 60px 0;
}

.empty-action {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.cart-content {
  padding-bottom: 60px;
}

.cart-list {
  padding: 12px;
}

.cart-item {
  display: flex;
  align-items: center;
  gap: 12px;
  background: #fff;
  border-radius: 8px;
  padding: 12px;
  margin-bottom: 12px;
}

.item-checkbox {
  flex-shrink: 0;
}

.item-cover {
  flex-shrink: 0;
}

.item-info {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
}

.item-title {
  font-size: 14px;
  font-weight: 500;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.item-author {
  margin-top: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.item-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 8px;
}

.item-price {
  font-size: 16px;
  font-weight: 600;
  color: #FF6B35;
}

.settle-bar {
  position: fixed;
  bottom: 50px;
  left: 0;
  right: 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background: #fff;
  border-top: 1px solid #ebedf0;
  padding-bottom: calc(12px + env(safe-area-inset-bottom));
}

.settle-left {
  display: flex;
  align-items: center;
}

.settle-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.total-price {
  font-size: 14px;
}

.total-price .price {
  font-size: 18px;
  font-weight: 600;
  color: #FF6B35;
}
</style>
