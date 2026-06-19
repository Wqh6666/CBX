<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Tabs, Tab, Button } from 'vant'
import MainLayout from '@/layouts/MainLayout.vue'
import OrderCard from '@/components/OrderCard.vue'
import EmptyState from '@/components/EmptyState.vue'
import type { Order } from '@/types'

const router = useRouter()

const activeTab = ref(0)
const orders = ref<Order[]>([])
const loading = ref(false)

const statusList = ['全部', '待付款', '待发货', '待收货', '已完成', '已取消']

const mockOrders: Order[] = [
  {
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
  },
  {
    id: 2,
    orderNo: 'ORD202401140002',
    bookId: 2,
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
    },
    buyerId: 1,
    sellerId: 3,
    amount: 65,
    status: 2,
    statusDesc: '待收货',
    payTime: '2024-01-14 16:00:00',
    payMethod: 'wechat',
    expressNo: 'SF1234567890',
    expressCompany: '顺丰速运',
    shipTime: '2024-01-14 18:00:00',
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
    createTime: '2024-01-14 15:20:00'
  }
]

const filteredOrders = () => {
  if (activeTab.value === 0) {
    return orders.value
  }
  return orders.value.filter(o => o.status === activeTab.value - 1)
}

const onTabChange = (index: number) => {
  activeTab.value = index
}

onMounted(() => {
  orders.value = mockOrders
})
</script>

<template>
  <MainLayout>
    <div class="page-container">
      <van-nav-bar title="我的订单" />

      <van-tabs v-model:active="activeTab" sticky @change="onTabChange">
        <van-tab v-for="status in statusList" :key="status" :title="status" />
      </van-tabs>

      <div class="order-list">
        <div v-if="filteredOrders().length === 0" class="empty-wrap">
          <EmptyState message="暂无订单" />
        </div>
        <OrderCard
          v-for="order in filteredOrders()"
          :key="order.id"
          :order="order"
        >
          <template #actions>
            <div class="order-actions">
              <van-button
                v-if="order.status === 0"
                size="small"
                round
                @click.stop="router.push(`/order/pay/${order.id}`)"
              >
                去支付
              </van-button>
              <van-button
                v-if="order.status === 2"
                size="small"
                round
                type="primary"
              >
                确认收货
              </van-button>
            </div>
          </template>
        </OrderCard>
      </div>
    </div>
  </MainLayout>
</template>

<style scoped>
.order-list {
  padding: 12px;
}

.empty-wrap {
  padding: 60px 0;
}

.order-actions {
  display: flex;
  gap: 8px;
}
</style>
