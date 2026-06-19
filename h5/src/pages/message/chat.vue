<script setup lang="ts">
import { ref, onMounted, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Image as VanImage } from 'vant'
import { formatTime } from '@/utils/format'
import type { Message, User } from '@/types'

const route = useRoute()
const router = useRouter()

const userId = parseInt(route.params.userId as string)
const otherUser = ref<User>({
  id: userId,
  phone: '',
  nickname: '小明同学',
  avatar: 'https://picsum.photos/40/40?random=100',
  gender: 1,
  email: '',
  status: 1,
  createTime: ''
})

const messages = ref<Message[]>([])
const inputMessage = ref('')
const messageListRef = ref<HTMLElement>()

const mockMessages: Message[] = [
  {
    id: 1,
    fromUserId: 1,
    toUserId: userId,
    content: '你好，请问这本书还在吗？',
    createTime: '2024-01-15 14:00:00'
  },
  {
    id: 2,
    fromUserId: userId,
    toUserId: 1,
    content: '在的，请问你是哪个学校的？',
    createTime: '2024-01-15 14:10:00'
  },
  {
    id: 3,
    fromUserId: 1,
    toUserId: userId,
    content: '复旦大学的，方便面交吗？',
    createTime: '2024-01-15 14:15:00'
  },
  {
    id: 4,
    fromUserId: userId,
    toUserId: 1,
    content: '好的，明天交易',
    createTime: '2024-01-15 14:30:00'
  }
]

const isMyMessage = (msg: Message) => {
  return msg.fromUserId === 1
}

const onSend = () => {
  if (!inputMessage.value.trim()) return

  const newMessage: Message = {
    id: Date.now(),
    fromUserId: 1,
    toUserId: userId,
    content: inputMessage.value.trim(),
    createTime: new Date().toISOString()
  }

  messages.value.push(newMessage)
  inputMessage.value = ''

  nextTick(() => {
    if (messageListRef.value) {
      messageListRef.value.scrollTop = messageListRef.value.scrollHeight
    }
  })
}

onMounted(() => {
  messages.value = mockMessages
  nextTick(() => {
    if (messageListRef.value) {
      messageListRef.value.scrollTop = messageListRef.value.scrollHeight
    }
  })
})
</script>

<template>
  <div class="chat-page">
    <van-nav-bar
      :title="otherUser.nickname"
      left-text="返回"
      left-arrow
      @click-left="router.back()"
    />

    <div ref="messageListRef" class="message-list">
      <div
        v-for="msg in messages"
        :key="msg.id"
        class="message-item"
        :class="{ 'my-message': isMyMessage(msg) }"
      >
        <van-image
          v-if="!isMyMessage(msg)"
          :src="otherUser.avatar"
          round
          width="36"
          height="36"
          class="avatar"
        />
        <div class="message-content">
          <div class="bubble" :class="{ 'my-bubble': isMyMessage(msg) }">
            {{ msg.content }}
          </div>
          <div class="message-time text-secondary text-small">
            {{ formatTime(msg.createTime) }}
          </div>
        </div>
        <van-image
          v-if="isMyMessage(msg)"
          src="https://picsum.photos/40/40?random=200"
          round
          width="36"
          height="36"
          class="avatar"
        />
      </div>
    </div>

    <div class="input-bar">
      <van-field
        v-model="inputMessage"
        placeholder="请输入消息..."
        @keyup.enter="onSend"
      >
        <template #button>
          <van-button size="small" type="primary" @click="onSend">发送</van-button>
        </template>
      </van-field>
    </div>
  </div>
</template>

<style scoped>
.chat-page {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f5f5f5;
}

.message-list {
  flex: 1;
  overflow-y: auto;
  padding: 12px;
  padding-bottom: env(safe-area-inset-bottom);
}

.message-item {
  display: flex;
  gap: 10px;
  margin-bottom: 16px;
  align-items: flex-start;
}

.message-item.my-message {
  flex-direction: row-reverse;
}

.avatar {
  flex-shrink: 0;
}

.message-content {
  max-width: 70%;
}

.bubble {
  padding: 10px 14px;
  background: #fff;
  border-radius: 8px;
  font-size: 14px;
  line-height: 1.5;
  word-break: break-word;
}

.my-bubble {
  background: #FF6B35;
  color: #fff;
}

.message-time {
  margin-top: 4px;
  text-align: right;
}

.input-bar {
  padding: 8px 12px;
  background: #fff;
  border-top: 1px solid #ebedf0;
  padding-bottom: calc(8px + env(safe-area-inset-bottom));
}

.input-bar :deep(.van-field) {
  padding: 6px 8px;
  background: #f5f5f5;
  border-radius: 20px;
}

.input-bar :deep(.van-button) {
  background: #FF6B35;
  border: none;
}
</style>
