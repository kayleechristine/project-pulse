<template>
  <div>
    <div class="text-h5 font-weight-bold mb-1">Team WAR Report</div>
    <div class="text-body-2 text-medium-emphasis mb-6">View your team's weekly activity report.</div>

    <v-select
      v-model="selectedWeekId"
      :items="weeks"
      item-title="label"
      item-value="id"
      label="Select Week"
      variant="outlined"
      style="max-width: 320px"
      class="mb-6"
      :loading="loadingWeeks"
      :disabled="loadingWeeks"
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
import { getMyTeam, getTeamWarReport } from '../../api/war'
import { getActiveWeeksForSection } from '../../api/activeWeeks'

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

function formatWeekLabel(weekStart) {
  const start = new Date(weekStart + 'T00:00:00')
  const end = new Date(start)
  end.setDate(end.getDate() + 6)
  const fmt = { month: 'short', day: 'numeric' }
  return `${start.toLocaleDateString('en-US', fmt)} – ${end.toLocaleDateString('en-US', fmt)}`
}

const selectedWeekId = ref(null)
const weeks = ref([])
const loadingWeeks = ref(false)
const teamId = ref(null)
const report = ref(null)
const loading = ref(false)
const error = ref('')

onMounted(async () => {
  loadingWeeks.value = true
  error.value = ''
  try {
    const teamRes = await getMyTeam()
    teamId.value = teamRes.data.data.teamId
    const sectionId = teamRes.data.data.sectionId
    const weeksRes = await getActiveWeeksForSection(sectionId)
    weeks.value = weeksRes.data.data.map(w => ({
      id: w.id,
      label: formatWeekLabel(w.weekStart),
    }))
    if (weeks.value.length > 0) {
      selectedWeekId.value = weeks.value[0].id
      await loadReport()
    }
  } catch {
    error.value = 'Failed to load weeks.'
  } finally {
    loadingWeeks.value = false
  }
})

async function loadReport() {
  loading.value = true
  error.value = ''
  try {
    const response = await getTeamWarReport(teamId.value, selectedWeekId.value)
    report.value = response.data.data
  } catch (err) {
    error.value = err.response?.data?.message ?? 'Failed to load report.'
  } finally {
    loading.value = false
  }
}
</script>
