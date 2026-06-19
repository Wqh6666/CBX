<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { Image as VanImage } from 'vant'
import { formatPrice, getConditionName } from '@/utils/format'
import type { Book } from '@/types'

const props = defineProps<{
  book: Book
  showSeller?: boolean
}>()

const router = useRouter()

const onClick = () => {
  router.push(`/book/${props.book.id}`)
}

const conditionClass = computed(() => {
  const map: Record<number, string> = {
    1: 'condition-new',
    2: 'condition-95',
    3: 'condition-9',
    4: 'condition-8',
    5: 'condition-old'
  }
  return map[props.book.condition] || ''
})
</script>

<template>
  <div class="book-card" @click="onClick">
    <div class="cover">
      <van-image
        :src="book.coverImage"
        fit="cover"
        width="100%"
        height="100%"
        radius="8"
      />
    </div>
    <div class="info">
      <div class="title">{{ book.title }}</div>
      <div class="author text-secondary text-small">{{ book.author }}</div>
      <div class="meta">
        <span class="condition" :class="conditionClass">{{ getConditionName(book.condition) }}</span>
        <span v-if="book.categoryName" class="category text-secondary text-small">{{ book.categoryName }}</span>
      </div>
      <div class="bottom">
        <span class="price">{{ formatPrice(book.price) }}</span>
        <span v-if="book.originalPrice" class="original-price text-secondary text-small">
          {{ formatPrice(book.originalPrice) }}
        </span>
      </div>
      <div v-if="showSeller" class="seller">
        <van-image :src="book.sellerAvatar" round width="20px" height="20px" />
        <span class="seller-name text-secondary text-small">{{ book.sellerName }}</span>
      </div>
    </div>
  </div>
</template>

<style scoped>
.book-card {
  display: flex;
  background: #fff;
  border-radius: 8px;
  padding: 12px;
  gap: 12px;
  cursor: pointer;
  transition: transform 0.2s;
}

.book-card:active {
  transform: scale(0.98);
}

.cover {
  width: 80px;
  height: 100px;
  flex-shrink: 0;
  background: #f5f5f5;
  border-radius: 8px;
  overflow: hidden;
}

.info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  min-width: 0;
}

.title {
  font-size: 14px;
  font-weight: 600;
  color: #323233;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.author {
  margin-top: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.meta {
  display: flex;
  gap: 8px;
  margin-top: 6px;
}

.condition {
  font-size: 10px;
  padding: 2px 6px;
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

.category {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.bottom {
  display: flex;
  align-items: baseline;
  gap: 8px;
  margin-top: 6px;
}

.price {
  font-size: 16px;
  font-weight: 600;
  color: #FF6B35;
}

.original-price {
  text-decoration: line-through;
}

.seller {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-top: 6px;
}

.seller-name {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
