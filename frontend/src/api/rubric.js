const API_URL = 'http://localhost:8080/api/rubrics'

export async function getRubrics() {
  const response = await fetch(API_URL)
  return response.json()
}

export async function createRubric(rubric) {
  const response = await fetch(API_URL, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(rubric)
  })
  return response.json()
}