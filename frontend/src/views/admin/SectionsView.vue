<template>
  <div>
    <div class="d-flex flex-wrap align-center justify-space-between mb-6 ga-3">
      <div>
        <div class="text-h3 font-weight-bold">Sections</div>
        <div class="text-h6 text-medium-emphasis">
          Manage senior design sections and drill into teams.
        </div>
      </div>

      <v-btn color="primary" prepend-icon="mdi-plus" to="/sections/new">
        Create Section
      </v-btn>
    </div>

    <v-card rounded="xl" elevation="1" class="mb-4">
      <v-card-text>
        <v-row>
          <v-col cols="12" md="5">
            <v-text-field
              v-model="search"
              label="Search by section name"
              variant="outlined"
              prepend-inner-icon="mdi-magnify"
              hide-details
            />
          </v-col>

          <v-col cols="12" md="3">
            <v-select
              v-model="yearFilter"
              label="Academic Year"
              :items="['All', ...yearOptions]"
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
            <th class="text-left">Section Name</th>
            <th class="text-left">Dates</th>
            <th class="text-left">Teams</th>
            <th class="text-left">Rubric</th>
            <th class="text-left">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="section in filteredSections" :key="section.id">
            <td>{{ section.name }}</td>
            <td>{{ formatDate(section.startDate) }} - {{ formatDate(section.endDate) }}</td>
            <td>{{ teamCount(section.id) }}</td>
            <td>{{ rubricName(section.rubricId) }}</td>
            <td>
              <div class="d-flex ga-2">
                <v-btn size="small" variant="tonal" color="primary" :to="`/sections/${section.id}`">
                  View
                </v-btn>
                <v-btn size="small" variant="outlined" :to="`/sections/${section.id}/edit`">
                  Edit
                </v-btn>
                <v-btn size="small" variant="outlined" :to="`/sections/${section.id}/active-weeks`">
                  Active Weeks
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
import { sections, teams, rubrics } from '../../data/mockAdminData'

const search = ref('')
const yearFilter = ref('All')

const yearOptions = sections.map(s => s.name)

const filteredSections = computed(() => {
  return sections.filter(section => {
    const matchesSearch = section.name.toLowerCase().includes(search.value.toLowerCase())
    const matchesYear = yearFilter.value === 'All' || section.name === yearFilter.value
    return matchesSearch && matchesYear
  })
})

function resetFilters() {
  search.value = ''
  yearFilter.value = 'All'
}

function teamCount(sectionId) {
  return teams.filter(team => team.sectionId === sectionId).length
}

function rubricName(rubricId) {
  return rubrics.find(r => r.id === rubricId)?.name ?? '—'
}

function formatDate(date) {
  return new Date(`${date}T00:00:00`).toLocaleDateString()
}
</script>