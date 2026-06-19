import axios, { AxiosError } from 'axios'

const BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'

const instance = axios.create({
  baseURL: BASE_URL,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
instance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
instance.interceptors.response.use(
  (response) => {
    return response.data
  },
  (error: AxiosError) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      window.location.href = '/pages/user/login'
    }
    return Promise.reject(error)
  }
)

export const get = <T = any>(url: string, params?: any): Promise<T> => {
  return instance.get(url, { params })
}

export const post = <T = any>(url: string, data?: any): Promise<T> => {
  return instance.post(url, data)
}

export const put = <T = any>(url: string, data?: any): Promise<T> => {
  return instance.put(url, data)
}

export const del = <T = any>(url: string, data?: any): Promise<T> => {
  return instance.delete(url, { data })
}

export default instance
