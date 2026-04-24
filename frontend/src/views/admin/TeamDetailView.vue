<template>
  <div v-if="team">
    <div class="d-flex flex-wrap align-center justify-space-between mb-6 ga-3">
      <div>
        <div class="text-h3 font-weight-bold">Team Details</div>
        <div class="text-h6 text-medium-emphasis">{{ team.name }}</div>
      </div>

      <div class="d-flex ga-2">
        <v-btn color="primary" :to="`/teams/${team.id}/edit`" prepend-icon="mdi-pencil-outline">
          Edit Team
        </v-btn>
        <v-btn variant="outlined" color="error" prepend-icon="mdi-delete-outline" @click="removeTeam">
          Delete Team
        </v-btn>
      </div>
    </div>

    <v-card rounded="xl" elevation="1" class="pa-6">
      <div class="mb-3"><strong>Name:</strong> {{ team.name }}</div>
      <div class="mb-3"><strong>Section:</strong> {{ section?.name }}</div>
      <div class="mb-3"><strong>Description:</strong> {{ team.description }}</div>
      <div><strong>Website URL:</strong> {{ team.websiteUrl }}</div>
    </v-card>
  </div>

  <v-alert v-else type="error" variant="tonal">
    Team not found.
  </v-alert>
</template>

<script setup>
import { computed } from 'vue'
import { getTeamById, getSectionById } from '../../data/mockAdminData'

const props = defineProps({
  id: String,
})

const team = computed(() => getTeamById(props.id))
const section = computed(() => team.value ? getSectionById(team.value.sectionId) : null)

function removeTeam() {
  alert(`Delete "${team.value.name}" (mock only).`)
}
</script>