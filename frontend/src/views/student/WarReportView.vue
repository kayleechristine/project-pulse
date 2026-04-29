<template>
  <div>
    <div class="text-h5 font-weight-bold mb-1">Team WAR Report</div>
    <div class="text-body-2 text-medium-emphasis mb-6">View your team's weekly activity report.</div>

    <v-select
      v-model="selectedWeekId"
      :items="MOCK_WEEKS"
      item-title="label"
      item-value="id"
      label="Select Week"
      variant="outlined"
      style="max-width: 320px"
      class="mb-6"
      @update:model-value="loadReport"
    />

    <v-alert v-if="error" type="error" variant="tonal" class="mb-4">{{ error }}</v-alert>

    <div v-if="loading" class="d-flex justify-center py-8">
      <v-progress-circular indeterminate color="primary" />
    </div>

    <template v-else-if="report">
      <div v-if="report.entries.length === 0" class="text-medium-emphasis text-body-2">
        No data available for this week yet.
      </div>

      <div v-for="entry in report.entries" :key="entry.studentId" class="mb-6">
        <div class="d-flex align-center ga-2 mb-2">
          <div class="text-h6 font-weight-medium">Student #{{ entry.studentId }}</div>
          <v-chip v-if="!entry.submitted" color="error" size="small">Did not submit</v-chip>
        </div>

        <v-table v-if="entry.submitted">
          <thead>
            <tr>
              <th>Category</th>
              <th>Description</th>
              <th>Planned Hrs</th>
              <th>Actual Hrs</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="activity in entry.activities" :key="activity.id">
              <td>{{ categoryLabel(activity.category) }}</td>
              <td>{{ activity.description }}</td>
              <td>{{ activity.plannedHours }}</td>
              <td>{{ activity.actualHours ?? '—' }}</td>
              <td>{{ statusLabel(activity.status) }}</td>
            </tr>
          </tbody>
        </v-table>
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getTeamWarReport } from '../../api/war'

const MOCK_WEEKS = [
  { id: 1, label: 'Week 1 (Jan 13 – Jan 19)' },
  { id: 2, label: 'Week 2 (Jan 20 – Jan 26)' },
  { id: 3, label: 'Week 3 (Jan 27 – Feb 2)' },
]

const MOCK_TEAM_ID = 1

const CATEGORY_LABELS = {
  DEVELOPMENT: 'Development', TESTING: 'Testing', BUGFIX: 'Bug Fix',
  COMMUNICATION: 'Communication', DOCUMENTATION: 'Documentation', DESIGN: 'Design',
  PLANNING: 'Planning', LEARNING: 'Learning', DEPLOYMENT: 'Deployment',
  SUPPORT: 'Support', MISCELLANEOUS: 'Miscellaneous',
}

const STATUS_LABELS = {
  IN_PROGRESS: 'In Progress',
  UNDER_TESTING: 'Under Testing',
  DONE: 'Done',
}

function categoryLabel(val) { return CATEGORY_LABELS[val] ?? val }
function statusLabel(val) { return STATUS_LABELS[val] ?? val }

const selectedWeekId = ref(MOCK_WEEKS[0].id)
const report = ref(null)
const loading = ref(true)
const error = ref('')

onMounted(loadReport)

async function loadReport() {
  loading.value = true
  error.value = ''
  try {
    const response = await getTeamWarReport(MOCK_TEAM_ID, selectedWeekId.value)
    report.value = response.data.data
  } catch (err) {
    error.value = err.response?.data?.message ?? 'Failed to load report.'
  } finally {
    loading.value = false
  }
}
</script>
