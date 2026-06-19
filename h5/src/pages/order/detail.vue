<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Button, showToast, showConfirmDialog } from 'vant'
import { Image as VanImage } from 'vant'
import { formatPrice, formatDateTime, getOrderStatusName, getOrderStatusColor } from '@/utils/format'
import type { Order } from '@/types'

const router = useRouter()
const route = useRoute()

const orderId = parseInt(route.params.id as string)
const order = ref<Order | null>(null)
const loading = ref(false)

const mockOrder: Order = {
  id: 1,
  orderNo: 'ORD202401150001',
  bookId: 1,
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
    sellerAvatar: 'https://picsum.photos/40/40?random=100',
    viewCount: 128,
    collectCount: 12,
    createTime: '2024-01-15 10:30:00'
  },
  buyerId: 1,
  sellerId: 2,
  amount: 25,
  status: 0,
  statusDesc: '待付款',
  payTime: '',
  payMethod: '',
  expressNo: '',
  expressCompany: '',
  shipTime: '',
  receiveTime: '',
  cancelTime: '',
  cancelReason: '',
  addressId: 1,
  address: {
    id: 1,
    name: '张三',
    phone: '13800138000',
    province: '上海市',
    city: '上海市',
    district: '杨浦区',
    detail: '复旦大学邯郸路220号',
    isDefault: 1
  },
  createTime: '2024-01-15 10:30:00'
}

const fullAddress = () => {
  if (!order.value?.address) return ''
  const a = order.value.address
  return `${a.province}${a.city}${a.district}${a.detail}`
}

const onCancelOrder = async () => {
  try {
    await showConfirmDialog({
      title: '确认取消',
      message: '确定要取消该订单吗？'
    })
    showToast('订单已取消')
    router.back()
  } catch (e) {
    // 用户取消
  }
}

const onPay = () => {
  router.push(`/order/pay/${orderId}`)
}

const onReceive = async () => {
  try {
    await showConfirmDialog({
      title: '确认收货',
      message: '确认已收到商品？'
    })
    showToast('已确认收货')
    router.back()
  } catch (e) {
    // 用户取消
  }
}

onMounted(() => {
  order.value = mockOrder
  order.value.id = orderId
})
</script>

<template>
  <div class="page-container">
    <van-nav-bar title="订单详情" left-text="返回" left-arrow @click-left="router.back()" />

    <div v-if="order" class="detail-content">
      <!-- 订单状态 -->
      <div class="status-section" :style="{ background: getOrderStatusColor(order.status) }">
        <van-icon name="clock" size="24" />
        <span class="status-text">{{ getOrderStatusName(order.status) }}</span>
      </div>

      <!-- 收货地址 -->
      <div class="address-section">
        <van-icon name="location-o" size="20" class="address-icon" />
        <div class="address-info">
          <div class="address-header">
            <span class="name">{{ order.address.name }}</span>
            <span class="phone">{{ order.address.phone }}</span>
          </div>
          <div class="address-detail">{{ fullAddress() }}</div>
        </div>
      </div>

      <!-- 商品信息 -->
      <div class="book-section">
        <div class="book-item">
          <van-image :src="order.book.coverImage" fit="cover" width="70" height="90" radius="4" />
          <div class="book-info">
            <div class="book-title">{{ order.book.title }}</div>
            <div class="book-author text-secondary text-small">{{ order.book.author }}</div>
            <div class="book-meta">
              <span class="condition-tag">{{ order.book.conditionName }}</span>
            </div>
          </div>
          <div class="book-price">
            <span>{{ formatPrice(order.book.price) }}</span>
            <span class="text-secondary text-small">x1</span>
          </div>
        </div>
      </div>

      <!-- 订单信息 -->
      <div class="info-section">
        <div class="info-row">
          <span class="label">订单编号</span>
          <span class="value">{{ order.orderNo }}</span>
        </div>
        <div class="info-row">
          <span class="label">下单时间</span>
          <span class="value">{{ formatDateTime(order.createTime) }}</span>
        </div>
        <div v-if="order.payTime" class="info-row">
          <span class="label">支付时间</span>
          <span class="value">{{ formatDateTime(order.payTime) }}</span>
        </div>
        <div v-if="order.expressNo" class="info-row">
          <span class="label">快递单号</span>
          <span class="value">{{ order.expressNo }}</span>
        </div>
        <div class="info-row">
          <span class="label">配送方式</span>
          <span class="value">快递配送</span>
        </div>
      </div>

      <!-- 金额信息 -->
      <div class="amount-section">
        <div class="info-row">
          <span class="label">商品金额</span>
          <span class="value">{{ formatPrice(order.book.price) }}</span>
        </div>
        <div class="info-row">
          <span class="label">运费</span>
          <span class="value text-success">免运费</span>
        </div>
        <div class="info-row total-row">
          <span class="label">合计</span>
          <span class="total-price">{{ formatPrice(order.amount) }}</span>
        </div>
      </div>
    </div>

    <!-- 底部操作栏 -->
    <div v-if="order" class="action-bar">
      <div class="action-left">
        <template v-if="order.status === 0">
          <van-button size="small" round plain @click="onCancelOrder">取消订单</van-button>
        </template>
      </div>
      <div class="action-right">
        <van-button
          v-if="order.status === 0"
          size="small"
          round
          type="primary"
          @click="onPay"
        >
          去支付
        </van-button>
        <van-button
          v-if="order.status === 2"
          size="small"
          round
          type="primary"
          @click="onReceive"
        >
          确认收货
        </van-button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.detail-content {
  padding-bottom: 60px;
}

.status-section {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 20px 16px;
  color: #fff;
}

.status-text {
  font-size: 16px;
  font-weight: 600;
}

.address-section {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 16px;
  background: #fff;
}

.address-icon {
  color: #FF6B35;
  flex-shrink: 0;
  margin-top: 2px;
}

.address-info {
  flex: 1;
}

.address-header {
  display: flex;
  gap: 10px;
  margin-bottom: 4px;
}

.name {
  font-weight: 600;
}

.address-detail {
  font-size: 13px;
  color: #646566;
}

.book-section {
  margin-top: 8px;
  padding: 16px;
  background: #fff;
}

.book-item {
  display: flex;
  gap: 12px;
}

.book-info {
  flex: 1;
}

.book-title {
  font-size: 14px;
  font-weight: 500;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.book-author {
  margin-top: 4px;
}

.book-meta {
  margin-top: 6px;
}

.condition-tag {
  font-size: 12px;
  padding: 2px 6px;
  background: #FFF7E6;
  color: #FF6B35;
  border-radius: 4px;
}

.book-price {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 4px;
  font-size: 14px;
  font-weight: 500;
}

.info-section,
.amount-section {
  margin-top: 8px;
  padding: 16px;
  background: #fff;
}

.info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
}

.info-row .label {
  color: #969799;
  font-size: 13px;
}

.info-row .value {
  font-size: 13px;
}

.text-success {
  color: #07C160;
}

.total-row {
  border-top: 1px solid #ebedf0;
  margin-top: 8px;
  padding-top: 12px;
}

.total-price {
  font-size: 18px;
  font-weight: 700;
  color: #FF6B35;
}

.action-bar {
  position: fixed;
  bottom: 0;
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

.action-left,
.action-right {
  display: flex;
  gap: 10px;
}
</style>
