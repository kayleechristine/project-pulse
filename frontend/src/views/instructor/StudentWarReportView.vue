<template>
  <div>
    <div class="text-h5 font-weight-bold mb-1">Student WAR Report</div>
    <div class="text-body-2 text-medium-emphasis mb-6">Review weekly activity grouped by active week.</div>

    <div class="d-flex ga-4 mb-6 flex-wrap">
      <v-select
        v-model="selectedStudentId"
        :items="students"
        :item-title="studentLabel"
        item-value="id"
        label="Student"
        variant="outlined"
        style="max-width: 320px"
      />
      <v-text-field v-model="startDate" label="Start Date" type="date" variant="outlined" style="max-width: 200px" />
      <v-text-field v-model="endDate" label="End Date" type="date" variant="outlined" style="max-width: 200px" />
      <v-btn color="primary" :disabled="!selectedStudentId || !startDate || !endDate" :loading="loading" @click="loadReport">
        Generate
      </v-btn>
    </div>

    <v-alert v-if="error" type="error" variant="tonal" class="mb-4">{{ error }}</v-alert>

    <div v-if="report">
      <v-card v-for="week in report.weeks" :key="week.weekId" rounded="xl" elevation="1" class="pa-6 mb-4">
        <div class="d-flex align-center justify-space-between mb-3">
          <div class="text-h6 font-weight-bold">{{ week.startDate }} to {{ week.endDate }}</div>
          <v-chip :color="week.submitted ? 'success' : 'error'" size="small">
            {{ week.submitted ? 'Submitted' : 'No WAR' }}
          </v-chip>
        </div>
        <div class="text-body-2 text-medium-emphasis mb-3">
          {{ week.plannedHours }} planned hours / {{ week.actualHours }} actual hours
        </div>
        <v-table v-if="week.activities.length > 0" density="compact">
          <thead>
            <tr>
              <th>Category</th>
              <th>Description</th>
              <th>Hours</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="activity in week.activities" :key="activity.id">
              <td>{{ activity.category }}</td>
              <td>{{ activity.description }}</td>
              <td>{{ activity.plannedHours }} / {{ activity.actualHours ?? 0 }}</td>
              <td>{{ activity.status }}</td>
            </tr>
          </tbody>
        </v-table>
      </v-card>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { searchStudents } from '../../api/students'
import { getStudentWarReport } from '../../api/reports'

const students = ref([])
const selectedStudentId = ref(null)
const startDate = ref('')
const endDate = ref('')
const report = ref(null)
const loading = ref(false)
const error = ref('')

function studentLabel(student) {
  return `${student.firstName ?? ''} ${student.lastName ?? ''}`.trim() || student.email
}

async function loadReport() {
  loading.value = true
  error.value = ''
  try {
    const response = await getStudentWarReport(selectedStudentId.value, startDate.value, endDate.value)
    report.value = response.data.data
  } catch (err) {
    error.value = err.response?.data?.message ?? 'Failed to load student WAR report.'
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  const response = await searchStudents('')
  students.value = response.data.data
})
</script>
