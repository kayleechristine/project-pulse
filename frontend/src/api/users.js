import api from '../plugins/axios'

export function getProfile() {
  return api.get('/api/users/me')
}

export function updateProfile(payload) {
  return api.put('/api/users/me', payload)
}
