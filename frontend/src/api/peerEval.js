import api from '../plugins/axios'

export function submitEvaluation(payload) {
  return api.post('/api/peer-evaluations', payload)
}

export function getMySubmissions(weekId) {
  return api.get('/api/peer-evaluations/my-submissions', { params: { weekId } })
}

export function getMyReport(weekId) {
  return api.get('/api/peer-evaluations/report', { params: { weekId } })
}
