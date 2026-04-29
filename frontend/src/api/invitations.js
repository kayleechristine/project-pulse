import api from '../plugins/axios'

export function sendInvitation(email, role) {
  return api.post('/api/invitations', { email, role })
}
