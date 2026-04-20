<template>
  <AppShell title="Dashboard">
    <v-row>
      <v-col cols="12" md="4">
        <v-card rounded="xl" elevation="0" class="pa-5">
          <div class="text-overline text-medium-emphasis">Sections</div>
          <div class="text-h4 font-weight-bold mt-2">{{ stats.sections }}</div>
          <div class="text-body-2 text-medium-emphasis mt-2">
            Senior design sections in the system
          </div>
        </v-card>
      </v-col>

      <v-col cols="12" md="4">
        <v-card rounded="xl" elevation="0" class="pa-5">
          <div class="text-overline text-medium-emphasis">Teams</div>
          <div class="text-h4 font-weight-bold mt-2">0</div>
          <div class="text-body-2 text-medium-emphasis mt-2">
            Placeholder until team endpoints are built
          </div>
        </v-card>
      </v-col>

      <v-col cols="12" md="4">
        <v-card rounded="xl" elevation="0" class="pa-5">
          <div class="text-overline text-medium-emphasis">Reports</div>
          <div class="text-h4 font-weight-bold mt-2">0</div>
          <div class="text-body-2 text-medium-emphasis mt-2">
            WAR and peer evaluation reporting later
          </div>
        </v-card>
      </v-col>
    </v-row>

    <v-row class="mt-2">
      <v-col cols="12" md="8">
        <v-card rounded="xl" elevation="0" class="pa-6">
          <div class="text-h6 font-weight-bold mb-2">Welcome</div>
          <div class="text-body-1 text-medium-emphasis">
            This is your Project Pulse app shell. The layout is now ready for real pages like sections,
            rubrics, teams, WARs, and peer evaluations.
          </div>
        </v-card>
      </v-col>

      <v-col cols="12" md="4">
        <v-card rounded="xl" elevation="0" class="pa-6">
          <div class="text-h6 font-weight-bold mb-4">Next Build Targets</div>
          <v-list density="compact">
            <v-list-item title="Rubrics" />
            <v-list-item title="Teams" />
            <v-list-item title="WAR submissions" />
            <v-list-item title="Peer evaluations" />
          </v-list>
        </v-card>
      </v-col>
    </v-row>
  </AppShell>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import axios from 'axios'
import AppShell from '../layouts/AppShell.vue'

const stats = ref({
  sections: 0
})

async function loadStats() {
  try {
    const response = await axios.get('http://localhost:8080/api/sections')
    stats.value.sections = response.data.length
  } catch (error) {
    console.error('Failed to load stats', error)
  }
}

onMounted(loadStats)
</script>