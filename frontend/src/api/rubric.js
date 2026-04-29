const API_URL = `${import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'}/api/rubrics`

function authHeaders(extra = {}) {
  const token = localStorage.getItem('token')
  return token ? { Authorization: `Bearer ${token}`, ...extra } : { ...extra }
}

export async function getRubrics() {
  const response = await fetch(API_URL, { headers: authHeaders() })

  if (!response.ok) {
    throw new Error('Failed to fetch rubrics')
  }

  return response.json()
}

export async function getRubric(id) {
  const response = await fetch(`${API_URL}/${id}`, { headers: authHeaders() })

  if (!response.ok) {
    throw new Error('Failed to fetch rubric')
  }

  return response.json()
}

export async function createRubric(rubric) {
  const response = await fetch(API_URL, {
    method: 'POST',
    headers: authHeaders({ 'Content-Type': 'application/json' }),
    body: JSON.stringify(rubric),
  })

  if (!response.ok) {
    throw new Error('Failed to create rubric')
  }

  return response.json()
}

export async function updateRubric(id, rubric) {
  const response = await fetch(`${API_URL}/${id}`, {
    method: 'PUT',
    headers: authHeaders({ 'Content-Type': 'application/json' }),
    body: JSON.stringify(rubric),
  })

  if (!response.ok) {
    throw new Error('Failed to update rubric')
  }

  return response.json()
}