<template>
  <div>
    <div class="text-h5 font-weight-bold mb-1">Section Peer Evaluation Report</div>
    <div class="text-body-2 text-medium-emphasis mb-6">Review grades, comments, and missing peer evaluation submissions.</div>

    <div class="d-flex ga-4 mb-6 flex-wrap">
      <v-select
        v-model="selectedSectionId"
        :items="sections"
        item-title="sectionName"
        item-value="id"
        label="Section"
        variant="outlined"
        style="max-width: 280px"
        @update:model-value="loadWeeks"
      />
      <v-select
        v-model="selectedWeekId"
        :items="weeks"
        item-title="label"
        item-value="id"
        label="Active Week"
        variant="outlined"
        style="max-width: 280px"
        :disabled="!selectedSectionId"
      />
      <v-btn color="primary" :disabled="!selectedSectionId || !selectedWeekId" :loading="loading" @click="loadReport">
        Generate
      </v-btn>
    </div>

    <v-alert v-if="error" type="error" variant="tonal" class="mb-4">{{ error }}</v-alert>

    <v-table v-if="report">
      <thead>
        <tr>
          <th>Student</th>
          <th>Grade</th>
          <th>Comments</th>
          <th>Submission</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="entry in report.entries" :key="entry.studentId">
          <td>{{ studentName(entry) }}</td>
          <td>{{ entry.grade }}</td>
          <td>
            <div v-for="comment in [...entry.publicComments, ...entry.privateComments]" :key="comment">
              {{ comment }}
            </div>
            <span v-if="entry.publicComments.length + entry.privateComments.length === 0" class="text-medium-emphasis">No comments</span>
          </td>
          <td>
            <v-chip v-if="entry.didNotSubmit" color="error" size="small">Did not submit</v-chip>
            <v-chip v-else color="success" size="small">Submitted</v-chip>
          </td>
        </tr>
      </tbody>
    </v-table>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { getSections } from '../../api/sections'
import { getActiveWeeksForSection } from '../../api/activeWeeks'
import { getSectionPeerEvalReport } from '../../api/reports'

const sections = ref([])
const weeks = ref([])
const selectedSectionId = ref(null)
const selectedWeekId = ref(null)
const report = ref(null)
const loading = ref(false)
const error = ref('')

function formatWeek(week) {
  return `${week.startDate} to ${week.endDate}`
}

function studentName(entry) {
  if (!entry.student) return `Student ${entry.studentId}`
  return `${entry.student.firstName ?? ''} ${entry.student.lastName ?? ''}`.trim() || entry.student.email
}

async function loadWeeks() {
  report.value = null
  selectedWeekId.value = null
  if (!selectedSectionId.value) return

  try {
    const response = await getActiveWeeksForSection(selectedSectionId.value)
    weeks.value = response.data.data.map((week) => ({ ...week, label: formatWeek(week) }))
    selectedWeekId.value = weeks.value[0]?.id ?? null
  } catch {
    error.value = 'Failed to load active weeks.'
  }
}

async function loadReport() {
  loading.value = true
  error.value = ''
  try {
    const response = await getSectionPeerEvalReport(selectedSectionId.value, selectedWeekId.value)
    report.value = response.data.data
  } catch (err) {
    error.value = err.response?.data?.message ?? 'Failed to load section report.'
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  sections.value = await getSections()
})
</script>
