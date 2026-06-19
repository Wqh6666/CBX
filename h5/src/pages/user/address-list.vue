<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showConfirmDialog, showToast } from 'vant'
import MainLayout from '@/layouts/MainLayout.vue'
import AddressCard from '@/components/AddressCard.vue'
import EmptyState from '@/components/EmptyState.vue'
import type { Address } from '@/types'

const router = useRouter()

const addresses = ref<Address[]>([])
const loading = ref(false)

const mockAddresses: Address[] = [
  {
    id: 1,
    name: '张三',
    phone: '13800138000',
    province: '上海市',
    city: '上海市',
    district: '杨浦区',
    detail: '复旦大学邯郸路220号',
    isDefault: 1
  },
  {
    id: 2,
    name: '李四',
    phone: '13900139000',
    province: '上海市',
    city: '上海市',
    district: '浦东新区',
    detail: '张江高科技园区',
    isDefault: 0
  }
]

const onEdit = (address: Address) => {
  router.push({ path: '/user/address/edit', query: { id: address.id.toString() } })
}

const onDelete = async (id: number) => {
  try {
    await showConfirmDialog({
      title: '确认删除',
      message: '确定要删除该地址吗？'
    })
    addresses.value = addresses.value.filter(a => a.id !== id)
    showToast('删除成功')
  } catch (e) {
    // 用户取消
  }
}

const onAdd = () => {
  router.push('/user/address/edit')
}

onMounted(() => {
  addresses.value = mockAddresses
})
</script>

<template>
  <MainLayout>
    <div class="page-container">
      <van-nav-bar title="收货地址" left-text="返回" left-arrow @click-left="router.back()" />

      <div v-if="addresses.length === 0" class="empty-wrap">
        <EmptyState message="暂无收货地址" />
        <div class="empty-action">
          <van-button size="small" round type="primary" @click="onAdd">添加地址</van-button>
        </div>
      </div>

      <div v-else class="address-list">
        <AddressCard
          v-for="address in addresses"
          :key="address.id"
          :address="address"
          @edit="onEdit"
          @delete="onDelete"
        />

        <div class="add-btn-wrap">
          <van-button icon="plus" type="primary" round block @click="onAdd">
            添加新地址
          </van-button>
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

.address-list {
  padding: 12px;
}

.address-list > :deep(.address-card) {
  margin-bottom: 12px;
}

.add-btn-wrap {
  margin-top: 16px;
  padding: 0 12px;
}

.add-btn-wrap .van-button {
  background: #fff;
  color: #FF6B35;
  border: 1px dashed #FF6B35;
}
</style>
