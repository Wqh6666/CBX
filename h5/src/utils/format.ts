export const formatPrice = (price: number | string): string => {
  const num = typeof price === 'string' ? parseFloat(price) : price
  return `¥${num.toFixed(2)}`
}

export const formatDate = (dateStr: string): string => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

export const formatDateTime = (dateStr: string): string => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hour = String(date.getHours()).padStart(2, '0')
  const minute = String(date.getMinutes()).padStart(2, '0')
  return `${year}-${month}-${day} ${hour}:${minute}`
}

export const formatTimeAgo = (dateStr: string): string => {
  if (!dateStr) return ''
  const now = new Date()
  const date = new Date(dateStr)
  const diff = now.getTime() - date.getTime()

  const minute = 60 * 1000
  const hour = 60 * minute
  const day = 24 * hour

  if (diff < minute) {
    return '刚刚'
  } else if (diff < hour) {
    return Math.floor(diff / minute) + '分钟前'
  } else if (diff < day) {
    return Math.floor(diff / hour) + '小时前'
  } else if (diff < 7 * day) {
    return Math.floor(diff / day) + '天前'
  } else {
    return formatDate(dateStr)
  }
}

export const getConditionName = (condition: number): string => {
  const map: Record<number, string> = {
    1: '全新',
    2: '95新',
    3: '9成新',
    4: '8成新',
    5: '7成新及以下'
  }
  return map[condition] || '未知'
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

export const getOrderStatusColor = (status: number): string => {
  const map: Record<number, string> = {
    0: '#FF9800',
    1: '#2196F3',
    2: '#9C27B0',
    3: '#4CAF50',
    4: '#9E9E9E',
    5: '#FF5722',
    6: '#795548'
  }
  return map[status] || '#9E9E9E'
}
