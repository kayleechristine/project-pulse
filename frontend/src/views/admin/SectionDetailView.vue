<template>
  <div v-if="section">
    <div class="d-flex flex-wrap align-center justify-space-between mb-6 ga-3">
      <div>
        <div class="text-h3 font-weight-bold">Section Details</div>
        <div class="text-h6 text-medium-emphasis">{{ section.name }}</div>
      </div>

      <div class="d-flex ga-2">
        <v-btn color="primary" :to="`/sections/${section.id}/edit`" prepend-icon="mdi-pencil-outline">
          Edit Section
        </v-btn>
        <v-btn variant="outlined" :to="`/sections/${section.id}/active-weeks`" prepend-icon="mdi-calendar-week">
          Active Weeks
        </v-btn>
      </div>
    </div>

    <v-row>
      <v-col cols="12" md="6">
        <v-card rounded="xl" elevation="1" class="pa-4">
          <div class="text-h5 font-weight-bold mb-4">Section Info</div>
          <div class="mb-2"><strong>Name:</strong> {{ section.name }}</div>
          <div class="mb-2"><strong>Start Date:</strong> {{ formatDate(section.startDate) }}</div>
          <div class="mb-2"><strong>End Date:</strong> {{ formatDate(section.endDate) }}</div>
          <div><strong>Rubric:</strong> {{ rubric?.name }}</div>
        </v-card>
      </v-col>

      <v-col cols="12" md="6">
        <v-card rounded="xl" elevation="1" class="pa-4">
          <div class="text-h5 font-weight-bold mb-4">Unassigned / Summary</div>
          <div class="mb-2"><strong>Teams:</strong> {{ sectionTeams.length }}</div>
          <div class="mb-2"><strong>Inactive Weeks:</strong> {{ section.inactiveWeeks.length }}</div>
          <div><strong>Rubric Criteria:</strong> {{ rubric?.criteria?.length ?? 0 }}</div>
        </v-card>
      </v-col>
    </v-row>

    <v-card rounded="xl" elevation="1" class="mt-6 pa-4">
      <div class="d-flex justify-space-between align-center mb-4">
        <div class="text-h5 font-weight-bold">Teams in this Section</div>
        <v-btn color="primary" prepend-icon="mdi-plus" to="/teams/new">Create Team</v-btn>
      </div>

      <v-table>
        <thead>
          <tr>
            <th class="text-left">Team</th>
            <th class="text-left">Description</th>
            <th class="text-left">Website</th>
            <th class="text-left">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="team in sectionTeams" :key="team.id">
            <td>{{ team.name }}</td>
            <td>{{ team.description }}</td>
            <td>{{ team.websiteUrl }}</td>
            <td>
              <div class="d-flex ga-2">
                <v-btn size="small" variant="tonal" color="primary" :to="`/teams/${team.id}`">View</v-btn>
                <v-btn size="small" variant="outlined" :to="`/teams/${team.id}/edit`">Edit</v-btn>
              </div>
            </td>
          </tr>
        </tbody>
      </v-table>
    </v-card>
  </div>

  <v-alert v-else type="error" variant="tonal">
    Section not found.
  </v-alert>
</template>

<script setup>
import { computed } from 'vue'
import { getSectionById, getRubricById, getTeamsBySectionId } from '../../data/mockAdminData'

const props = defineProps({
  id: String,
})

const section = computed(() => getSectionById(props.id))
const rubric = computed(() => section.value ? getRubricById(section.value.rubricId) : null)
const sectionTeams = computed(() => section.value ? getTeamsBySectionId(section.value.id) : [])

function formatDate(date) {
  return new Date(`${date}T00:00:00`).toLocaleDateString()
}
</script>