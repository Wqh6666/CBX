<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Field, FieldRow, Switch, Button, showToast } from 'vant'
import type { Address } from '@/types'

const router = useRouter()
const route = useRoute()

const isEdit = computed(() => !!route.query.id)

const form = ref({
  name: '',
  phone: '',
  province: '上海市',
  city: '上海市',
  district: '',
  detail: '',
  isDefault: false
})

const submitting = ref(false)

const onSubmit = async () => {
  if (!form.value.name.trim()) {
    showToast('请输入收货人姓名')
    return
  }
  if (!form.value.phone || form.value.phone.length !== 11) {
    showToast('请输入正确的手机号')
    return
  }
  if (!form.value.district) {
    showToast('请选择省市区')
    return
  }
  if (!form.value.detail.trim()) {
    showToast('请输入详细地址')
    return
  }

  submitting.value = true
  try {
    await new Promise(resolve => setTimeout(resolve, 1000))
    showToast(isEdit.value ? '修改成功' : '添加成功')
    router.back()
  } catch (e) {
    showToast('操作失败')
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <div class="page-container">
    <van-nav-bar
      :title="isEdit ? '编辑地址' : '新增地址'"
      left-text="返回"
      left-arrow
      @click-left="router.back()"
    />

    <div class="form-content">
      <van-cell-group inset>
        <van-field
          v-model="form.name"
          label="收货人"
          placeholder="请输入收货人姓名"
          :border="false"
        />
        <van-field
          v-model="form.phone"
          type="tel"
          label="手机号"
          placeholder="请输入手机号"
          maxlength="11"
          :border="false"
        />
        <van-field
          v-model="form.district"
          label="所在地区"
          placeholder="请选择省市区"
          readonly
          :border="false"
        >
          <template #right-icon>
            <van-icon name="arrow" />
          </template>
        </van-field>
        <van-field
          v-model="form.detail"
          type="textarea"
          label="详细地址"
          placeholder="请输入详细地址"
          rows="2"
          :border="false"
        />
        <van-cell title="设为默认地址">
          <template #right-icon>
            <van-switch v-model="form.isDefault" size="20" />
          </template>
        </van-cell>
      </van-cell-group>

      <div class="submit-btn">
        <van-button
          type="primary"
          round
          block
          :loading="submitting"
          @click="onSubmit"
        >
          保存
        </van-button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.form-content {
  padding: 16px 0;
}

.form-content :deep(.van-cell-group) {
  margin: 0;
}

.submit-btn {
  padding: 24px 16px;
}

.submit-btn .van-button {
  background: linear-gradient(135deg, #FF6B35, #FF8F65);
  border: none;
}
</style>
