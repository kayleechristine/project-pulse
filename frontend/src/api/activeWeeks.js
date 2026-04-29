import api from '../plugins/axios'

export function getActiveWeeksBySection(sectionId) {
  return api.get(`/api/active-weeks/section/${sectionId}`)
}

export function saveActiveWeeksBySection(sectionId, weeks) {
  return api.post(`/api/active-weeks/section/${sectionId}`, weeks)
}