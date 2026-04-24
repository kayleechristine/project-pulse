<template>
  <div>
    <div class="d-flex flex-wrap align-center justify-space-between mb-6 ga-3">
      <div>
        <div class="text-h3 font-weight-bold">Teams</div>
        <div class="text-h6 text-medium-emphasis">
          Find, view, edit, and manage senior design teams.
        </div>
      </div>

      <v-btn color="primary" prepend-icon="mdi-plus" to="/teams/new">
        Create Team
      </v-btn>
    </div>

    <v-card rounded="xl" elevation="1" class="mb-4">
      <v-card-text>
        <v-row>
          <v-col cols="12" md="4">
            <v-text-field
              v-model="search"
              label="Search by team name"
              variant="outlined"
              prepend-inner-icon="mdi-magnify"
              hide-details
            />
          </v-col>

          <v-col cols="12" md="4">
            <v-select
              v-model="sectionFilter"
              label="Section"
              :items="sectionOptions"
              variant="outlined"
              hide-details
            />
          </v-col>

          <v-col cols="12" md="4" class="d-flex align-center ga-2">
            <v-btn color="primary">Search</v-btn>
            <v-btn variant="outlined" @click="resetFilters">Reset</v-btn>
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>

    <v-card rounded="xl" elevation="1">
      <v-table>
        <thead>
          <tr>
            <th class="text-left">Team Name</th>
            <th class="text-left">Section</th>
            <th class="text-left">Description</th>
            <th class="text-left">Website</th>
            <th class="text-left">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="team in filteredTeams" :key="team.id">
            <td>{{ team.name }}</td>
            <td>{{ sectionName(team.sectionId) }}</td>
            <td>{{ team.description }}</td>
            <td>{{ team.websiteUrl }}</td>
            <td>
              <div class="d-flex ga-2">
                <v-btn size="small" variant="tonal" color="primary" :to="`/teams/${team.id}`">View</v-btn>
                <v-btn size="small" variant="outlined" :to="`/teams/${team.id}/edit`">Edit</v-btn>
                <v-btn size="small" variant="outlined" color="error" @click="deleteTeam(team)">
                  Delete
                </v-btn>
              </div>
            </td>
          </tr>
        </tbody>
      </v-table>
    </v-card>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { sections, teams } from '../../data/mockAdminData'

const search = ref('')
const sectionFilter = ref('All')

const sectionOptions = ['All', ...sections.map(s => s.name)]

const filteredTeams = computed(() => {
  return teams.filter(team => {
    const matchesSearch = team.name.toLowerCase().includes(search.value.toLowerCase())
    const matchesSection =
      sectionFilter.value === 'All' ||
      sections.find(s => s.id === team.sectionId)?.name === sectionFilter.value
    return matchesSearch && matchesSection
  })
})

function resetFilters() {
  search.value = ''
  sectionFilter.value = 'All'
}

function sectionName(sectionId) {
  return sections.find(s => s.id === sectionId)?.name ?? '—'
}

function deleteTeam(team) {
  alert(`Delete "${team.name}" (mock only).`)
}
</script>