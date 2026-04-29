import api from '../plugins/axios'

export function getRubrics() {
  return api.get('/api/rubrics')
}

export function createRubric(rubric) {
  return api.post('/api/rubrics', rubric)
}
