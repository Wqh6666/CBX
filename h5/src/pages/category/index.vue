<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Sticky } from 'vant'
import MainLayout from '@/layouts/MainLayout.vue'
import BookCard from '@/components/BookCard.vue'
import EmptyState from '@/components/EmptyState.vue'
import type { Category, Book } from '@/types'

const route = useRoute()
const router = useRouter()

const categories = ref<Category[]>([
  { id: 1, name: '全部', parentId: 0, level: 1, sort: 0 },
  { id: 2, name: '教材', parentId: 0, level: 1, sort: 1 },
  { id: 3, name: '小说', parentId: 0, level: 1, sort: 2 },
  { id: 4, name: '专业书', parentId: 0, level: 1, sort: 3 },
  { id: 5, name: '杂志', parentId: 0, level: 1, sort: 4 },
  { id: 6, name: '工具书', parentId: 0, level: 1, sort: 5 },
  { id: 7, name: '其他', parentId: 0, level: 1, sort: 6 }
])

const subCategories = ref<Category[]>([
  { id: 11, name: '计算机', parentId: 4, level: 2, sort: 1 },
  { id: 12, name: '数学', parentId: 4, level: 2, sort: 2 },
  { id: 13, name: '物理', parentId: 4, level: 2, sort: 3 },
  { id: 14, name: '化学', parentId: 4, level: 2, sort: 4 },
  { id: 15, name: '英语', parentId: 2, level: 2, sort: 5 }
])

const selectedCategoryId = ref(0)
const selectedSubCategoryId = ref<number | null>(null)
const books = ref<Book[]>([])
const loading = ref(false)

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
    categoryId: 4,
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
  },
  {
    id: 3,
    title: '线性代数第六版',
    author: '同济大学数学系',
    isbn: '9787040396638',
    publisher: '高等教育出版社',
    categoryId: 2,
    categoryName: '教材',
    coverImage: 'https://picsum.photos/160/200?random=14',
    images: [],
    description: '线性代数经典教材',
    originalPrice: 35,
    price: 18,
    condition: 4,
    conditionName: '8成新',
    status: 1,
    sellerId: 5,
    sellerName: '数学爱好者',
    sellerAvatar: 'https://picsum.photos/40/40?random=104',
    viewCount: 76,
    collectCount: 5,
    createTime: '2024-01-13 14:00:00'
  }
]

const onCategorySelect = (categoryId: number) => {
  selectedCategoryId.value = categoryId
  selectedSubCategoryId.value = null
  loadBooks()
}

const onSubCategorySelect = (subCategoryId: number | null) => {
  selectedSubCategoryId.value = selectedSubCategoryId.value === subCategoryId ? null : subCategoryId
  loadBooks()
}

const loadBooks = () => {
  let filtered = [...mockBooks]
  if (selectedCategoryId.value > 0) {
    filtered = filtered.filter(b => b.categoryId === selectedCategoryId.value)
  }
  if (selectedSubCategoryId.value) {
    filtered = filtered.filter(b => b.categoryId === selectedSubCategoryId.value)
  }
  books.value = filtered
}

onMounted(() => {
  if (route.query.id) {
    selectedCategoryId.value = parseInt(route.query.id as string)
  }
  loadBooks()
})
</script>

<template>
  <MainLayout>
    <div class="page-container">
      <!-- 分类标签 -->
      <div class="category-tabs">
        <div class="tab-scroll">
          <div
            v-for="cat in categories"
            :key="cat.id"
            class="tab-item"
            :class="{ active: selectedCategoryId === cat.id }"
            @click="onCategorySelect(cat.id)"
          >
            {{ cat.name }}
          </div>
        </div>
      </div>

      <!-- 子分类 -->
      <div v-if="selectedCategoryId === 4" class="sub-category">
        <div class="sub-scroll">
          <div
            v-for="sub in subCategories"
            :key="sub.id"
            class="sub-item"
            :class="{ active: selectedSubCategoryId === sub.id }"
            @click="onSubCategorySelect(sub.id)"
          >
            {{ sub.name }}
          </div>
        </div>
      </div>

      <!-- 排序和筛选 -->
      <div class="filter-bar">
        <div class="filter-item active">综合</div>
        <div class="filter-item">最新</div>
        <div class="filter-item">价格</div>
        <div class="filter-item">销量</div>
      </div>

      <!-- 书籍列表 -->
      <div class="book-grid">
        <div v-if="books.length === 0" class="empty-wrap">
          <EmptyState message="暂无相关书籍" />
        </div>
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
.category-tabs {
  background: #fff;
  padding: 12px 0;
  position: sticky;
  top: 0;
  z-index: 10;
}

.tab-scroll {
  display: flex;
  gap: 20px;
  padding: 0 12px;
  overflow-x: auto;
  -webkit-overflow-scrolling: touch;
}

.tab-item {
  font-size: 14px;
  color: #969799;
  white-space: nowrap;
  padding-bottom: 8px;
  border-bottom: 2px solid transparent;
  transition: all 0.2s;
}

.tab-item.active {
  color: #FF6B35;
  border-bottom-color: #FF6B35;
  font-weight: 600;
}

.sub-category {
  background: #fff;
  padding: 0 12px 12px;
}

.sub-scroll {
  display: flex;
  gap: 12px;
  overflow-x: auto;
  -webkit-overflow-scrolling: touch;
}

.sub-item {
  font-size: 12px;
  padding: 6px 12px;
  background: #f5f5f5;
  border-radius: 16px;
  color: #646566;
  white-space: nowrap;
  transition: all 0.2s;
}

.sub-item.active {
  background: #FF6B35;
  color: #fff;
}

.filter-bar {
  display: flex;
  gap: 24px;
  padding: 12px;
  background: #fff;
  margin-top: 8px;
}

.filter-item {
  font-size: 13px;
  color: #969799;
}

.filter-item.active {
  color: #FF6B35;
  font-weight: 600;
}

.book-grid {
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding: 12px;
}

.empty-wrap {
  padding: 60px 0;
}
</style>
