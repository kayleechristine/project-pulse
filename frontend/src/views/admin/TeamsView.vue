<template>
  <div>
    <div class="d-flex justify-space-between align-center mb-6">
      <div>
        <div class="text-h3 font-weight-bold">Teams</div>
        <div class="text-h6 text-medium-emphasis">
          Find and manage senior design teams.
        </div>
      </div>

      <v-btn color="primary" to="/teams/new" prepend-icon="mdi-plus">
        Create Team
      </v-btn>
    </div>

    <v-alert v-if="error" type="error" variant="tonal" class="mb-4">
      {{ error }}
    </v-alert>

    <v-card rounded="xl" elevation="1">
      <v-table>
        <thead>
          <tr>
            <th>Team Name</th>
            <th>Description</th>
            <th>Website</th>
            <th class="text-right">Actions</th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="team in teams" :key="team.id">
            <td>{{ team.name }}</td>
            <td>{{ team.description }}</td>
            <td>{{ team.websiteUrl || 'N/A' }}</td>
            <td class="text-right">
              <v-btn size="small" variant="text" :to="`/teams/${team.id}`">
                Manage
              </v-btn>
              <v-btn size="small" variant="text" :to="`/teams/${team.id}/edit`">
                Edit
              </v-btn>
              <v-btn size="small" variant="text" color="error" @click="removeTeam(team)">
                Delete
              </v-btn>
            </td>
          </tr>

          <tr v-if="teams.length === 0">
            <td colspan="4" class="text-center pa-6 text-medium-emphasis">
              No teams found.
            </td>
          </tr>
        </tbody>
      </v-table>
    </v-card>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { getTeams, deleteTeam } from '../../api/teams'

const teams = ref([])
const error = ref('')

async function loadTeams() {
  try {
    teams.value = await getTeams()
  } catch (err) {
    error.value = err.message
  }
}

async function removeTeam(team) {
  if (!confirm(`Delete "${team.name}" permanently?`)) {
    return
  }

  try {
    await deleteTeam(team.id)
    alert('Team deleted successfully')
    await loadTeams()
  } catch (err) {
    error.value = err.message
  }
}

onMounted(loadTeams)
</script>
