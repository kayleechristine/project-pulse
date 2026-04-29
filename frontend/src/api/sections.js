import api from '../plugins/axios'

export function getSections() {
  return api.get('/api/sections')
}

export function getSection(id) {
  return api.get(`/api/sections/${id}`)
}

export function createSection(payload) {
  return api.post('/api/sections', payload)
}

export function updateSection(id, payload) {
  return api.put(`/api/sections/${id}`, payload)
}

export function deleteSection(id) {
  return api.delete(`/api/sections/${id}`)
}