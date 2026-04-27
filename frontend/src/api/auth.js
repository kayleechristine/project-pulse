import api from '../plugins/axios'

export function validateInviteToken(token) {
  return api.get('/api/invitations/validate', { params: { token } })
}

export function register(token, firstName, lastName, password) {
  return api.post('/api/auth/register', { token, firstName, lastName, password })
}

export function login(email, password) {
  return api.post('/api/auth/login', { email, password })
}
