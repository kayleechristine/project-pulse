const API_URL = 'http://localhost:8080/api/teams'

export async function getTeams() {
  const response = await fetch(API_URL)
  return response.json()
}

export async function getTeam(id) {
  const response = await fetch(`${API_URL}/${id}`)
  return response.json()
}

export async function createTeam(team) {
  const response = await fetch(API_URL, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(team)
  })
  return response.json()
}

export async function updateTeam(id, team) {
  const response = await fetch(`${API_URL}/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(team)
  })
  return response.json()
}

export async function deleteTeam(id) {
  return fetch(`${API_URL}/${id}`, {
    method: 'DELETE'
  })
}