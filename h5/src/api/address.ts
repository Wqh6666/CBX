import { get, post, put, del } from '@/utils/request'
import type { Address } from '@/types'

export const getAddresses = () => {
  return get('/user/v1/addresses')
}

export const addAddress = (data: Partial<Address>) => {
  return post('/user/v1/addresses', data)
}

export const updateAddress = (id: number, data: Partial<Address>) => {
  return put(`/user/v1/addresses/${id}`, data)
}

export const deleteAddress = (id: number) => {
  return del(`/user/v1/addresses/${id}`)
}
