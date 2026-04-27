<template>
  <div>
    <div class="mb-6">
      <div class="text-h3 font-weight-bold">Team Details</div>
      <div class="text-h6 text-medium-emphasis">
        View and manage a senior design team.
      </div>
    </div>

    <v-alert v-if="error" type="error" variant="tonal" class="mb-4">
      {{ error }}
    </v-alert>

    <v-card v-if="team" rounded="xl" elevation="1" class="pa-6">
      <div class="text-h4 font-weight-bold mb-2">{{ team.name }}</div>
      <div class="text-body-1 mb-4">{{ team.description }}</div>

      <div class="mb-2">
        <strong>Website:</strong>
        <span class="ml-2">{{ team.websiteUrl || 'N/A' }}</span>
      </div>

      <div class="d-flex ga-2 mt-6">
        <v-btn color="primary" :to="`/teams/${team.id}/edit`">Edit Team</v-btn>
        <v-btn color="error" variant="tonal" @click="removeTeam">Delete Team</v-btn>
        <v-btn variant="outlined" to="/teams">Back</v-btn>
      </div>
    </v-card>

    <v-card v-else rounded="xl" elevation="1" class="pa-6">
      Loading team...
    </v-card>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getTeam, deleteTeam } from '../../api/teams'

const route = useRoute()
const router = useRouter()

const team = ref(null)
const error = ref('')

async function loadTeam() {
  try {
    team.value = await getTeam(route.params.id)
  } catch (err) {
    error.value = err.message
  }
}

async function removeTeam() {
  if (!confirm(`Delete "${team.value.name}" permanently?`)) {
    return
  }

  try {
    await deleteTeam(route.params.id)
    alert('Team deleted successfully')
    router.push('/teams')
  } catch (err) {
    error.value = err.message
  }
}

onMounted(loadTeam)
</script>