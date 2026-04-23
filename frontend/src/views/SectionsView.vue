<template>
  <div>
    <div class="d-flex flex-wrap align-center justify-space-between mb-6 ga-3">
      <div>
        <div class="text-h4 font-weight-bold">Sections</div>
        <div class="text-body-1 text-medium-emphasis">
          Manage senior design sections and drill into teams.
        </div>
      </div>

      <v-btn color="primary" prepend-icon="mdi-plus">
        Create Section
      </v-btn>
    </div>

    <v-card rounded="xl" elevation="1" class="mb-4">
      <v-card-text>
        <v-row>
          <v-col cols="12" md="5">
            <v-text-field
              label="Search by section name"
              variant="outlined"
              density="comfortable"
              prepend-inner-icon="mdi-magnify"
              hide-details
            />
          </v-col>
          <v-col cols="12" md="3">
            <v-select
              label="Academic Year"
              :items="['All', '2025-2026', '2024-2025']"
              model-value="All"
              variant="outlined"
              density="comfortable"
              hide-details
            />
          </v-col>
          <v-col cols="12" md="4" class="d-flex align-center ga-2">
            <v-btn color="primary">Search</v-btn>
            <v-btn variant="outlined">Reset</v-btn>
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
          <tr v-for="section in sections" :key="section.name">
            <td>{{ section.name }}</td>
            <td>{{ section.dateRange }}</td>
            <td>{{ section.teams }}</td>
            <td>{{ section.rubric }}</td>
            <td>
              <div class="d-flex ga-2">
                <v-btn size="small" variant="tonal" color="primary">View</v-btn>
                <v-btn size="small" variant="outlined">Edit</v-btn>
              </div>
            </td>
          </tr>
        </tbody>
      </v-table>
    </v-card>
  </div>
</template>

<script setup>
const sections = [
  {
    name: '2025-2026',
    dateRange: '08/25/2025 - 05/01/2026',
    teams: 9,
    rubric: 'Peer Eval Rubric v1'
  },
  {
    name: '2024-2025',
    dateRange: '08/26/2024 - 05/02/2025',
    teams: 8,
    rubric: 'Peer Eval Rubric v1'
  }
]
</script>