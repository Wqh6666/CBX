import { get, post, put, del } from '@/utils/request'

export const login = (data: { username: string; password: string }) => {
  return post('/admin/v1/auth/login', data)
}

export const getAdminInfo = () => {
  return get('/admin/v1/auth/info')
}

export const logout = () => {
  return post('/admin/v1/auth/logout')
}
