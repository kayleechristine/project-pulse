<template>
  <div class="pa-6">
    <h1>Assign Students to Team</h1>

    <p v-if="team" class="mb-4">
      Team: <strong>{{ team.name }}</strong>
    </p>

    <v-alert v-if="error" type="error" class="mb-4">
      {{ error }}
    </v-alert>

    <v-alert v-if="success" type="success" class="mb-4">
      {{ success }}
    </v-alert>

    <v-card class="pa-4 mb-6">
      <h2 class="mb-4">Add or Move Students</h2>

      <v-select
        v-model="selectedStudentIds"
        :items="availableStudents"
        :item-title="studentLabel"
        item-value="id"
        label="Select students"
        multiple
        chips
      />

      <v-btn color="primary" class="mt-4" @click="assignStudents">
        Add / Move Students
      </v-btn>
    </v-card>

    <v-card class="pa-4">
      <h2 class="mb-4">Current Team Members</h2>

      <v-list v-if="currentMembers.length">
        <v-list-item
          v-for="student in currentMembers"
          :key="student.id"
        >
          <v-list-item-title>
            {{ studentLabel(student) }}
          </v-list-item-title>

          <template #append>
            <v-btn
              color="error"
              variant="tonal"
              size="small"
              @click="removeStudent(student.id)"
            >
              Remove
            </v-btn>
          </template>
        </v-list-item>
      </v-list>

      <p v-else>No students are assigned to this team yet.</p>
    </v-card>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { getTeam, assignStudentsToTeam, removeStudentFromTeam } from '../../api/teams'
import { searchStudents } from '../../api/students'

const route = useRoute()

const team = ref(null)
const students = ref([])
const selectedStudentIds = ref([])
const error = ref('')
const success = ref('')

const teamId = computed(() => Number(route.params.id))

const currentMembers = computed(() => {
  if (!team.value?.studentIds) return []
  return students.value.filter(student => team.value.studentIds.includes(student.id))
})

const availableStudents = computed(() => {
  if (!team.value?.studentIds) return students.value
  return students.value.filter(student => !team.value.studentIds.includes(student.id))
})

function studentLabel(student) {
  return `${student.firstName ?? ''} ${student.lastName ?? ''} (${student.email ?? 'no email'})`
}

async function loadData() {
  error.value = ''

  try {
    team.value = await getTeam(teamId.value)

    const studentsResponse = await searchStudents('')
    students.value = studentsResponse.data.data
  } catch (err) {
    error.value = err.message || 'Failed to load team students.'
  }
}

async function assignStudents() {
  error.value = ''
  success.value = ''

  if (selectedStudentIds.value.length === 0) {
    error.value = 'Select at least one student.'
    return
  }

  try {
    team.value = await assignStudentsToTeam(teamId.value, selectedStudentIds.value)
    selectedStudentIds.value = []
    success.value = 'Students assigned successfully.'
  } catch (err) {
    error.value = err.message || 'Failed to assign students.'
  }
}

async function removeStudent(studentId) {
  error.value = ''
  success.value = ''

  try {
    team.value = await removeStudentFromTeam(teamId.value, studentId)
    success.value = 'Student removed successfully.'
  } catch (err) {
    error.value = err.message || 'Failed to remove student.'
  }
}

onMounted(loadData)
</script>
