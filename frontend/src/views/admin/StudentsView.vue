<template>
  <div>
    <div class="d-flex justify-space-between align-center mb-6">
      <div>
        <div class="text-h3 font-weight-bold">Students</div>
        <div class="text-h6 text-medium-emphasis">Search and manage students.</div>
      </div>

      <v-btn color="primary" to="/students/invite" prepend-icon="mdi-email-plus">
        Invite Student
      </v-btn>
    </div>

    <v-alert v-if="error" type="error" variant="tonal" class="mb-4">
      {{ error }}
    </v-alert>

    <v-card rounded="xl" elevation="1">
      <div class="pa-4">
        <v-text-field
          v-model="search"
          label="Search students"
          prepend-inner-icon="mdi-magnify"
          variant="outlined"
          density="comfortable"
          hide-details
          @keyup.enter="loadStudents"
        />
      </div>
      <v-table>
        <thead>
          <tr>
            <th>Name</th>
            <th>Email</th>
            <th>Status</th>
            <th class="text-right">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="student in students" :key="student.id">
            <td>{{ student.firstName }} {{ student.lastName }}</td>
            <td>{{ student.email }}</td>
            <td>{{ student.accountStatus }}</td>
            <td class="text-right">
              <v-btn size="small" variant="text" :to="`/students/${student.id}`">View</v-btn>
              <v-btn size="small" color="error" variant="text" @click="removeStudent(student)">Delete</v-btn>
            </td>
          </tr>
          <tr v-if="students.length === 0">
            <td colspan="4" class="text-center pa-6 text-medium-emphasis">No students found.</td>
          </tr>
        </tbody>
      </v-table>
    </v-card>
  </div>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue'
import { deleteStudent, searchStudents } from '../../api/students'

const students = ref([])
const search = ref('')
const error = ref('')

async function loadStudents() {
  try {
    const response = await searchStudents(search.value)
    students.value = response.data.data
  } catch (err) {
    error.value = err.response?.data?.message ?? 'Failed to load students.'
  }
}

async function removeStudent(student) {
  if (!confirm(`Delete ${student.firstName} ${student.lastName}? This removes associated WARs and peer evaluations.`)) {
    return
  }

  try {
    await deleteStudent(student.id)
    await loadStudents()
  } catch (err) {
    error.value = err.response?.data?.message ?? 'Failed to delete student.'
  }
}

watch(search, () => loadStudents())
onMounted(loadStudents)
</script>
