import { get, post, del } from '@/utils/request'
import type { Collect } from '@/types'

export const getCollects = () => {
  return get('/user/v1/collects')
}

export const addCollect = (bookId: number) => {
  return post('/user/v1/collects', { bookId })
}

export const removeCollect = (bookId: number) => {
  return del(`/user/v1/collects/${bookId}`)
}
