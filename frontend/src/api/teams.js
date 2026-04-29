import api from '../plugins/axios'

export function getTeams() {
  return api.get('/api/teams')
}

export function getTeam(id) {
  return api.get(`/api/teams/${id}`)
}

export function createTeam(payload) {
  return api.post('/api/teams', payload)
}

export function updateTeam(id, payload) {
  return api.put(`/api/teams/${id}`, payload)
}

export function deleteTeam(id) {
  return api.delete(`/api/teams/${id}`)
}

export function assignStudentsToTeam(teamId, studentIds) {
  return api.post(`/api/teams/${teamId}/students`, { studentIds })
}

export function removeStudentFromTeam(teamId, studentId) {
  return api.delete(`/api/teams/${teamId}/students/${studentId}`)
}