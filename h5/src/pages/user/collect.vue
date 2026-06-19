<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import MainLayout from '@/layouts/MainLayout.vue'
import BookCard from '@/components/BookCard.vue'
import EmptyState from '@/components/EmptyState.vue'
import type { Book } from '@/types'

const router = useRouter()

const books = ref<Book[]>([])
const loading = ref(false)

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
    sellerId: 2,
    sellerName: '小明同学',
    sellerAvatar: 'https://picsum.photos/40/40?random=100',
    viewCount: 128,
    collectCount: 12,
    createTime: '2024-01-15 10:30:00'
  },
  {
    id: 3,
    title: '平凡的世界',
    author: '路遥',
    isbn: '9787530216780',
    publisher: '北京十月文艺出版社',
    categoryId: 2,
    categoryName: '小说',
    coverImage: 'https://picsum.photos/160/200?random=12',
    images: [],
    description: '',
    originalPrice: 68,
    price: 30,
    condition: 4,
    conditionName: '8成新',
    status: 1,
    sellerId: 3,
    sellerName: '文艺青年小王',
    sellerAvatar: 'https://picsum.photos/40/40?random=102',
    viewCount: 89,
    collectCount: 8,
    createTime: '2024-01-13 09:15:00'
  }
]

const onRemove = (book: Book) => {
  books.value = books.value.filter(b => b.id !== book.id)
  showToast('已取消收藏')
}

onMounted(() => {
  books.value = mockBooks
})
</script>

<template>
  <MainLayout>
    <div class="page-container">
      <van-nav-bar
        title="我的收藏"
        left-text="返回"
        left-arrow
        @click-left="router.back()"
      />

      <div v-if="books.length === 0" class="empty-wrap">
        <EmptyState message="暂无收藏" />
      </div>

      <div v-else class="book-list">
        <BookCard
          v-for="book in books"
          :key="book.id"
          :book="book"
          show-seller
        />
      </div>
    </div>
  </MainLayout>
</template>

<style scoped>
.book-list {
  padding: 12px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.empty-wrap {
  padding: 60px 0;
}
</style>
