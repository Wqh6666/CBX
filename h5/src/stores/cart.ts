import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { CartItem } from '@/types'

export const useCartStore = defineStore('cart', () => {
  const items = ref<CartItem[]>([])
  const totalPrice = ref(0)

  const updateCart = (newItems: CartItem[]) => {
    items.value = newItems
    totalPrice.value = newItems.reduce((sum, item) => sum + item.book.price * item.quantity, 0)
  }

  const addItem = (item: CartItem) => {
    const existing = items.value.find(i => i.bookId === item.bookId)
    if (existing) {
      existing.quantity += item.quantity
    } else {
      items.value.push(item)
    }
    recalcTotal()
  }

  const removeItem = (bookId: number) => {
    items.value = items.value.filter(i => i.bookId !== bookId)
    recalcTotal()
  }

  const clearCart = () => {
    items.value = []
    totalPrice.value = 0
  }

  const recalcTotal = () => {
    totalPrice.value = items.value.reduce((sum, item) => sum + item.book.price * item.quantity, 0)
  }

  const getCount = () => {
    return items.value.reduce((sum, item) => sum + item.quantity, 0)
  }

  return {
    items,
    totalPrice,
    updateCart,
    addItem,
    removeItem,
    clearCart,
    getCount
  }
})
