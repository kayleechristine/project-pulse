<template>
  <div>
    <div class="mb-6">
      <div class="text-h3 font-weight-bold">Team Details</div>
      <div class="text-h6 text-medium-emphasis">
        View and manage a senior design team.
      </div>
    </div>

    <v-snackbar v-model="snackbar.show" :color="snackbar.color" location="bottom" timeout="4000">
      {{ snackbar.message }}
    </v-snackbar>

    <v-card v-if="team" rounded="xl" elevation="1" class="pa-6">
      <v-text-field
        v-model="editName"
        label="Team Name"
        variant="outlined"
        density="comfortable"
        class="mb-3"
        hide-details
      />
      <v-textarea
        v-model="editDescription"
        label="Description"
        variant="outlined"
        density="comfortable"
        rows="2"
        auto-grow
        class="mb-3"
        hide-details
      />
      <v-text-field
        v-model="editWebsiteUrl"
        label="Website URL"
        variant="outlined"
        density="comfortable"
        class="mb-4"
        hide-details
      />

      <v-divider class="my-6" />

      <div class="text-h6 font-weight-bold mb-3">Assigned Students</div>
      <div class="d-flex ga-3 align-center mb-4 flex-wrap">
        <v-select
          v-model="selectedStudentIds"
          :items="availableStudents"
          :item-title="studentLabel"
          item-value="id"
          label="Add or move students"
          multiple
          chips
          variant="outlined"
          density="comfortable"
          hide-details
          style="max-width: 520px"
        />
        <v-btn color="primary" :disabled="selectedStudentIds.length === 0" @click="assignStudents">
          Add / Move
        </v-btn>
      </div>

      <v-table density="compact" class="mb-6">
        <tbody>
          <tr v-for="studentId in team.studentIds || []" :key="studentId">
            <td>{{ studentName(studentId) }}</td>
            <td class="text-right">
              <v-btn size="small" variant="text" color="error" @click="removeStudent(studentId)">
                Remove
              </v-btn>
            </td>
          </tr>
          <tr v-if="!team.studentIds || team.studentIds.length === 0">
            <td class="text-medium-emphasis pa-4">No students assigned.</td>
          </tr>
        </tbody>
      </v-table>

      <div class="text-h6 font-weight-bold mb-3">Assigned Instructors</div>
      <div class="d-flex ga-3 align-center mb-4 flex-wrap">
        <v-select
          v-model="selectedInstructorIds"
          :items="availableInstructors"
          :item-title="instructorLabel"
          item-value="id"
          label="Add or move instructors"
          multiple
          chips
          variant="outlined"
          density="comfortable"
          hide-details
          style="max-width: 520px"
        />
        <v-btn color="primary" :disabled="selectedInstructorIds.length === 0" @click="assignInstructors">
          Add / Move
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
        <v-btn color="primary" :loading="saving" @click="saveTeam">Save</v-btn>
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
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  assignInstructorsToTeam,
  assignStudentsToTeam,
  deleteTeam,
  getTeam,
  removeInstructorFromTeam,
  removeStudentFromTeam,
  updateTeam,
} from '../../api/teams'
import { searchInstructors } from '../../api/instructors'
import { searchStudents } from '../../api/students'

const route = useRoute()
const router = useRouter()

const team = ref(null)
const saving = ref(false)
const snackbar = ref({ show: false, message: '', color: 'success' })

function notify(message, color = 'success') {
  snackbar.value = { show: true, message, color }
}
const editName = ref('')
const editDescription = ref('')
const editWebsiteUrl = ref('')
const instructors = ref([])
const students = ref([])
const selectedStudentIds = ref([])
const selectedInstructorIds = ref([])

const availableStudents = computed(() => {
  if (!team.value?.studentIds) return students.value
  return students.value.filter((student) => !team.value.studentIds.includes(student.id))
})

const availableInstructors = computed(() => {
  if (!team.value?.instructorIds) return instructors.value
  return instructors.value.filter((instructor) => !team.value.instructorIds.includes(instructor.id))
})

async function loadTeam() {
  try {
    team.value = await getTeam(route.params.id)
    editName.value = team.value.name ?? ''
    editDescription.value = team.value.description ?? ''
    editWebsiteUrl.value = team.value.websiteUrl ?? ''
  } catch (err) {
    notify(err.message, 'error')
  }
}

async function saveTeam() {
  saving.value = true
  try {
    const updated = await updateTeam(route.params.id, {
      sectionId: team.value.sectionId,
      name: editName.value,
      description: editDescription.value,
      websiteUrl: editWebsiteUrl.value,
    })
    team.value = { ...team.value, ...updated }
    router.push('/teams')
  } catch (err) {
    notify(err.message, 'error')
  } finally {
    saving.value = false
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

async function loadStudents() {
  try {
    const response = await searchStudents('')
    students.value = response.data.data
  } catch {
    students.value = []
  }
}

function studentLabel(student) {
  return `${student.firstName ?? ''} ${student.lastName ?? ''}`.trim() || student.email
}

function studentName(id) {
  const student = students.value.find((item) => item.id === id)
  return student ? studentLabel(student) : `Student ${id}`
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
    notify(err.message, 'error')
  }
}

async function assignStudents() {
  try {
    team.value = await assignStudentsToTeam(route.params.id, selectedStudentIds.value)
    selectedStudentIds.value = []
  } catch (err) {
    notify(err.message, 'error')
  }
}

async function removeStudent(studentId) {
  try {
    team.value = await removeStudentFromTeam(route.params.id, studentId)
  } catch (err) {
    notify(err.message, 'error')
  }
}

async function removeInstructor(instructorId) {
  try {
    team.value = await removeInstructorFromTeam(route.params.id, instructorId)
  } catch (err) {
    notify(err.message, 'error')
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
    notify(err.message, 'error')
  }
}

onMounted(() => {
  loadTeam()
  loadStudents()
  loadInstructors()
})
</script>
