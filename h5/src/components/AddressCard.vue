<script setup lang="ts">
import { useRouter } from 'vue-router'
import { Image as VanImage } from 'vant'
import type { Address } from '@/types'

const props = defineProps<{
  address: Address
  editable?: boolean
}>()

const emit = defineEmits<{
  edit: [address: Address]
  delete: [id: number]
}>()

const router = useRouter()

const onEdit = () => {
  emit('edit', props.address)
}

const onDelete = () => {
  emit('delete', props.address.id)
}

const fullAddress = `${props.address.province}${props.address.city}${props.address.district}${props.address.detail}`
</script>

<template>
  <div class="address-card">
    <div class="info" @click="onEdit">
      <div class="header">
        <span class="name">{{ address.name }}</span>
        <span class="phone">{{ address.phone }}</span>
        <span v-if="address.isDefault" class="default-tag">默认</span>
      </div>
      <div class="address-detail text-secondary text-small">{{ fullAddress }}</div>
    </div>
    <div class="actions">
      <van-icon name="edit" size="18" @click="onEdit" />
      <van-icon name="delete-o" size="18" @click="onDelete" />
    </div>
  </div>
</template>

<style scoped>
.address-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  border-radius: 8px;
  padding: 14px 12px;
  gap: 12px;
}

.info {
  flex: 1;
  min-width: 0;
}

.header {
  display: flex;
  align-items: center;
  gap: 8px;
}

.name {
  font-size: 16px;
  font-weight: 600;
}

.phone {
  font-size: 14px;
  color: #323233;
}

.default-tag {
  font-size: 10px;
  padding: 2px 6px;
  background: #FF6B35;
  color: #fff;
  border-radius: 4px;
}

.address-detail {
  margin-top: 6px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.actions {
  display: flex;
  gap: 16px;
  color: #969799;
}
</style>
