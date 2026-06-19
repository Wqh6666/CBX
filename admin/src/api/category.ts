import { get, post, put, del } from '@/utils/request'
import type { Category } from '@/types'

export const getCategories = () => {
  return get('/admin/v1/categories')
}

export const createCategory = (data: Partial<Category>) => {
  return post('/admin/v1/categories', data)
}

export const updateCategory = (id: number, data: Partial<Category>) => {
  return put(`/admin/v1/categories/${id}`, data)
}

export const deleteCategory = (id: number) => {
  return del(`/admin/v1/categories/${id}`)
}
