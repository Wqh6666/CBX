<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Swipe, SwipeItem, Grid, GridItem, showToast } from 'vant'
import MainLayout from '@/layouts/MainLayout.vue'
import BookCard from '@/components/BookCard.vue'
import EmptyState from '@/components/EmptyState.vue'
import type { Book, Category } from '@/types'
import { getBooks, getCategories } from '@/api/book'

const router = useRouter()

const banners = ref([
  { id: 1, image: 'https://picsum.photos/375/150?random=1', url: '' },
  { id: 2, image: 'https://picsum.photos/375/150?random=2', url: '' },
  { id: 3, image: 'https://picsum.photos/375/150?random=3', url: '' }
])

const categories = ref<Category[]>([])
const books = ref<Book[]>([])
const loading = ref(false)
const refreshing = ref(false)

const onRefresh = async () => {
  try {
    const [categoriesRes, booksRes] = await Promise.all([
      getCategories(),
      getBooks({ page: 1, size: 10 })
    ])
    categories.value = (categoriesRes as any).data || []
    books.value = ((booksRes as any).data?.records || []).slice(0, 10)
  } catch (e: any) {
    showToast(e.message || '加载失败')
  } finally {
    refreshing.value = false
  }
}

const onCategoryClick = (category: Category) => {
  router.push({ path: '/category', query: { id: category.id.toString() } })
}

const onSearchClick = () => {
  router.push('/search')
}

const onPublishClick = () => {
  router.push('/book/publish')
}

onMounted(async () => {
  loading.value = true
  try {
    const [categoriesRes, booksRes] = await Promise.all([
      getCategories(),
      getBooks({ page: 1, size: 10 })
    ])
    categories.value = (categoriesRes as any).data || []
    books.value = ((booksRes as any).data?.records || []).slice(0, 10)
  } catch (e: any) {
    showToast(e.message || '加载失败')
    // Fallback to mock data if API fails
    categories.value = [
      { id: 1, name: '教材', parentId: 0, level: 1, sort: 1, status: 1 },
      { id: 2, name: '小说', parentId: 0, level: 1, sort: 2, status: 1 },
      { id: 3, name: '专业书', parentId: 0, level: 1, sort: 3, status: 1 },
      { id: 4, name: '杂志', parentId: 0, level: 1, sort: 4, status: 1 },
      { id: 5, name: '工具书', parentId: 0, level: 1, sort: 5, status: 1 },
      { id: 6, name: '其他', parentId: 0, level: 1, sort: 6, status: 1 }
    ]
    books.value = [
      {
        id: 1,
        title: '高等数学第七版上册',
        author: '同济大学数学系',
        categoryId: 1,
        categoryName: '教材',
        coverImage: 'https://picsum.photos/160/200?random=10',
        price: 25,
        condition: 3,
        conditionName: '9成新',
        status: 1,
        sellerId: 1,
        sellerName: '小明同学',
        viewCount: 128,
        collectCount: 12
      }
    ]
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <MainLayout>
    <div class="page-container">
      <!-- 顶部搜索栏 -->
      <div class="search-bar" @click="onSearchClick">
        <van-icon name="search" size="16" />
        <span class="search-placeholder">搜索书名/作者/ISBN</span>
      </div>

      <!-- 轮播图 -->
      <div class="banner">
        <van-swipe :autoplay="3000" :show-indicators="false" lazy-render>
          <van-swipe-item v-for="item in banners" :key="item.id">
            <img :src="item.image" class="banner-img" />
          </van-swipe-item>
        </van-swipe>
      </div>

      <!-- 分类入口 -->
      <div class="category-grid">
        <van-grid :column-num="6" :border="false">
          <van-grid-item v-for="cat in categories" :key="cat.id" @click="onCategoryClick(cat)">
            <div class="category-icon">
              <van-icon name="book-mark" size="24" color="#FF6B35" />
            </div>
            <span class="category-name">{{ cat.name }}</span>
          </van-grid-item>
        </van-grid>
      </div>

      <!-- 发布按钮 -->
      <div class="publish-btn-wrap">
        <van-button type="primary" size="small" round @click="onPublishClick">
          <van-icon name="plus" /> 我要卖书
        </van-button>
      </div>

      <!-- 推荐书籍 -->
      <div class="section">
        <div class="section-header">
          <span class="section-title">推荐书籍</span>
          <span class="section-more" @click="router.push('/search')">更多</span>
        </div>
        <div class="book-list" v-if="books.length > 0">
          <BookCard
            v-for="book in books"
            :key="book.id"
            :book="book"
            show-seller
          />
        </div>
        <EmptyState v-else message="暂无书籍" />
      </div>
    </div>
  </MainLayout>
</template>

<style scoped>
.search-bar {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 12px;
  padding: 10px 16px;
  background: #fff;
  border-radius: 20px;
  color: #969799;
  font-size: 14px;
}

.banner {
  margin: 0 12px;
  border-radius: 8px;
  overflow: hidden;
}

.banner-img {
  width: 100%;
  height: 150px;
  object-fit: cover;
}

.category-grid {
  margin: 16px 12px;
  background: #fff;
  border-radius: 12px;
  padding: 16px 8px;
}

.category-icon {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #FFF7E6;
  border-radius: 10px;
  margin-bottom: 6px;
}

.category-name {
  font-size: 12px;
  color: #323233;
}

.publish-btn-wrap {
  padding: 0 12px;
  margin-bottom: 16px;
}

.publish-btn-wrap .van-button {
  width: 100%;
  background: linear-gradient(135deg, #FF6B35, #FF8F65);
  border: none;
}

.section {
  padding: 0 12px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
}

.section-more {
  font-size: 12px;
  color: #969799;
}

.book-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
</style>
