<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Image as VanImage } from 'vant'
import MainLayout from '@/layouts/MainLayout.vue'
import EmptyState from '@/components/EmptyState.vue'
import { formatTimeAgo } from '@/utils/format'
import type { Conversation } from '@/types'

const router = useRouter()

const conversations = ref<Conversation[]>([])

const mockConversations: Conversation[] = [
  {
    userId: 2,
    userName: '小明同学',
    userAvatar: 'https://picsum.photos/40/40?random=100',
    lastMessage: '好的，明天交易',
    lastMessageTime: '2024-01-15 14:30:00',
    unreadCount: 2
  },
  {
    userId: 3,
    userName: '学霸小李',
    userAvatar: 'https://picsum.photos/40/40?random=101',
    lastMessage: '价格可以再商量',
    lastMessageTime: '2024-01-14 18:20:00',
    unreadCount: 0
  }
]

const onConversationClick = (conv: Conversation) => {
  router.push(`/message/chat/${conv.userId}`)
}

onMounted(() => {
  conversations.value = mockConversations
})
</script>

<template>
  <MainLayout>
    <div class="page-container">
      <van-nav-bar title="消息中心" left-text="返回" left-arrow @click-left="router.back()" />

      <div v-if="conversations.length === 0" class="empty-wrap">
        <EmptyState message="暂无消息" />
      </div>

      <div v-else class="conversation-list">
        <div
          v-for="conv in conversations"
          :key="conv.userId"
          class="conversation-item"
          @click="onConversationClick(conv)"
        >
          <div class="avatar-wrap">
            <van-image :src="conv.userAvatar" round width="48" height="48" />
            <span v-if="conv.unreadCount > 0" class="unread-badge">
              {{ conv.unreadCount > 99 ? '99+' : conv.unreadCount }}
            </span>
          </div>
          <div class="conversation-info">
            <div class="info-header">
              <span class="user-name">{{ conv.userName }}</span>
              <span class="time text-secondary text-small">{{ formatTimeAgo(conv.lastMessageTime) }}</span>
            </div>
            <div class="last-message text-secondary text-small">
              {{ conv.lastMessage }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </MainLayout>
</template>

<style scoped>
.conversation-list {
  background: #fff;
}

.conversation-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 16px;
  border-bottom: 1px solid #ebedf0;
}

.avatar-wrap {
  position: relative;
  flex-shrink: 0;
}

.unread-badge {
  position: absolute;
  top: -4px;
  right: -4px;
  min-width: 16px;
  height: 16px;
  background: #ee0a24;
  color: #fff;
  font-size: 10px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 4px;
}

.conversation-info {
  flex: 1;
  min-width: 0;
}

.info-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}

.user-name {
  font-size: 15px;
  font-weight: 500;
}

.last-message {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.empty-wrap {
  padding: 60px 0;
}
</style>
