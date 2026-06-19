import { get, post, put, del } from '@/utils/request'
import type { Book } from '@/types'

export const getBooks = (params: {
  categoryId?: number
  keyword?: string
  page?: number
  size?: number
  sort?: string
}) => {
  return get('/user/v1/books', params)
}

export const getBookDetail = (id: number) => {
  return get(`/user/v1/books/${id}`)
}

export const publishBook = (data: Partial<Book>) => {
  return post('/user/v1/books', data)
}

export const updateBook = (id: number, data: Partial<Book>) => {
  return put(`/user/v1/books/${id}`, data)
}

export const getMyBooks = () => {
  return get('/user/v1/books/my')
}

export const getCategories = () => {
  return get('/user/v1/categories')
}
