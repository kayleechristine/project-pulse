const API_BASE_URL = 'http://localhost:8080/api/sections'

async function handleResponse(response, errorMessage) {
  if (!response.ok) {
    throw new Error(errorMessage)
  }

  if (response.status === 204) {
    return null
  }

  const data = await response.json()

  // Supports either plain arrays/objects OR backend wrappers like { data: ... }
  return data.data ?? data
}

export async function getSections() {
  const response = await fetch(API_BASE_URL)
  return handleResponse(response, 'Failed to fetch sections')
}

export async function getSection(id) {
  const response = await fetch(`${API_BASE_URL}/${id}`)
  return handleResponse(response, 'Failed to fetch section')
}

export async function createSection(section) {
  const response = await fetch(API_BASE_URL, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(section),
  })

  return handleResponse(response, 'Failed to create section')
}

export async function updateSection(id, section) {
  const response = await fetch(`${API_BASE_URL}/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(section),
  })

  return handleResponse(response, 'Failed to update section')
}

export async function deleteSection(id) {
  const response = await fetch(`${API_BASE_URL}/${id}`, {
    method: 'DELETE',
  })

  return handleResponse(response, 'Failed to delete section')
}