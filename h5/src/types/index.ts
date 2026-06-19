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
  cancelTime: string
  cancelReason: string
  addressId: number
  address: Address
  createTime: string
}

export interface Address {
  id: number
  name: string
  phone: string
  province: string
  city: string
  district: string
  detail: string
  isDefault: number
}

export interface Category {
  id: number
  name: string
  parentId: number
  level: number
  sort: number
  children?: Category[]
}

export interface CartItem {
  id: number
  userId: number
  bookId: number
  book: Book
  quantity: number
  createTime: string
}

export interface Collect {
  id: number
  userId: number
  bookId: number
  book: Book
  createTime: string
}

export interface Message {
  id: number
  fromUserId: number
  toUserId: number
  content: string
  createTime: string
}

export interface Conversation {
  userId: number
  userName: string
  userAvatar: string
  lastMessage: string
  lastMessageTime: string
  unreadCount: number
}

export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}
