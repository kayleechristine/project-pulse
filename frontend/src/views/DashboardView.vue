<template>
  <div>
    <div class="d-flex flex-wrap align-center justify-space-between mb-6 ga-3">
      <div>
        <div class="text-h3 font-weight-bold">Dashboard</div>
        <div class="text-h6 text-medium-emphasis">
          Overview of sections, teams, students, and reports.
        </div>
      </div>

      <div class="d-flex ga-2">
        <v-btn color="primary" prepend-icon="mdi-plus" to="/sections/new">
          New Section
        </v-btn>
        <v-btn variant="outlined" prepend-icon="mdi-account-group-outline" to="/teams/new">
          Create Team
        </v-btn>
      </div>
    </div>

    <v-alert
      v-if="error"
      type="error"
      variant="tonal"
      class="mb-4"
    >
      {{ error }}
    </v-alert>

    <v-row>
      <v-col cols="12" md="3">
        <v-card rounded="xl" elevation="1" class="pa-4">
          <div class="text-overline">Sections</div>
          <div class="text-h3 font-weight-bold">
            {{ loading ? '...' : sectionCount }}
          </div>
          <div class="text-body-1 text-medium-emphasis">Active academic sections</div>
        </v-card>
      </v-col>

      <v-col cols="12" md="3">
        <v-card rounded="xl" elevation="1" class="pa-4">
          <div class="text-overline">Teams</div>
          <div class="text-h3 font-weight-bold">
            {{ loading ? '...' : teamCount }}
          </div>
          <div class="text-body-1 text-medium-emphasis">Senior design teams</div>
        </v-card>
      </v-col>

      <v-col cols="12" md="3">
        <v-card rounded="xl" elevation="1" class="pa-4">
          <div class="text-overline">Students</div>
          <div class="text-h3 font-weight-bold">
            {{ studentCount }}
          </div>
          <div class="text-body-1 text-medium-emphasis">Registered students</div>
        </v-card>
      </v-col>

      <v-col cols="12" md="3">
        <v-card rounded="xl" elevation="1" class="pa-4">
          <div class="text-overline">Pending Reports</div>
          <div class="text-h3 font-weight-bold">
            {{ pendingReports }}
          </div>
          <div class="text-body-1 text-medium-emphasis">WAR / peer evaluation follow-up</div>
        </v-card>
      </v-col>
    </v-row>

    <v-row class="mt-2">
      <v-col cols="12" lg="7">
        <v-card rounded="xl" elevation="1">
          <v-card-title class="font-weight-bold">Quick Actions</v-card-title>
          <v-card-text>
            <div class="d-flex flex-wrap ga-3">
              <v-btn color="primary" variant="tonal" prepend-icon="mdi-google-classroom" to="/sections">
                Sections
              </v-btn>
              <v-btn color="primary" variant="tonal" prepend-icon="mdi-account-group-outline" to="/teams">
                Teams
              </v-btn>
              <v-btn color="primary" variant="tonal" prepend-icon="mdi-clipboard-text-outline" to="/rubrics">
                Rubrics
              </v-btn>
              <v-btn color="primary" variant="tonal" prepend-icon="mdi-calendar-week" :to="activeWeeksLink">
                Active Weeks
              </v-btn>
            </div>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" lg="5">
        <v-card rounded="xl" elevation="1">
          <v-card-title class="font-weight-bold">Recent Activity</v-card-title>
          <v-list lines="two">
            <v-list-item
              v-if="sections.length > 0"
              :title="`Most recent section: ${sections[0].name}`"
              subtitle="Loaded from backend section data"
            />
            <v-list-item
              v-if="teams.length > 0"
              :title="`Most recent team: ${teams[0].name}`"
              subtitle="Loaded from backend team data"
            />
            <v-list-item
              title="Reports"
              subtitle="Report totals are not connected yet"
            />
          </v-list>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { getSections } from '../api/sections'
import { getTeams } from '../api/teams'

const sections = ref([])
const teams = ref([])
const loading = ref(true)
const error = ref('')

const sectionCount = computed(() => sections.value.length)
const teamCount = computed(() => teams.value.length)

// Keep these at 0 unless your backend has student/report endpoints.
const studentCount = 0
const pendingReports = 0

const activeWeeksLink = computed(() => {
  if (sections.value.length > 0) {
    return `/sections/${sections.value[0].id}/active-weeks`
  }

  return '/sections'
})

onMounted(async () => {
  try {
    const [sectionData, teamData] = await Promise.all([
      getSections(),
      getTeams(),
    ])

    sections.value = Array.isArray(sectionData) ? sectionData : []
    teams.value = Array.isArray(teamData) ? teamData : []
  } catch (err) {
    console.error(err)
    error.value = 'Could not load dashboard data. Make sure the backend is running on port 8080.'
  } finally {
    loading.value = false
  }
})
</script>