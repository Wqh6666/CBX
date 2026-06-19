import { get, put } from '@/utils/request'
import type { Book } from '@/types'

export const getBooks = (params: {
  page?: number
  size?: number
  status?: number
  keyword?: string
}) => {
  return get('/admin/v1/books', params)
}

export const getPendingBooks = (params: { page?: number; size?: number }) => {
  return get('/admin/v1/books/pending', params)
}

export const auditBook = (id: number, data: { status: number; reason?: string }) => {
  return put(`/admin/v1/books/${id}/audit`, data)
}

export const offshelfBook = (id: number, data: { reason: string }) => {
  return put(`/admin/v1/books/${id}/offshelf`, data)
}
