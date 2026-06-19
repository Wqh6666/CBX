<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Uploader, Field, Cell, CellGroup, Picker, Popup, Button, showToast } from 'vant'
import type { Category } from '@/types'

const router = useRouter()
const route = useRoute()

const isEdit = computed(() => !!route.params.id)

const categories = ref<Category[]>([
  { id: 1, name: '教材', parentId: 0, level: 1, sort: 1 },
  { id: 2, name: '小说', parentId: 0, level: 1, sort: 2 },
  { id: 3, name: '专业书', parentId: 0, level: 1, sort: 3 },
  { id: 4, name: '杂志', parentId: 0, level: 1, sort: 4 },
  { id: 5, name: '工具书', parentId: 0, level: 1, sort: 5 },
  { id: 6, name: '其他', parentId: 0, level: 1, sort: 6 }
])

const conditionOptions = [
  { text: '全新', value: 1 },
  { text: '95新', value: 2 },
  { text: '9成新', value: 3 },
  { text: '8成新', value: 4 },
  { text: '7成新及以下', value: 5 }
]

const form = ref({
  title: '',
  author: '',
  isbn: '',
  publisher: '',
  categoryId: 0,
  coverImage: '',
  images: [] as string[],
  description: '',
  originalPrice: '',
  price: '',
  condition: 3
})

const showCategoryPicker = ref(false)
const showConditionPicker = ref(false)
const submitting = ref(false)

const selectedCategoryName = computed(() => {
  const cat = categories.value.find(c => c.id === form.value.categoryId)
  return cat?.name || '请选择分类'
})

const onCategoryConfirm = (value: any) => {
  form.value.categoryId = value.selectedValues[0]
  showCategoryPicker.value = false
}

const onConditionConfirm = (value: any) => {
  form.value.condition = value.selectedValues[0]
  showConditionPicker.value = false
}

const onCoverChange = (result: any) => {
  if (result.status === 'success') {
    form.value.coverImage = result.content
    showToast('封面上传成功')
  }
}

const onImageChange = (result: any) => {
  if (result.status === 'success') {
    form.value.images.push(result.content)
    showToast('图片上传成功')
  }
}

const onRemoveImage = (index: number) => {
  form.value.images.splice(index, 1)
}

const validateForm = () => {
  if (!form.value.title.trim()) {
    showToast('请输入书名')
    return false
  }
  if (!form.value.categoryId) {
    showToast('请选择分类')
    return false
  }
  if (!form.value.price) {
    showToast('请输入售价')
    return false
  }
  if (parseFloat(form.value.price) <= 0) {
    showToast('售价必须大于0')
    return false
  }
  return true
}

