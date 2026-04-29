import api from '../plugins/axios'

export function getSectionPeerEvalReport(sectionId, weekId) {
  return api.get('/api/reports/peer-evaluations/section', { params: { sectionId, weekId } })
}

export function getStudentPeerEvalReport(studentId, startDate, endDate) {
  return api.get('/api/reports/peer-evaluations/student', { params: { studentId, startDate, endDate } })
}

export function getStudentWarReport(studentId, startDate, endDate) {
  return api.get('/api/reports/war/student', { params: { studentId, startDate, endDate } })
}
