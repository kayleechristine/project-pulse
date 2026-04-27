import { defineStore } from 'pinia'
import { login as apiLogin } from '../api/auth'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token') || null,
    user: JSON.parse(localStorage.getItem('user')) || null,
  }),

  getters: {
    isLoggedIn: (state) => !!state.token,
    role: (state) => state.user?.roles?.[0] ?? null,
  },

  actions: {
    async login(email, password) {
      const response = await apiLogin(email, password)
      const data = response.data.data

      this.token = data.token
      this.user = {
        id: data.userId,
        email: data.email,
        firstName: data.firstName,
        lastName: data.lastName,
        roles: data.roles,
      }

      localStorage.setItem('token', this.token)
      localStorage.setItem('user', JSON.stringify(this.user))
    },

    logout() {
      this.token = null
      this.user = null
      localStorage.removeItem('token')
      localStorage.removeItem('user')
    },
  },
})