const onSubmit = async (asDraft = false) => {
  if (!asDraft && !validateForm()) return

  submitting.value = true
  try {
    // 模拟 API 调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    showToast(asDraft ? '保存草稿成功' : '发布成功')
    router.push('/user/my-books')
  } catch (e) {
    showToast('操作失败')
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <div class="page-container">
    <!-- 顶部导航 -->
    <van-nav-bar
      title="发布书籍"
      left-text="返回"
      left-arrow
      @click-left="router.back()"
    />

    <!-- 表单内容 -->
    <div class="form-content">
      <!-- 封面图片 -->
      <div class="form-item">
        <div class="form-label">书籍封面 <span class="required">*</span></div>
        <div class="cover-uploader">
          <van-uploader
            :after-read="onCoverChange"
            :max-count="1"
            :preview-size="100"
          >
            <div v-if="form.coverImage" class="cover-preview">
              <img :src="form.coverImage" />
            </div>
            <div v-else class="cover-placeholder">
              <van-icon name="plus" size="24" />
              <span>上传封面</span>
            </div>
          </van-uploader>
        </div>
      </div>

      <!-- 详情图片 -->
      <div class="form-item">
        <div class="form-label">详情图片</div>
        <van-uploader
          :after-read="onImageChange"
          multiple
          :max-count="9"
        >
          <div class="image-uploader">
            <van-icon name="plus" size="24" />
          </div>
        </van-uploader>
        <div v-if="form.images.length > 0" class="image-list">
          <div v-for="(img, index) in form.images" :key="index" class="image-item">
            <img :src="img" />
            <van-icon name="close" class="remove-btn" @click="onRemoveImage(index)" />
          </div>
        </div>
      </div>

      <!-- 书籍信息 -->
      <div class="form-item">
        <div class="form-label">书名 <span class="required">*</span></div>
        <van-field
          v-model="form.title"
          placeholder="请输入书名"
          :border="false"
        />
      </div>

      <div class="form-item">
        <div class="form-label">作者</div>
        <van-field
          v-model="form.author"
          placeholder="请输入作者"
          :border="false"
        />
      </div>

      <div class="form-item">
        <div class="form-label">ISBN</div>
        <van-field
          v-model="form.isbn"
          placeholder="请输入ISBN"
          :border="false"
        />
      </div>

      <div class="form-item">
        <div class="form-label">出版社</div>
        <van-field
          v-model="form.publisher"
          placeholder="请输入出版社"
          :border="false"
        />
      </div>

      <div class="form-item" @click="showCategoryPicker = true">
        <div class="form-label">分类 <span class="required">*</span></div>
        <van-field
          :model-value="selectedCategoryName"
          placeholder="请选择分类"
          readonly
          :border="false"
        >
          <template #right-icon>
            <van-icon name="arrow" />
          </template>
        </van-field>
      </div>

      <!-- 分类选择器 -->
      <van-popup v-model:show="showCategoryPicker" position="bottom">
        <van-picker
          :columns="categories.map(c => ({ text: c.name, value: c.id }))"
          @confirm="onCategoryConfirm"
          @cancel="showCategoryPicker = false"
        />
      </van-popup>

      <div class="form-item" @click="showConditionPicker = true">
        <div class="form-label">成色 <span class="required">*</span></div>
        <van-field
          :model-value="conditionOptions.find(c => c.value === form.condition)?.text"
          placeholder="请选择成色"
          readonly
          :border="false"
        >
          <template #right-icon>
            <van-icon name="arrow" />
          </template>
        </van-field>
      </div>

      <!-- 成色选择器 -->
      <van-popup v-model:show="showConditionPicker" position="bottom">
        <van-picker
          :columns="conditionOptions"
          @confirm="onConditionConfirm"
          @cancel="showConditionPicker = false"
        />
      </van-popup>

      <!-- 价格信息 -->
      <div class="form-item price-row">
        <div class="form-item-half">
          <div class="form-label">原价 <span class="text-secondary">(选填)</span></div>
          <van-field
            v-model="form.originalPrice"
            type="number"
            placeholder="0.00"
            :border="false"
          />
        </div>
        <div class="form-item-half">
          <div class="form-label">售价 <span class="required">*</span></div>
          <van-field
            v-model="form.price"
            type="number"
            placeholder="0.00"
            :border="false"
          />
        </div>
      </div>

      <!-- 书籍描述 -->
      <div class="form-item">
        <div class="form-label">书籍描述</div>
        <van-field
          v-model="form.description"
          type="textarea"
          placeholder="请描述书籍的详细信息..."
          rows="4"
          :border="false"
        />
      </div>
    </div>

    <!-- 底部按钮 -->
    <div class="form-actions">
      <van-button size="small" round @click="onSubmit(true)" :loading="submitting">保存草稿</van-button>
      <van-button size="small" round type="primary" @click="onSubmit(false)" :loading="submitting">直接发布</van-button>
    </div>
  </div>
</template>

<style scoped>
.form-content {
  padding: 16px;
  padding-bottom: 80px;
}

.form-item {
  margin-bottom: 20px;
}

.form-label {
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 8px;
  color: #323233;
}

.required {
  color: #ee0a24;
}

.cover-uploader {
  display: flex;
}

.cover-placeholder {
  width: 100px;
  height: 130px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
  background: #f5f5f5;
  border-radius: 8px;
  color: #969799;
  font-size: 12px;
}

.cover-preview img {
  width: 100px;
  height: 130px;
  object-fit: cover;
  border-radius: 8px;
}

.image-uploader {
  width: 80px;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
  border-radius: 8px;
  color: #969799;
}

.image-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-top: 10px;
}

.image-item {
  position: relative;
  width: 80px;
  height: 80px;
}

.image-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
}

.remove-btn {
  position: absolute;
  top: -8px;
  right: -8px;
  width: 18px;
  height: 18px;
  background: rgba(0, 0, 0, 0.6);
  border-radius: 50%;
  color: #fff;
  font-size: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.price-row {
  display: flex;
  gap: 16px;
}

.form-item-half {
  flex: 1;
}

.form-actions {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 12px 16px;
  padding-bottom: calc(12px + env(safe-area-inset-bottom));
  background: #fff;
  border-top: 1px solid #ebedf0;
}
</style>
