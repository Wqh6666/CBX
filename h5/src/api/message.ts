import { get, post } from '@/utils/request'
import type { Message, Conversation } from '@/types'

export const getConversations = () => {
  return get('/user/v1/messages/conversations')
}

export const getMessages = (userId: number, params?: { page?: number; size?: number }) => {
  return get(`/user/v1/messages/${userId}`, params)
}

export const sendMessage = (data: { toUserId: number; content: string }) => {
  return post('/user/v1/messages', data)
}
