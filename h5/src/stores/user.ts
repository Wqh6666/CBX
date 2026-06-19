import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { User } from '@/types'
import storage from '@/utils/storage'

export const useUserStore = defineStore('user', () => {
  const token = ref<string>(storage.get('token') || '')
  const userInfo = ref<User | null>(storage.get('userInfo') || null)

  const setToken = (newToken: string) => {
    token.value = newToken
    storage.set('token', newToken)
  }

  const setUserInfo = (info: User) => {
    userInfo.value = info
    storage.set('userInfo', info)
  }

  const login = (newToken: string, info: User) => {
    setToken(newToken)
    setUserInfo(info)
  }

  const logout = () => {
    token.value = ''
    userInfo.value = null
    storage.remove('token')
    storage.remove('userInfo')
  }

  const isLoggedIn = () => {
    return !!token.value
  }

  return {
    token,
    userInfo,
    setToken,
    setUserInfo,
    login,
    logout,
    isLoggedIn
  }
})
