<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElTable, ElMessage, ElMessageBox } from 'element-plus'
import { getPendingBooks, auditBook } from '@/api/book'
import { formatDateTime, formatPrice } from '@/utils/format'
import type { Book } from '@/types'

const loading = ref(false)
const tableData = ref<Book[]>([])
const pagination = ref({
  page: 1,
  size: 10,
  total: 0
})

const loadData = async () => {
  loading.value = true
  try {
    const res: any = await getPendingBooks({
      page: pagination.value.page,
      size: pagination.value.size
    })
    tableData.value = res.data.records
    pagination.value.total = res.data.total
  } catch (e: any) {
    ElMessage.error(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

const handleAudit = async (row: Book, status: number) => {
  const action = status === 1 ? '通过' : '拒绝'
  try {
    if (status === 0) {
      await ElMessageBox.prompt('请输入拒绝原因', '审核拒绝', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      })
    }
    const reason = status === 0 ? '不符合平台规范' : ''
    await auditBook(row.id, { status, reason })
    ElMessage.success(`审核${action}成功`)
    loadData()
  } catch (e: any) {
    if (e !== 'cancel') {
      ElMessage.error(e.message || '操作失败')
    }
  }
}

const handlePageChange = (page: number) => {
  pagination.value.page = page
  loadData()
}

onMounted(() => {
  loadData()
})
</script>

<template>
  <div class="book-pending">
    <h2 class="page-title">书籍审核</h2>

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
        <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="createTime" label="提交时间" width="160">
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="success" size="small" @click="handleAudit(row, 1)">
              通过
            </el-button>
            <el-button type="danger" size="small" @click="handleAudit(row, 0)">
              拒绝
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
.book-pending {
  padding: 0;
}

.page-title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 20px;
  color: #303133;
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
