<template>
  <div>
    <div class="mb-6">
      <div class="text-h3 font-weight-bold">Team Details</div>
      <div class="text-h6 text-medium-emphasis">
        View and manage a senior design team.
      </div>
    </div>

    <v-alert v-if="error" type="error" variant="tonal" class="mb-4">
      {{ error }}
    </v-alert>

    <v-card v-if="team" rounded="xl" elevation="1" class="pa-6">
      <div class="text-h4 font-weight-bold mb-2">{{ team.name }}</div>
      <div class="text-body-1 mb-4">{{ team.description }}</div>

      <div class="mb-2">
        <strong>Website:</strong>
        <span class="ml-2">{{ team.websiteUrl || 'N/A' }}</span>
      </div>

      <v-divider class="my-6" />

      <div class="text-h6 font-weight-bold mb-3">Assigned Instructors</div>
      <div class="d-flex ga-3 align-center mb-4">
        <v-select
          v-model="selectedInstructorIds"
          :items="instructors"
          :item-title="instructorLabel"
          item-value="id"
          label="Add instructors"
          multiple
          chips
          variant="outlined"
          density="comfortable"
          hide-details
          style="max-width: 520px"
        />
        <v-btn color="primary" :disabled="selectedInstructorIds.length === 0" @click="assignInstructors">
          Assign
        </v-btn>
      </div>

      <v-table density="compact" class="mb-4">
        <tbody>
          <tr v-for="instructorId in team.instructorIds || []" :key="instructorId">
            <td>{{ instructorName(instructorId) }}</td>
            <td class="text-right">
              <v-btn size="small" variant="text" color="error" @click="removeInstructor(instructorId)">
                Remove
              </v-btn>
            </td>
          </tr>
          <tr v-if="!team.instructorIds || team.instructorIds.length === 0">
            <td class="text-medium-emphasis pa-4">No instructors assigned.</td>
          </tr>
        </tbody>
      </v-table>

      <div class="d-flex ga-2 mt-6">
        <v-btn color="primary" :to="`/teams/${team.id}/edit`">Edit Team</v-btn>
        <v-btn color="error" variant="tonal" @click="removeTeam">Delete Team</v-btn>
        <v-btn variant="outlined" to="/teams">Back</v-btn>
      </div>
    </v-card>

    <v-card v-else rounded="xl" elevation="1" class="pa-6">
      Loading team...
    </v-card>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { assignInstructorsToTeam, deleteTeam, getTeam, removeInstructorFromTeam } from '../../api/teams'
import { searchInstructors } from '../../api/instructors'

const route = useRoute()
const router = useRouter()

const team = ref(null)
const error = ref('')
const instructors = ref([])
const selectedInstructorIds = ref([])

async function loadTeam() {
  try {
    team.value = await getTeam(route.params.id)
  } catch (err) {
    error.value = err.message
  }
}

async function loadInstructors() {
  try {
    const response = await searchInstructors('')
    instructors.value = response.data.data
  } catch {
    instructors.value = []
  }
}

function instructorLabel(instructor) {
  return `${instructor.firstName ?? ''} ${instructor.lastName ?? ''}`.trim() || instructor.email
}

function instructorName(id) {
  const instructor = instructors.value.find((item) => item.id === id)
  return instructor ? instructorLabel(instructor) : `Instructor ${id}`
}

async function assignInstructors() {
  try {
    team.value = await assignInstructorsToTeam(route.params.id, selectedInstructorIds.value)
    selectedInstructorIds.value = []
  } catch (err) {
    error.value = err.message
  }
}

async function removeInstructor(instructorId) {
  try {
    team.value = await removeInstructorFromTeam(route.params.id, instructorId)
  } catch (err) {
    error.value = err.message
  }
}

async function removeTeam() {
  if (!confirm(`Delete "${team.value.name}" permanently?`)) {
    return
  }

  try {
    await deleteTeam(route.params.id)
    alert('Team deleted successfully')
    router.push('/teams')
  } catch (err) {
    error.value = err.message
  }
}

onMounted(() => {
  loadTeam()
  loadInstructors()
})
</script>
