import { get, put } from '@/utils/request'

export const getOrders = (params: {
  page?: number
  size?: number
  status?: number
}) => {
  return get('/admin/v1/orders', params)
}

export const shipOrder = (id: number, data: { expressNo: string; expressCompany: string }) => {
  return put(`/admin/v1/orders/${id}/ship`, data)
}

export const refundOrder = (id: number) => {
  return put(`/admin/v1/orders/${id}/refund`, {})
}
