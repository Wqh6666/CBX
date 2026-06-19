<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { showToast } from 'vant'
import { formatPrice } from '@/utils/format'
import type { Order } from '@/types'

const router = useRouter()
const route = useRoute()

const orderId = parseInt(route.params.id as string)
const order = ref<Order | null>(null)
const paying = ref(false)

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
    sellerAvatar: '',
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

const onPay = async () => {
  paying.value = true
  try {
    // 模拟支付
    await new Promise(resolve => setTimeout(resolve, 2000))
    showToast('支付成功')
    setTimeout(() => {
      router.replace('/order/list')
    }, 1000)
  } catch (e) {
    showToast('支付失败')
  } finally {
    paying.value = false
  }
}

onMounted(() => {
  order.value = mockOrder
  order.value.id = orderId
})
</script>

<template>
  <div class="page-container">
    <van-nav-bar title="订单支付" left-text="返回" left-arrow @click-left="router.back()" />

    <div v-if="order" class="pay-content">
      <!-- 金额 -->
      <div class="amount-section">
        <div class="amount-label">支付金额</div>
        <div class="amount-value">{{ formatPrice(order.amount) }}</div>
      </div>

      <!-- 订单信息 -->
      <div class="order-info">
        <div class="info-row">
          <span class="label">订单编号</span>
          <span class="value">{{ order.orderNo }}</span>
        </div>
        <div class="info-row">
          <span class="label">商品</span>
          <span class="value">{{ order.book.title }}</span>
        </div>
        <div class="info-row">
          <span class="label">卖家</span>
          <span class="value">{{ order.book.sellerName }}</span>
        </div>
      </div>

      <!-- 支付方式 -->
      <div class="pay-methods">
        <div class="method-title">选择支付方式</div>
        <div class="method-item active">
          <van-icon name="wechat" size="24" color="#07C160" />
          <span>微信支付</span>
          <van-icon name="success" size="18" color="#07C160" class="check-icon" />
        </div>
        <div class="method-item">
          <van-icon name="alipay" size="24" color="#1677FF" />
          <span>支付宝</span>
          <van-icon name="success" size="18" color="#DCDEE0" class="check-icon" />
        </div>
      </div>
    </div>

    <!-- 底部支付按钮 -->
    <div class="pay-bar">
      <van-button
        type="primary"
        round
        block
        :loading="paying"
        @click="onPay"
      >
        确认支付 {{ order ? formatPrice(order.amount) : '' }}
      </van-button>
    </div>
  </div>
</template>

<style scoped>
.pay-content {
  padding: 12px;
}

.amount-section {
  background: linear-gradient(135deg, #FF6B35, #FF8F65);
  color: #fff;
  padding: 30px 20px;
  border-radius: 12px;
  text-align: center;
  margin-bottom: 16px;
}

.amount-label {
  font-size: 14px;
  opacity: 0.9;
  margin-bottom: 8px;
}

.amount-value {
  font-size: 36px;
  font-weight: 700;
}

.order-info {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 16px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
}

.info-row .label {
  color: #969799;
  font-size: 14px;
}

.info-row .value {
  font-size: 14px;
  color: #323233;
}

.pay-methods {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
}

.method-title {
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 12px;
}

.method-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 0;
  border-bottom: 1px solid #ebedf0;
}

.method-item:last-child {
  border-bottom: none;
}

.method-item span {
  flex: 1;
  font-size: 14px;
}

.check-icon {
  flex-shrink: 0;
}

.pay-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 12px 16px;
  padding-bottom: calc(12px + env(safe-area-inset-bottom));
  background: #fff;
  border-top: 1px solid #ebedf0;
}
</style>
