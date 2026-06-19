import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Admin } from '@/types'

export const useAdminStore = defineStore('admin', () => {
  const token = ref<string>(localStorage.getItem('admin_token') || '')
  const adminInfo = ref<Admin | null>(null)

  const setToken = (newToken: string) => {
    token.value = newToken
    localStorage.setItem('admin_token', newToken)
  }

  const setAdminInfo = (info: Admin) => {
    adminInfo.value = info
  }

  const login = (newToken: string, info: Admin) => {
    setToken(newToken)
    setAdminInfo(info)
  }

  const logout = () => {
    token.value = ''
    adminInfo.value = null
    localStorage.removeItem('admin_token')
  }

  const isLoggedIn = () => {
    return !!token.value
  }

  return {
    token,
    adminInfo,
    setToken,
    setAdminInfo,
    login,
    logout,
    isLoggedIn
  }
})
