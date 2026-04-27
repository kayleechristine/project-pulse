import api from '../plugins/axios'

export function submitEvaluation(payload) {
  return api.post('/api/peer-evaluations', payload)
}

export function getMyReport(weekId) {
  return api.get('/api/peer-evaluations/report', { params: { weekId } })
}
