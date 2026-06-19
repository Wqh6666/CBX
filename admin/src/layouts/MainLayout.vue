<script setup lang="ts">
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useAdminStore } from '@/stores/admin'
import { logout } from '@/api/auth'

const router = useRouter()
const route = useRoute()
const adminStore = useAdminStore()
const isCollapse = ref(false)

const menuItems = [
  { path: '/dashboard', title: '仪表盘', icon: 'DataAnalysis' },
  { path: '/book', title: '书籍管理', icon: 'Books' },
  { path: '/book/pending', title: '书籍审核', icon: 'DocumentChecked' },
  { path: '/order', title: '订单管理', icon: 'List' },
  { path: '/user', title: '用户管理', icon: 'User' },
  { path: '/category', title: '分类管理', icon: 'Collection' }
]

const activeMenu = () => route.path

const handleMenuSelect = (path: string) => {
  router.push(path)
}

const handleLogout = async () => {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      type: 'warning'
    })
    await logout()
    adminStore.logout()
    router.push('/login')
  } catch (e) {
    adminStore.logout()
    router.push('/login')
  }
}
</script>

<template>
  <el-container class="layout-container">
    <el-aside width="200px" class="aside">
      <div class="logo">
        <el-icon :size="24"><Books /></el-icon>
        <span v-if="!isCollapse">二手书平台</span>
      </div>
      <el-menu
        :default-active="activeMenu()"
        :collapse="isCollapse"
        router
        class="menu"
      >
        <el-menu-item
          v-for="item in menuItems"
          :key="item.path"
          :index="item.path"
        >
          <el-icon><component :is="item.icon" /></el-icon>
          <template #title>{{ item.title }}</template>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="header">
        <div class="header-left">
          <el-icon class="collapse-btn" @click="isCollapse = !isCollapse">
            <Fold v-if="!isCollapse" />
            <Expand v-else />
          </el-icon>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleLogout">
            <span class="user-info">
              <el-icon><Avatar /></el-icon>
              <span>{{ adminStore.adminInfo?.nickname || '管理员' }}</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.layout-container {
  height: 100vh;
}

.aside {
  background: #304156;
  transition: width 0.3s;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  border-bottom: 1px solid #3d4a5c;
}

.menu {
  border-right: none;
  background: #304156;
}

.menu :deep(.el-menu-item) {
  color: #bfcbd9;
}

.menu :deep(.el-menu-item:hover),
.menu :deep(.el-menu-item.is-active) {
  background: #263445;
  color: #409eff;
}

.header {
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
}

.header-left {
  display: flex;
  align-items: center;
}

.collapse-btn {
  font-size: 20px;
  cursor: pointer;
  padding: 0 12px;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  padding: 0 12px;
}

.main {
  background: #f5f7fa;
  padding: 20px;
}
</style>
