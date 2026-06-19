<script setup lang="ts">
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Field, Button, showToast } from 'vant'
import { useUserStore } from '@/stores/user'
import { login, sendCode } from '@/api/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const phone = ref('')
const code = ref('')
const loading = ref(false)
const sendingCode = ref(false)
const countdown = ref(0)

const onSendCode = async () => {
  if (!phone.value || phone.value.length !== 11) {
    showToast('请输入正确的手机号')
    return
  }

  sendingCode.value = true
  try {
    await sendCode(phone.value)
    showToast('验证码已发送')
    countdown.value = 60
    const timer = setInterval(() => {
      countdown.value--
      if (countdown.value <= 0) {
        clearInterval(timer)
      }
    }, 1000)
  } catch (e: any) {
    showToast(e.message || '发送失败')
  } finally {
    sendingCode.value = false
  }
}

const onLogin = async () => {
  if (!phone.value || phone.value.length !== 11) {
    showToast('请输入正确的手机号')
    return
  }
  if (!code.value || code.value.length !== 4) {
    showToast('请输入4位验证码')
    return
  }

  loading.value = true
  try {
    const res: any = await login({ phone: phone.value, code: code.value })
    const token = res.data?.token
    const userInfo = res.data?.userInfo

    if (token && userInfo) {
      userStore.login(token, userInfo)
      showToast('登录成功')

      const redirect = route.query.redirect as string
      if (redirect) {
        router.replace(redirect)
      } else {
        router.replace('/')
      }
    } else {
      showToast('登录失败')
    }
  } catch (e: any) {
    showToast(e.message || '登录失败')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-page">
    <div class="login-header">
      <div class="logo">
        <van-icon name="book-mark" size="48" color="#FF6B35" />
      </div>
      <h1 class="title">校园二手书</h1>
      <p class="subtitle">让闲置书籍找到新主人</p>
    </div>

    <div class="login-form">
      <van-field
        v-model="phone"
        type="tel"
        maxlength="11"
        placeholder="请输入手机号"
        clearable
      >
        <template #left-icon>
          <van-icon name="phone-o" />
        </template>
      </van-field>

      <van-field
        v-model="code"
        type="digit"
        maxlength="4"
        placeholder="请输入验证码"
        clearable
      >
        <template #left-icon>
          <van-icon name="shield-o" />
        </template>
        <template #button>
          <van-button
            size="small"
            type="primary"
            plain
            :disabled="countdown > 0"
            :loading="sendingCode"
            @click="onSendCode"
          >
            {{ countdown > 0 ? `${countdown}s` : '获取验证码' }}
          </van-button>
        </template>
      </van-field>

      <van-button
        type="primary"
        round
        block
        :loading="loading"
        @click="onLogin"
      >
        登录
      </van-button>

      <div class="login-tip">
        <span>登录即表示同意</span>
        <a href="#">《用户协议》</a>
        <span>和</span>
        <a href="#">《隐私政策》</a>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  min-height: 100vh;
  background: linear-gradient(180deg, #FFF7E6 0%, #FFFFFF 100%);
  padding: 60px 24px 24px;
}

.login-header {
  text-align: center;
  margin-bottom: 48px;
}

.logo {
  width: 80px;
  height: 80px;
  background: #fff;
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16px;
  box-shadow: 0 4px 20px rgba(255, 107, 53, 0.15);
}

.title {
  font-size: 24px;
  font-weight: 700;
  color: #323233;
  margin-bottom: 8px;
}

.subtitle {
  font-size: 14px;
  color: #969799;
}

.login-form {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
}

.login-form :deep(.van-field) {
  padding: 16px 0;
  border-bottom: 1px solid #ebedf0;
  margin-bottom: 0;
}

.login-form :deep(.van-field:first-child) {
  margin-bottom: 0;
}

.login-form .van-button {
  margin-top: 24px;
  height: 44px;
  font-size: 16px;
  background: linear-gradient(135deg, #FF6B35, #FF8F65);
  border: none;
}

.login-tip {
  text-align: center;
  font-size: 12px;
  color: #969799;
  margin-top: 16px;
}

.login-tip a {
  color: #FF6B35;
  text-decoration: none;
}
</style>
