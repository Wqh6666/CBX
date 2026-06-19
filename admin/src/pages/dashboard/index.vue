<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElCard } from 'element-plus'
import { getDashboard } from '@/api/statistics'
import { formatPrice } from '@/utils/format'

const stats = ref({
  totalUsers: 0,
  activeUsers: 0,
  totalBooks: 0,
  onSaleBooks: 0,
  pendingBooks: 0,
  totalOrders: 0,
  completedOrders: 0,
  pendingOrders: 0,
  totalAmount: 0
})

const statCards = [
  { title: '用户总数', value: () => stats.value.totalUsers, icon: 'User', color: '#409eff' },
  { title: '活跃用户', value: () => stats.value.activeUsers, icon: 'UserFilled', color: '#67c23a' },
  { title: '书籍总数', value: () => stats.value.totalBooks, icon: 'Books', color: '#e6a23c' },
  { title: '上架中', value: () => stats.value.onSaleBooks, icon: 'Goods', color: '#f56c6c' },
  { title: '待审核', value: () => stats.value.pendingBooks, icon: 'DocumentChecked', color: '#909399' },
  { title: '订单总数', value: () => stats.value.totalOrders, icon: 'List', color: '#409eff' },
  { title: '已完成订单', value: () => stats.value.completedOrders, icon: 'CircleCheck', color: '#67c23a' },
  { title: '待处理订单', value: () => stats.value.pendingOrders, icon: 'Clock', color: '#e6a23c' },
  { title: '交易总额', value: () => formatPrice(stats.value.totalAmount), icon: 'Money', color: '#f56c6c' }
]

onMounted(async () => {
  try {
    const res: any = await getDashboard()
    stats.value = res.data
  } catch (e) {
    console.error(e)
  }
})
</script>

<template>
  <div class="dashboard">
    <h2 class="page-title">仪表盘</h2>

    <el-row :gutter="20" class="stat-row">
      <el-col v-for="(card, index) in statCards" :key="index" :xs="24" :sm="12" :md="8" :lg="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" :style="{ backgroundColor: card.color + '15', color: card.color }">
              <el-icon :size="28"><component :is="card.icon" /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ card.value() }}</div>
              <div class="stat-title">{{ card.title }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="chart-row">
      <el-col :span="24">
        <el-card class="welcome-card">
          <template #header>
            <span>欢迎使用</span>
          </template>
          <div class="welcome-content">
            <h3>校园二手书交易平台管理后台</h3>
            <p>这是一个简洁高效的管理系统，帮助您管理用户、书籍、订单等核心业务。</p>
            <div class="quick-actions">
              <router-link to="/book/pending">
                <el-button type="primary">审核书籍</el-button>
              </router-link>
              <router-link to="/order">
                <el-button type="success">处理订单</el-button>
              </router-link>
              <router-link to="/user">
                <el-button>查看用户</el-button>
              </router-link>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.dashboard {
  padding: 0;
}

.page-title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 20px;
  color: #303133;
}

.stat-row {
  margin-bottom: 20px;
}

.stat-card {
  margin-bottom: 16px;
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #303133;
  line-height: 1.2;
}

.stat-title {
  font-size: 13px;
  color: #909399;
  margin-top: 4px;
}

.welcome-card {
  margin-top: 10px;
}

.welcome-content {
  text-align: center;
  padding: 20px;
}

.welcome-content h3 {
  font-size: 20px;
  color: #303133;
  margin-bottom: 12px;
}

.welcome-content p {
  color: #606266;
  margin-bottom: 24px;
}

.quick-actions {
  display: flex;
  justify-content: center;
  gap: 12px;
}
</style>
