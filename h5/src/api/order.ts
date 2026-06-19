import { get, post, put } from '@/utils/request'
import type { Order } from '@/types'

export const createOrder = (data: {
  bookId: number
  addressId: number
  quantity: number
}) => {
  return post('/user/v1/orders', data)
}

export const getOrders = (params: {
  status?: number
  page?: number
  size?: number
}) => {
  return get('/user/v1/orders', params)
}

export const getOrderDetail = (id: number) => {
  return get(`/user/v1/orders/${id}`)
}

export const payOrder = (id: number) => {
  return post(`/user/v1/orders/${id}/pay`)
}

export const receiveOrder = (id: number) => {
  return post(`/user/v1/orders/${id}/receive`)
}

export const cancelOrder = (id: number, reason: string) => {
  return post(`/user/v1/orders/${id}/cancel`, { reason })
}
