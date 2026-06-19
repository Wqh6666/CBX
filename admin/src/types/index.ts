export interface Admin {
  id: number
  username: string
  nickname: string
  avatar: string
  status: number
}

export interface User {
  id: number
  phone: string
  nickname: string
  avatar: string
  gender: number
  email: string
  status: number
  createTime: string
}

export interface Book {
  id: number
  title: string
  author: string
  isbn: string
  publisher: string
  categoryId: number
  categoryName: string
  coverImage: string
  images: string[]
  description: string
  originalPrice: number
  price: number
  condition: number
  conditionName: string
  status: number
  sellerId: number
  sellerName: string
  sellerAvatar: string
  viewCount: number
  collectCount: number
  rejectReason: string
  createTime: string
}

export interface Order {
  id: number
  orderNo: string
  bookId: number
  book: Book
  buyerId: number
  sellerId: number
  amount: number
  status: number
  statusDesc: string
  payTime: string
  payMethod: string
  expressNo: string
  expressCompany: string
  shipTime: string
  receiveTime: string
  addressId: number
  address: Address
  createTime: string
}

export interface Category {
  id: number
  name: string
  parentId: number
  level: number
  sort: number
  status: number
}

export interface DashboardStats {
  totalUsers: number
  activeUsers: number
  totalBooks: number
  onSaleBooks: number
  pendingBooks: number
  totalOrders: number
  completedOrders: number
  pendingOrders: number
  totalAmount: number
}
