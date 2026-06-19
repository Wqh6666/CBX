export const formatPrice = (price: number | string): string => {
  const num = typeof price === 'string' ? parseFloat(price) : price
  return `¥${num.toFixed(2)}`
}

export const formatDate = (dateStr: string): string => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

export const formatDateTime = (dateStr: string): string => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hour = String(date.getHours()).padStart(2, '0')
  const minute = String(date.getMinutes()).padStart(2, '0')
  return `${year}-${month}-${day} ${hour}:${minute}`
}

export const getBookStatusName = (status: number): string => {
  const map: Record<number, string> = {
    0: '待审核',
    1: '上架中',
    2: '已下架',
    3: '违规下架'
  }
  return map[status] || '未知'
}

export const getBookStatusType = (status: number): string => {
  const map: Record<number, string> = {
    0: 'warning',
    1: 'success',
    2: 'info',
    3: 'danger'
  }
  return map[status] || 'info'
}

export const getOrderStatusName = (status: number): string => {
  const map: Record<number, string> = {
    0: '待付款',
    1: '待发货',
    2: '待收货',
    3: '已完成',
    4: '已取消',
    5: '退款中',
    6: '已退款'
  }
  return map[status] || '未知'
}

export const getOrderStatusType = (status: number): string => {
  const map: Record<number, string> = {
    0: 'warning',
    1: 'primary',
    2: 'info',
    3: 'success',
    4: 'info',
    5: 'warning',
    6: 'danger'
  }
  return map[status] || 'info'
}
