import { get, post, put, del } from '@/utils/request'
import type { User } from '@/types'

export const login = (data: { phone: string; code: string }) => {
  return post('/user/v1/auth/login', data)
}

export const getUserInfo = () => {
  return get('/user/v1/profile')
}

export const updateUserInfo = (data: Partial<User>) => {
  return put('/user/v1/profile', data)
}

export const sendCode = (phone: string) => {
  return post('/user/v1/auth/send-code', { phone })
}
