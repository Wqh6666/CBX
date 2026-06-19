import { get, post, del } from '@/utils/request'
import type { CartItem } from '@/types'

export const getCart = () => {
  return get('/user/v1/cart')
}

export const addToCart = (data: { bookId: number; quantity: number }) => {
  return post('/user/v1/cart', data)
}

export const removeFromCart = (ids: string) => {
  return del(`/user/v1/cart/${ids}`)
}

export const clearCart = () => {
  return del('/user/v1/cart/clear')
}
