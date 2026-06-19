<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElTable, ElMessage, ElMessageBox } from 'element-plus'
import { getOrders, shipOrder, refundOrder } from '@/api/order'
import { formatDateTime, getOrderStatusName, getOrderStatusType, formatPrice } from '@/utils/format'
import type { Order } from '@/types'

const loading = ref(false)
const tableData = ref<Order[]>([])
const pagination = ref({
  page: 1,
  size: 10,
  total: 0
})
const searchForm = ref({
  status: undefined as number | undefined
})
const shipDialog = ref({
  visible: false,
  orderId: null as number | null,
  form: {
    expressNo: '',
    expressCompany: ''
  }
})

const loadData = async () => {
  loading.value = true
  try {
    const res: any = await getOrders({
      page: pagination.value.page,
      size: pagination.value.size,
      status: searchForm.value.status
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
  searchForm.value = { status: undefined }
  handleSearch()
}

const handlePageChange = (page: number) => {
  pagination.value.page = page
  loadData()
}

const handleShip = (row: Order) => {
  shipDialog.value = {
    visible: true,
    orderId: row.id,
    form: {
      expressNo: '',
      expressCompany: ''
    }
  }
}

const confirmShip = async () => {
  if (!shipDialog.value.form.expressNo || !shipDialog.value.form.expressCompany) {
    ElMessage.warning('请填写完整信息')
    return
  }
  try {
    await shipOrder(shipDialog.value.orderId!, shipDialog.value.form)
    ElMessage.success('发货成功')
    shipDialog.value.visible = false
    loadData()
  } catch (e: any) {
    ElMessage.error(e.message || '发货失败')
  }
}

const handleRefund = async (row: Order) => {
  try {
    await ElMessageBox.confirm('确定要退款吗？', '提示', { type: 'warning' })
    await refundOrder(row.id)
    ElMessage.success('退款成功')
    loadData()
  } catch (e: any) {
    if (e !== 'cancel') {
      ElMessage.error(e.message || '退款失败')
    }
  }
}

onMounted(() => {
  loadData()
})
</script>

<template>
  <div class="order-manage">
    <h2 class="page-title">订单管理</h2>

    <el-card class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="订单状态">
          <el-select v-model="searchForm.status" clearable placeholder="全部状态">
            <el-option label="待付款" :value="0" />
            <el-option label="待发货" :value="1" />
            <el-option label="待收货" :value="2" />
            <el-option label="已完成" :value="3" />
            <el-option label="已取消" :value="4" />
            <el-option label="退款中" :value="5" />
            <el-option label="已退款" :value="6" />
          </el-select>
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
        <el-table-column prop="orderNo" label="订单号" width="180" show-overflow-tooltip />
        <el-table-column label="书籍" min-width="200">
          <template #default="{ row }">
            <div v-if="row.book">{{ row.book.title }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="金额" width="100">
          <template #default="{ row }">
            {{ formatPrice(row.amount) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getOrderStatusType(row.status)">
              {{ getOrderStatusName(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="expressNo" label="快递单号" width="150" show-overflow-tooltip />
        <el-table-column prop="createTime" label="下单时间" width="160">
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="row.status === 1"
              type="primary"
              size="small"
              @click="handleShip(row)"
            >
              发货
            </el-button>
            <el-button
              v-if="row.status === 1 || row.status === 5"
              type="danger"
              size="small"
              @click="handleRefund(row)"
            >
              退款
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

    <el-dialog v-model="shipDialog.visible" title="发货" width="400px">
      <el-form :model="shipDialog.form">
        <el-form-item label="快递公司" required>
          <el-input v-model="shipDialog.form.expressCompany" placeholder="如：顺丰速运" />
        </el-form-item>
        <el-form-item label="快递单号" required>
          <el-input v-model="shipDialog.form.expressNo" placeholder="请输入快递单号" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="shipDialog.visible = false">取消</el-button>
        <el-button type="primary" @click="confirmShip">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.order-manage {
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
