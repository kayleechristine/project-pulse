<template>
  <div>
    <div class="d-flex justify-space-between align-center mb-6">
      <div>
        <div class="text-h3 font-weight-bold">Sections</div>
        <div class="text-h6 text-medium-emphasis">
          Manage senior design sections and drill into teams.
        </div>
      </div>

      <v-btn color="primary" to="/sections/new" prepend-icon="mdi-plus">
        Create Section
      </v-btn>
    </div>

    <v-card rounded="xl" elevation="1" class="pa-4 mb-4">
      <v-row>
        <v-col cols="12" md="5">
          <v-text-field
            v-model="search"
            label="Search by section name"
            prepend-inner-icon="mdi-magnify"
            variant="outlined"
            hide-details
          />
        </v-col>

        <v-col cols="12" md="3">
          <v-select
            v-model="academicYear"
            label="Academic Year"
            :items="academicYearOptions"
            variant="outlined"
            hide-details
          />
        </v-col>

        <v-col cols="12" md="4" class="d-flex ga-2">
          <v-btn color="primary" @click="applyFilters">Search</v-btn>
          <v-btn variant="outlined" @click="resetFilters">Reset</v-btn>
        </v-col>
      </v-row>
    </v-card>

    <v-alert v-if="error" type="error" variant="tonal" class="mb-4">
      {{ error }}
    </v-alert>

    <v-card rounded="xl" elevation="1">
      <v-table>
        <thead>
          <tr>
            <th>Section Name</th>
            <th>Dates</th>
            <th>Teams</th>
            <th>Rubric</th>
            <th>Actions</th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="section in filteredSections" :key="section.id">
            <td>{{ section.name }}</td>
            <td>{{ section.startDate }} - {{ section.endDate }}</td>
            <td>{{ section.teamCount ?? section.teams?.length ?? 0 }}</td>
            <td>{{ getRubricName(section.rubricId) }}</td>
            <td>
              <v-btn size="small" variant="text" :to="`/sections/${section.id}`">View</v-btn>
              <v-btn size="small" variant="outlined" class="mx-2" :to="`/sections/${section.id}/edit`">Edit</v-btn>
              <v-btn size="small" variant="outlined" :to="`/sections/${section.id}/active-weeks`">Active Weeks</v-btn>
            </td>
          </tr>

          <tr v-if="filteredSections.length === 0">
            <td colspan="5" class="text-center pa-6 text-medium-emphasis">
              No sections found.
            </td>
          </tr>
        </tbody>
      </v-table>
    </v-card>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { getSections } from '../../api/sections'
import { getRubrics } from '../../api/rubric'

const sections = ref([])
const rubrics = ref([])
const search = ref('')
const appliedSearch = ref('')
const academicYear = ref('All')
const appliedAcademicYear = ref('All')
const error = ref('')

const academicYearOptions = computed(() => {
  const years = sections.value.map(section => section.name).filter(Boolean)
  return ['All', ...new Set(years)]
})

const filteredSections = computed(() => {
  return sections.value.filter(section => {
    const matchesSearch = section.name?.toLowerCase().includes(appliedSearch.value.toLowerCase())
    const matchesYear = appliedAcademicYear.value === 'All' || section.name === appliedAcademicYear.value
    return matchesSearch && matchesYear
  })
})

function getRubricName(rubricId) {
  const rubric = rubrics.value.find(r => r.id === rubricId)
  return rubric?.name ?? 'N/A'
}

function applyFilters() {
  appliedSearch.value = search.value
  appliedAcademicYear.value = academicYear.value
}

function resetFilters() {
  search.value = ''
  appliedSearch.value = ''
  academicYear.value = 'All'
  appliedAcademicYear.value = 'All'
}

async function loadData() {
  try {
    sections.value = await getSections()
    rubrics.value = await getRubrics()
  } catch (err) {
    error.value = err.message
  }
}

onMounted(loadData)
</script>