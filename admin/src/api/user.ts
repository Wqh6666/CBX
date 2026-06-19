import { get, put } from '@/utils/request'
import type { User } from '@/types'

export const getUsers = (params: {
  page?: number
  size?: number
  status?: number
  keyword?: string
}) => {
  return get('/admin/v1/users', params)
}

export const updateUserStatus = (id: number, status: number) => {
  return put(`/admin/v1/users/${id}/status`, { status })
}
