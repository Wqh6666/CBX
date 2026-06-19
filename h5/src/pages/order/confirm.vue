<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { RadioGroup, Radio, Button, showToast } from 'vant'
import { Image as VanImage } from 'vant'
import { formatPrice } from '@/utils/format'
import type { Book, Address } from '@/types'

const router = useRouter()
const route = useRoute()

const bookId = parseInt(route.query.bookId as string) || 1

const book = ref<Book>({
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
})

const address = ref<Address>({
  id: 1,
  name: '张三',
  phone: '13800138000',
  province: '上海市',
  city: '上海市',
  district: '杨浦区',
  detail: '复旦大学邯郸路220号',
  isDefault: 1
})

const quantity = ref(1)
const payMethod = ref('wechat')
const submitting = ref(false)

const totalPrice = () => {
  return book.value.price * quantity.value
}

const fullAddress = () => {
  return `${address.value.province}${address.value.city}${address.value.district}${address.value.detail}`
}

const onSubmitOrder = async () => {
  if (!address.value) {
    showToast('请选择收货地址')
    return
  }
  submitting.value = true
  try {
    await new Promise(resolve => setTimeout(resolve, 1000))
    showToast('订单创建成功')
    router.replace('/order/list')
  } catch (e) {
    showToast('创建订单失败')
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <div class="page-container">
    <van-nav-bar title="确认订单" left-text="返回" left-arrow @click-left="router.back()" />

    <div class="order-content">
      <!-- 收货地址 -->
      <div class="address-section" @click="router.push('/user/address')">
        <van-icon name="location-o" size="24" class="address-icon" />
        <div v-if="address" class="address-info">
          <div class="address-header">
            <span class="name">{{ address.name }}</span>
            <span class="phone">{{ address.phone }}</span>
            <span v-if="address.isDefault" class="default-tag">默认</span>
          </div>
          <div class="address-detail">{{ fullAddress() }}</div>
        </div>
        <div v-else class="address-info">
          <span>请选择收货地址</span>
        </div>
        <van-icon name="arrow" class="arrow-icon" />
      </div>

      <!-- 商品信息 -->
      <div class="book-section">
        <div class="section-title">商品信息</div>
        <div class="book-item">
          <van-image :src="book.coverImage" fit="cover" width="70" height="90" radius="4" />
          <div class="book-info">
            <div class="book-title">{{ book.title }}</div>
            <div class="book-author text-secondary text-small">{{ book.author }}</div>
            <div class="book-meta">
              <span class="condition-tag">{{ book.conditionName }}</span>
            </div>
            <div class="book-price">
              <span class="price">{{ formatPrice(book.price) }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 配送方式 -->
      <div class="section">
        <div class="section-title">配送方式</div>
        <div class="delivery-info">
          <span>快递配送</span>
          <span class="text-secondary">免运费</span>
        </div>
      </div>

      <!-- 支付方式 -->
      <div class="section">
        <div class="section-title">支付方式</div>
        <van-radio-group v-model="payMethod">
          <div class="pay-item">
            <div class="pay-left">
              <van-icon name="wechat" size="20" color="#07C160" />
              <span>微信支付</span>
            </div>
            <van-radio name="wechat" checked-color="#07C160" />
          </div>
          <div class="pay-item">
            <div class="pay-left">
              <van-icon name="alipay" size="20" color="#1677FF" />
              <span>支付宝</span>
            </div>
            <van-radio name="alipay" checked-color="#1677FF" />
          </div>
        </van-radio-group>
      </div>
    </div>

    <!-- 底部提交栏 -->
    <div class="submit-bar">
      <div class="submit-info">
        <span class="label">合计:</span>
        <span class="total-price">{{ formatPrice(totalPrice()) }}</span>
      </div>
      <van-button type="primary" round size="small" :loading="submitting" @click="onSubmitOrder">
        提交订单
      </van-button>
    </div>
  </div>
</template>

<style scoped>
.order-content {
  padding: 12px;
  padding-bottom: 70px;
}

.address-section {
  display: flex;
  align-items: center;
  gap: 12px;
  background: #fff;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 12px;
}

.address-icon {
  color: #FF6B35;
  flex-shrink: 0;
}

.address-info {
  flex: 1;
}

.address-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 4px;
}

.name {
  font-size: 16px;
  font-weight: 600;
}

.phone {
  font-size: 14px;
}

.default-tag {
  font-size: 10px;
  padding: 2px 6px;
  background: #FF6B35;
  color: #fff;
  border-radius: 4px;
}

.address-detail {
  font-size: 13px;
  color: #646566;
}

.arrow-icon {
  color: #969799;
  flex-shrink: 0;
}

.section {
  background: #fff;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 12px;
}

.section-title {
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 12px;
}

.book-item {
  display: flex;
  gap: 12px;
}

.book-info {
  flex: 1;
  display: flex;
  flex-direction: column;
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
  margin-top: auto;
}

.book-price .price {
  font-size: 16px;
  font-weight: 600;
  color: #FF6B35;
}

.delivery-info {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
}

.pay-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
}

.pay-left {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
}

.submit-bar {
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

.submit-info {
  display: flex;
  align-items: baseline;
  gap: 4px;
}

.submit-info .label {
  font-size: 14px;
  color: #646566;
}

.total-price {
  font-size: 20px;
  font-weight: 700;
  color: #FF6B35;
}
</style>
