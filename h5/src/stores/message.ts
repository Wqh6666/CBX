import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Conversation } from '@/types'

export const useMessageStore = defineStore('message', () => {
  const conversations = ref<Conversation[]>([])
  const totalUnread = ref(0)

  const setConversations = (list: Conversation[]) => {
    conversations.value = list
    totalUnread.value = list.reduce((sum, c) => sum + c.unreadCount, 0)
  }

  const updateUnread = (userId: number, count: number) => {
    const conv = conversations.value.find(c => c.userId === userId)
    if (conv) {
      conv.unreadCount = count
      totalUnread.value = conversations.value.reduce((sum, c) => sum + c.unreadCount, 0)
    }
  }

  const clearUnread = (userId: number) => {
    updateUnread(userId, 0)
  }

  return {
    conversations,
    totalUnread,
    setConversations,
    updateUnread,
    clearUnread
  }
})
