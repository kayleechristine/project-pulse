<template>
  <div>
    <div class="text-h5 font-weight-bold mb-1">Weekly Activity Report</div>
    <div class="text-body-2 text-medium-emphasis mb-6">Log your work for the week.</div>

    <v-alert v-if="error" type="error" variant="tonal" class="mb-4">{{ error }}</v-alert>

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
      @update:model-value="loadActivities"
    />

    <div class="d-flex align-center justify-space-between mb-3">
      <div class="text-h6 font-weight-medium">Activities</div>
      <v-btn color="primary" prepend-icon="mdi-plus" @click="openForm(null)">
        Add Activity
      </v-btn>
    </div>

    <WarActivityTable
      :activities="activities"
      class="mb-4"
      @edit="openForm"
      @delete="confirmDelete"
    />

    <v-dialog v-model="formOpen" max-width="600">
      <WarActivityForm
        :activity="editingActivity"
        :week-id="selectedWeekId"
        @saved="onSaved"
        @cancelled="formOpen = false"
      />
    </v-dialog>

    <v-dialog v-model="deleteDialogOpen" max-width="400">
      <v-card rounded="lg">
        <v-card-title class="pa-4">Delete Activity</v-card-title>
        <v-card-text class="px-4">Are you sure you want to delete this activity?</v-card-text>
        <v-card-actions class="pa-4 pt-0">
          <v-spacer />
          <v-btn variant="text" @click="deleteDialogOpen = false">Cancel</v-btn>
          <v-btn color="error" :loading="deleting" @click="handleDelete">Delete</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyTeam, getActivities, deleteActivity } from '../../api/war'
import { getActiveWeeksForSection } from '../../api/activeWeeks'
import WarActivityTable from '../../components/WarActivityTable.vue'
import WarActivityForm from '../../components/WarActivityForm.vue'

const selectedWeekId = ref(null)
const weeks = ref([])
const loadingWeeks = ref(false)
const activities = ref([])
const error = ref('')

const formOpen = ref(false)
const editingActivity = ref(null)

const deleteDialogOpen = ref(false)
const deletingActivity = ref(null)
const deleting = ref(false)

onMounted(async () => {
  await loadWeeks()
  if (selectedWeekId.value) loadActivities()
})

async function loadWeeks() {
  loadingWeeks.value = true
  error.value = ''
  try {
    const teamRes = await getMyTeam()
    const sectionId = teamRes.data.data.sectionId
    const weeksRes = await getActiveWeeksForSection(sectionId)
    weeks.value = weeksRes.data.data.map(w => ({
      id: w.id,
      label: formatWeekLabel(w.startDate),
    }))
    if (weeks.value.length > 0) {
      selectedWeekId.value = weeks.value[0].id
    }
  } catch {
    error.value = 'Failed to load weeks.'
  } finally {
    loadingWeeks.value = false
  }
}

function formatWeekLabel(weekStart) {
  const start = new Date(weekStart + 'T00:00:00')
  const end = new Date(start)
  end.setDate(end.getDate() + 6)
  const fmt = { month: 'short', day: 'numeric' }
  return `${start.toLocaleDateString('en-US', fmt)} – ${end.toLocaleDateString('en-US', fmt)}`
}

async function loadActivities() {
  error.value = ''
  try {
    const response = await getActivities(selectedWeekId.value)
    activities.value = response.data.data
  } catch {
    error.value = 'Failed to load activities.'
  }
}

function openForm(activity) {
  editingActivity.value = activity
  formOpen.value = true
}

function onSaved() {
  formOpen.value = false
  loadActivities()
}

function confirmDelete(activity) {
  deletingActivity.value = activity
  deleteDialogOpen.value = true
}

async function handleDelete() {
  deleting.value = true
  try {
    await deleteActivity(deletingActivity.value.id)
    deleteDialogOpen.value = false
    loadActivities()
  } catch {
    error.value = 'Failed to delete activity.'
    deleteDialogOpen.value = false
  } finally {
    deleting.value = false
  }
}
</script>
