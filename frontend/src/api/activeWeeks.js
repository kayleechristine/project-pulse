const API_URL = 'http://localhost:8080/api/active-weeks'

export async function getActiveWeeks(sectionId) {
  const response = await fetch(`${API_URL}/section/${sectionId}`)
  return response.json()
}

export async function saveActiveWeeks(sectionId, activeWeeks) {
  const response = await fetch(`${API_URL}/section/${sectionId}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(activeWeeks)
  })
  return response.json()
}