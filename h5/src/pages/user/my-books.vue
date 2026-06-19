<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Tabs, Tab, Button, showToast } from 'vant'
import MainLayout from '@/layouts/MainLayout.vue'
import BookCard from '@/components/BookCard.vue'
import EmptyState from '@/components/EmptyState.vue'
import type { Book } from '@/types'

const router = useRouter()

const activeTab = ref(0)
const books = ref<Book[]>([])
const loading = ref(false)

const statusList = ['全部', '上架中', '已下架', '草稿']

const mockBooks: Book[] = [
  {
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
    sellerId: 1,
    sellerName: '我',
    sellerAvatar: '',
    viewCount: 128,
    collectCount: 12,
    createTime: '2024-01-15 10:30:00'
  },
  {
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
    status: 0,
    sellerId: 1,
    sellerName: '我',
    sellerAvatar: '',
    viewCount: 0,
    collectCount: 0,
    createTime: '2024-01-14 15:20:00'
  }
]

const filteredBooks = () => {
  if (activeTab.value === 0) {
    return books.value
  }
  const statusMap: Record<number, number> = { 1: 1, 2: 2, 3: 0 }
  return books.value.filter(b => b.status === statusMap[activeTab.value])
}

const onTabChange = (index: number) => {
  activeTab.value = index
}

const onPublish = () => {
  router.push('/book/publish')
}

const onEdit = (book: Book) => {
  router.push(`/book/edit/${book.id}`)
}

onMounted(() => {
  books.value = mockBooks
})
</script>

<template>
  <MainLayout>
    <div class="page-container">
      <van-nav-bar
        title="我的发布"
        left-text="返回"
        left-arrow
        @click-left="router.back()"
      />

      <van-tabs v-model:active="activeTab" sticky @change="onTabChange">
        <van-tab v-for="status in statusList" :key="status" :title="status" />
      </van-tabs>

      <div class="book-list">
        <div v-if="filteredBooks().length === 0" class="empty-wrap">
          <EmptyState message="暂无发布" />
        </div>
        <BookCard
          v-for="book in filteredBooks()"
          :key="book.id"
          :book="book"
        />
      </div>

      <div class="publish-btn">
        <van-button type="primary" round block @click="onPublish">
          <van-icon name="plus" /> 发布新书
        </van-button>
      </div>
    </div>
  </MainLayout>
</template>

<style scoped>
.book-list {
  padding: 12px;
  padding-bottom: 70px;
}

.empty-wrap {
  padding: 60px 0;
}

.publish-btn {
  position: fixed;
  bottom: 60px;
  left: 12px;
  right: 12px;
}

.publish-btn .van-button {
  background: linear-gradient(135deg, #FF6B35, #FF8F65);
  border: none;
}
</style>
