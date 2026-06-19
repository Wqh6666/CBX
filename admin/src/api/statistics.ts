import { get } from '@/utils/request'
import type { DashboardStats } from '@/types'

export const getDashboard = () => {
  return get('/admin/v1/statistics/dashboard')
}
