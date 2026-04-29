const TEAM_API_URL = 'http://localhost:8080/api/teams'

async function handleResponse(response, errorMessage) {
  if (!response.ok) {
    throw new Error(errorMessage)
  }

  if (response.status === 204) {
    return null
  }

  const data = await response.json()
  return data.data ?? data
}

export async function getTeams() {
  const response = await fetch(TEAM_API_URL)
  return handleResponse(response, 'Failed to fetch teams')
}

export async function getTeam(id) {
  const response = await fetch(`${TEAM_API_URL}/${id}`)
  return handleResponse(response, 'Failed to fetch team')
}

export async function createTeam(team) {
  const response = await fetch(TEAM_API_URL, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(team),
  })

  return handleResponse(response, 'Failed to create team')
}

export async function updateTeam(id, team) {
  const response = await fetch(`${TEAM_API_URL}/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(team),
  })

  return handleResponse(response, 'Failed to update team')
}

export async function deleteTeam(id) {
  const response = await fetch(`${TEAM_API_URL}/${id}`, {
    method: 'DELETE',
  })

  return handleResponse(response, 'Failed to delete team')
}

export async function getMyTeam() {
  return {
    id: 1,
    name: 'Demo Team',
    description: 'Senior design project team',
    websiteUrl: 'https://example.com',
    studentIds: [],
  }
}

export async function assignStudentsToTeam(teamId, studentIds) {
  const response = await fetch(`${TEAM_API_URL}/${teamId}/students`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(studentIds),
  })
  

  

  return handleResponse(response, 'Failed to assign students to team')
}

export async function removeStudentFromTeam(teamId, studentId) {
  const response = await fetch(`${TEAM_API_URL}/${teamId}/students/${studentId}`, {
    method: 'DELETE',
  })

  return handleResponse(response, 'Failed to remove student from team')
}

export async function assignInstructorsToTeam(teamId, instructorIds) {
  const response = await fetch(`${TEAM_API_URL}/${teamId}/instructors`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ instructorIds }),
  })

  return handleResponse(response, 'Failed to assign instructors to team')
}

export async function removeInstructorFromTeam(teamId, instructorId) {
  const response = await fetch(`${TEAM_API_URL}/${teamId}/instructors/${instructorId}`, {
    method: 'DELETE',
  })

  return handleResponse(response, 'Failed to remove instructor from team')
}
