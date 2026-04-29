<template>
  <div>
    <div class="text-h5 font-weight-bold mb-1">Student Peer Evaluation Report</div>
    <div class="text-body-2 text-medium-emphasis mb-6">Review per-week peer grades and public/private comments.</div>

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

    <v-card v-if="report" rounded="xl" elevation="1">
      <v-table>
        <thead>
          <tr>
            <th>Week</th>
            <th>Grade</th>
            <th>Public Comments</th>
            <th>Private Comments</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="week in report.weeks" :key="week.weekId">
            <td>{{ week.startDate }} to {{ week.endDate }}</td>
            <td>{{ week.grade }}</td>
            <td>
              <div v-for="comment in week.publicComments" :key="comment">{{ comment }}</div>
              <span v-if="week.publicComments.length === 0" class="text-medium-emphasis">None</span>
            </td>
            <td>
              <div v-for="comment in week.privateComments" :key="comment">{{ comment }}</div>
              <span v-if="week.privateComments.length === 0" class="text-medium-emphasis">None</span>
            </td>
          </tr>
        </tbody>
      </v-table>
    </v-card>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { searchStudents } from '../../api/students'
import { getStudentPeerEvalReport } from '../../api/reports'

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
    const response = await getStudentPeerEvalReport(selectedStudentId.value, startDate.value, endDate.value)
    report.value = response.data.data
  } catch (err) {
    error.value = err.response?.data?.message ?? 'Failed to load student peer evaluation report.'
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  const response = await searchStudents('')
  students.value = response.data.data
})
</script>
