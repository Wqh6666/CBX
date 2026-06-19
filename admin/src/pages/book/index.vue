<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElTable, ElMessage, ElMessageBox } from 'element-plus'
import { getBooks, offshelfBook } from '@/api/book'
import { formatDateTime, getBookStatusName, getBookStatusType, formatPrice } from '@/utils/format'
import type { Book } from '@/types'

const loading = ref(false)
const tableData = ref<Book[]>([])
const pagination = ref({
  page: 1,
  size: 10,
  total: 0
})
const searchForm = ref({
  status: undefined as number | undefined,
  keyword: ''
})

const loadData = async () => {
  loading.value = true
  try {
    const res: any = await getBooks({
      page: pagination.value.page,
      size: pagination.value.size,
      status: searchForm.value.status,
      keyword: searchForm.value.keyword || undefined
    })
    tableData.value = res.data.records
    pagination.value.total = res.data.total
  } catch (e: any) {
    ElMessage.error(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.value.page = 1
  loadData()
}

const handleReset = () => {
  searchForm.value = { status: undefined, keyword: '' }
  handleSearch()
}

const handlePageChange = (page: number) => {
  pagination.value.page = page
  loadData()
}

const handleSizeChange = (size: number) => {
  pagination.value.size = size
  loadData()
}

const handleOffshelf = async (row: Book) => {
  try {
    await ElMessageBox.prompt('请输入下架原因', '下架书籍', {
      confirmButtonText: '确定',
      cancelButtonText: '取消'
    })
    const reason = '管理员下架'
    await offshelfBook(row.id, { reason })
    ElMessage.success('下架成功')
    loadData()
  } catch (e) {
    // 取消
  }
}

onMounted(() => {
  loadData()
})
</script>

<template>
  <div class="book-manage">
    <h2 class="page-title">书籍管理</h2>

    <el-card class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" clearable placeholder="全部状态">
            <el-option label="待审核" :value="0" />
            <el-option label="上架中" :value="1" />
            <el-option label="已下架" :value="2" />
            <el-option label="违规下架" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="关键词">
          <el-input v-model="searchForm.keyword" placeholder="书名/作者" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="书名" min-width="200" show-overflow-tooltip />
        <el-table-column prop="author" label="作者" width="120" show-overflow-tooltip />
        <el-table-column prop="price" label="售价" width="100">
          <template #default="{ row }">
            {{ formatPrice(row.price) }}
          </template>
        </el-table-column>
        <el-table-column prop="categoryName" label="分类" width="100" />
        <el-table-column prop="sellerName" label="卖家" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getBookStatusType(row.status)">
              {{ getBookStatusName(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="发布时间" width="160">
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="row.status === 1"
              type="danger"
              size="small"
              @click="handleOffshelf(row)"
            >
              下架
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          @change="handlePageChange"
        />
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.book-manage {
  padding: 0;
}

.page-title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 20px;
  color: #303133;
}

.search-card {
  margin-bottom: 16px;
}

.table-card {
  margin-top: 10px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
