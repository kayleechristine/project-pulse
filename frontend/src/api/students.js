import api from '../plugins/axios'

export function inviteStudent(email, sectionId = null) {
  return api.post('/api/students/invitations', { email, sectionId })
}

export function searchStudents(search = '') {
  return api.get('/api/students', { params: { search } })
}

export function getStudent(id) {
  return api.get(`/api/students/${id}`)
}

export function deleteStudent(id) {
  return api.delete(`/api/students/${id}`)
}
