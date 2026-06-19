<script setup lang="ts">
defineProps<{
  message?: string
  image?: string
}>()
</script>

<template>
  <div class="empty-state">
    <van-empty v-if="!image" :description="message || '暂无数据'" />
    <div v-else class="empty-with-image">
      <img :src="image" class="empty-img" />
      <p class="empty-text">{{ message || '暂无数据' }}</p>
    </div>
  </div>
</template>

<style scoped>
.empty-state {
  padding: 60px 0;
}

.empty-with-image {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40px 0;
}

.empty-img {
  width: 200px;
  height: 200px;
  opacity: 0.6;
}

.empty-text {
  margin-top: 16px;
  color: #969799;
  font-size: 14px;
}
</style>
