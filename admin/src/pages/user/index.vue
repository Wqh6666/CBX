<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElTable, ElMessage, ElMessageBox } from 'element-plus'
import { getUsers, updateUserStatus } from '@/api/user'
import { formatDateTime } from '@/utils/format'
import type { User } from '@/types'

const loading = ref(false)
const tableData = ref<User[]>([])
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
    const res: any = await getUsers({
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

const handleStatusChange = async (row: User, status: number) => {
  const action = status === 1 ? '启用' : '禁用'
  try {
    await ElMessageBox.confirm(`确定要${action}该用户吗？`, '提示', { type: 'warning' })
    await updateUserStatus(row.id, status)
    ElMessage.success(`${action}成功`)
    loadData()
  } catch (e: any) {
    if (e !== 'cancel') {
      ElMessage.error(e.message || '操作失败')
    }
  }
}

onMounted(() => {
  loadData()
})
</script>

<template>
  <div class="user-manage">
    <h2 class="page-title">用户管理</h2>

    <el-card class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" clearable placeholder="全部状态">
            <el-option label="正常" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="关键词">
          <el-input v-model="searchForm.keyword" placeholder="昵称/手机号" clearable />
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
        <el-table-column prop="nickname" label="昵称" width="120" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="avatar" label="头像" width="80">
          <template #default="{ row }">
            <el-avatar v-if="row.avatar" :src="row.avatar" :size="40" />
            <el-avatar v-else :size="40">{{ row.nickname?.[0] || 'U' }}</el-avatar>
          </template>
        </el-table-column>
        <el-table-column prop="gender" label="性别" width="80">
          <template #default="{ row }">
            {{ row.gender === 1 ? '男' : row.gender === 2 ? '女' : '未知' }}
          </template>
        </el-table-column>
        <el-table-column prop="email" label="邮箱" min-width="150" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" width="160">
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
              @click="handleStatusChange(row, 0)"
            >
              禁用
            </el-button>
            <el-button
              v-else
              type="success"
              size="small"
              @click="handleStatusChange(row, 1)"
            >
              启用
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
.user-manage {
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
