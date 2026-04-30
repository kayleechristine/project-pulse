import api from '../plugins/axios'

export async function getRubrics() {
  const response = await api.get('/api/rubrics')
  return response.data || response
}

export async function getRubric(id) {
  const response = await api.get(`/api/rubrics/${id}`)
  return response.data || response
}

export async function createRubric(rubric) {
  const response = await api.post('/api/rubrics', rubric)
  return response.data || response
}

export async function updateRubric(id, rubric) {
  const response = await api.put(`/api/rubrics/${id}`, rubric)
  return response.data || response
}