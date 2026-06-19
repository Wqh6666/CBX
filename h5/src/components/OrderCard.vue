<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { Image as VanImage } from 'vant'
import { formatPrice, formatDateTime, getOrderStatusName, getOrderStatusColor } from '@/utils/format'
import type { Order } from '@/types'

const props = defineProps<{
  order: Order
}>()

const router = useRouter()

const statusColor = computed(() => getOrderStatusColor(props.order.status))

const onClick = () => {
  router.push(`/order/detail/${props.order.id}`)
}
</script>

<template>
  <div class="order-card" @click="onClick">
    <div class="header">
      <span class="order-no">订单号: {{ order.orderNo }}</span>
      <span class="status" :style="{ color: statusColor }">{{ getOrderStatusName(order.status) }}</span>
    </div>
    <div class="book-info">
      <van-image :src="order.book.coverImage" fit="cover" width="60px" height="80px" radius="4" />
      <div class="book-detail">
        <div class="title">{{ order.book.title }}</div>
        <div class="author text-secondary text-small">{{ order.book.author }}</div>
        <div class="price-row">
          <span class="price">{{ formatPrice(order.amount) }}</span>
          <span class="quantity text-secondary text-small">x1</span>
        </div>
      </div>
    </div>
    <div class="footer">
      <span class="create-time text-secondary text-small">{{ formatDateTime(order.createTime) }}</span>
      <slot name="actions" />
    </div>
  </div>
</template>

<style scoped>
.order-card {
  background: #fff;
  border-radius: 8px;
  padding: 12px;
  cursor: pointer;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 10px;
  border-bottom: 1px solid #ebedf0;
}

.order-no {
  font-size: 12px;
  color: #969799;
}

.status {
  font-size: 14px;
  font-weight: 600;
}

.book-info {
  display: flex;
  gap: 12px;
  padding: 12px 0;
}

.book-detail {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  min-width: 0;
}

.title {
  font-size: 14px;
  font-weight: 500;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.author {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.price-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price {
  font-size: 16px;
  font-weight: 600;
  color: #FF6B35;
}

.footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 10px;
  border-top: 1px solid #ebedf0;
}
</style>
