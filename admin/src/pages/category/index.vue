<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElTable, ElMessage, ElMessageBox } from 'element-plus'
import { getCategories, createCategory, updateCategory, deleteCategory } from '@/api/category'
import type { Category } from '@/types'

const loading = ref(false)
const tableData = ref<Category[]>([])
const dialog = ref({
  visible: false,
  isEdit: false,
  form: {
    id: undefined as number | undefined,
    name: '',
    parentId: 0,
    sort: 0,
    status: 1
  }
})

const loadData = async () => {
  loading.value = true
  try {
    const res: any = await getCategories()
    tableData.value = res.data
  } catch (e: any) {
    ElMessage.error(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  dialog.value = {
    visible: true,
    isEdit: false,
    form: {
      id: undefined,
      name: '',
      parentId: 0,
      sort: 0,
      status: 1
    }
  }
}

const handleEdit = (row: Category) => {
  dialog.value = {
    visible: true,
    isEdit: true,
    form: {
      id: row.id,
      name: row.name,
      parentId: row.parentId,
      sort: row.sort,
      status: row.status
    }
  }
}

const handleSave = async () => {
  if (!dialog.value.form.name) {
    ElMessage.warning('请输入分类名称')
    return
  }
  try {
    if (dialog.value.isEdit) {
      await updateCategory(dialog.value.form.id!, dialog.value.form)
      ElMessage.success('修改成功')
    } else {
      await createCategory(dialog.value.form)
      ElMessage.success('添加成功')
    }
    dialog.value.visible = false
    loadData()
  } catch (e: any) {
    ElMessage.error(e.message || '操作失败')
  }
}

const handleDelete = async (row: Category) => {
  try {
    await ElMessageBox.confirm('确定要删除该分类吗？', '提示', { type: 'warning' })
    await deleteCategory(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (e: any) {
    if (e !== 'cancel') {
      ElMessage.error(e.message || '删除失败')
    }
  }
}

onMounted(() => {
  loadData()
})
</script>

<template>
  <div class="category-manage">
    <h2 class="page-title">分类管理</h2>

    <el-card class="search-card">
      <el-button type="primary" @click="handleAdd">添加分类</el-button>
    </el-card>

    <el-card class="table-card">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="分类名称" min-width="200" />
        <el-table-column prop="parentId" label="父级ID" width="100">
          <template #default="{ row }">
            {{ row.parentId === 0 ? '顶级' : row.parentId }}
          </template>
        </el-table-column>
        <el-table-column prop="level" label="层级" width="80">
          <template #default="{ row }">
            {{ row.level === 1 ? '一级' : '二级' }}
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
      v-model="dialog.visible"
      :title="dialog.isEdit ? '编辑分类' : '添加分类'"
      width="400px"
    >
      <el-form :model="dialog.form" label-width="80px">
        <el-form-item label="分类名称" required>
          <el-input v-model="dialog.form.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="父级分类">
          <el-select v-model="dialog.form.parentId" placeholder="顶级分类">
            <el-option label="顶级分类" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="dialog.form.sort" :min="0" :max="999" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="dialog.form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog.visible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.category-manage {
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
</style>
