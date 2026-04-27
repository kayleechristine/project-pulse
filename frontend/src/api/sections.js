const API_URL = 'http://localhost:8080/api/sections'

export async function getSections() {
  const response = await fetch(API_URL)
  return response.json()
}

export async function getSection(id) {
  const response = await fetch(`${API_URL}/${id}`)
  return response.json()
}

export async function createSection(section) {
  const response = await fetch(API_URL, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(section)
  })
  return response.json()
}

export async function updateSection(id, section) {
  const response = await fetch(`${API_URL}/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(section)
  })
  return response.json()
}