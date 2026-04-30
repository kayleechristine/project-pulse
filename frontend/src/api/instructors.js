import api from '../plugins/axios'

export function inviteInstructor(email) {
  return api.post('/api/instructors/invitations', { email })
}

export function searchInstructors(search = '') {
  return api.get('/api/instructors', { params: { search } })
}

export function getInstructor(id) {
  return api.get(`/api/instructors/${id}`)
}

export function deactivateInstructor(id) {
  return api.patch(`/api/instructors/${id}/deactivate`)
}

export function reactivateInstructor(id) {
  return api.patch(`/api/instructors/${id}/reactivate`)
}
