<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { Search, Tag } from 'vant'
import EmptyState from '@/components/EmptyState.vue'
import BookCard from '@/components/BookCard.vue'
import type { Book } from '@/types'

const router = useRouter()
const searchValue = ref('')
const searchHistory = ref<string[]>(['算法导论', '高等数学', 'Python'])
const hotSearch = ref(['高等数学', '线性代数', '大学英语', '计算机网络', '数据结构'])
const books = ref<Book[]>([])
const searching = ref(false)
const showResult = ref(false)

const mockBooks: Book[] = [
  {
    id: 1,
    title: '高等数学第七版上册',
    author: '同济大学数学系',
    isbn: '9787040396614',
    publisher: '高等教育出版社',
    categoryId: 2,
    categoryName: '教材',
    coverImage: 'https://picsum.photos/160/200?random=10',
    images: [],
    description: '经典高数教材，适合大一学生使用',
    originalPrice: 45,
    price: 25,
    condition: 3,
    conditionName: '9成新',
    status: 1,
    sellerId: 1,
    sellerName: '小明同学',
    sellerAvatar: 'https://picsum.photos/40/40?random=100',
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
    description: '计算机专业经典算法教材',
    originalPrice: 128,
    price: 65,
    condition: 2,
    conditionName: '95新',
    status: 1,
    sellerId: 2,
    sellerName: '学霸小李',
    sellerAvatar: 'https://picsum.photos/40/40?random=101',
    viewCount: 256,
    collectCount: 34,
    createTime: '2024-01-14 15:20:00'
  }
]

const onSearch = () => {
  if (!searchValue.value.trim()) return

  if (!searchHistory.value.includes(searchValue.value)) {
    searchHistory.value.unshift(searchValue.value)
  }

  searching.value = true
  showResult.value = true

  const keyword = searchValue.value.toLowerCase()
  books.value = mockBooks.filter(b =>
    b.title.toLowerCase().includes(keyword) ||
    b.author.toLowerCase().includes(keyword) ||
    b.isbn.includes(keyword)
  )
  searching.value = false
}

const onHistoryClick = (keyword: string) => {
  searchValue.value = keyword
  onSearch()
}

const onHotClick = (keyword: string) => {
  searchValue.value = keyword
  onSearch()
}

const onClearHistory = () => {
  searchHistory.value = []
}

const onBookClick = (book: Book) => {
  router.push(`/book/${book.id}`)
}
</script>

<template>
  <div class="page-container">
    <!-- 搜索框 -->
    <div class="search-header">
      <van-search
        v-model="searchValue"
        placeholder="搜索书名/作者/ISBN"
        show-action
        @search="onSearch"
        @cancel="router.back()"
      />
    </div>

    <!-- 搜索结果 -->
    <div v-if="showResult" class="search-result">
      <div v-if="books.length === 0" class="empty-wrap">
        <EmptyState message="未找到相关书籍" />
      </div>
      <div v-else class="result-list">
        <BookCard
          v-for="book in books"
          :key="book.id"
          :book="book"
          show-seller
          @click="onBookClick(book)"
        />
      </div>
    </div>

    <!-- 搜索历史和热门 -->
    <div v-else class="search-content">
      <!-- 搜索历史 -->
      <div v-if="searchHistory.length > 0" class="section">
        <div class="section-header">
          <span class="section-title">搜索历史</span>
          <van-icon name="delete-o" @click="onClearHistory" />
        </div>
        <div class="tag-list">
          <van-tag
            v-for="(keyword, index) in searchHistory"
            :key="index"
            plain
            size="medium"
            @click="onHistoryClick(keyword)"
          >
            {{ keyword }}
          </van-tag>
        </div>
      </div>

      <!-- 热门搜索 -->
      <div class="section">
        <div class="section-header">
          <span class="section-title">热门搜索</span>
        </div>
        <div class="tag-list">
          <van-tag
            v-for="(keyword, index) in hotSearch"
            :key="index"
            plain
            size="medium"
            type="primary"
            @click="onHotClick(keyword)"
          >
            {{ keyword }}
          </van-tag>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.search-header {
  background: #fff;
  position: sticky;
  top: 0;
  z-index: 100;
}

.search-content {
  padding: 16px 12px;
}

.section {
  margin-bottom: 24px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.section-title {
  font-size: 14px;
  font-weight: 600;
  color: #323233;
}

.tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.tag-list :deep(.van-tag) {
  padding: 6px 12px;
}

.search-result {
  padding: 12px;
}

.result-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.empty-wrap {
  padding: 60px 0;
}
</style>
