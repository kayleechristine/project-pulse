import api from '../plugins/axios'

export function getActivities(weekId) {
  return api.get('/api/war-activities', { params: { weekId } })
}

export function addActivity(payload) {
  return api.post('/api/war-activities', payload)
}

export function updateActivity(id, payload) {
  return api.put(`/api/war-activities/${id}`, payload)
}

export function deleteActivity(id) {
  return api.delete(`/api/war-activities/${id}`)
}

export function getTeamWarReport(teamId, weekId) {
  return api.get('/api/reports/war', { params: { teamId, weekId } })
}
