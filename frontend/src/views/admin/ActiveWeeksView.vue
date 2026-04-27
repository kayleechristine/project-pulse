<template>
  <div v-if="section">
    <div class="mb-6">
      <div class="text-h3 font-weight-bold">Set Up Active Weeks</div>
      <div class="text-h6 text-medium-emphasis">{{ section.name }}</div>
    </div>

    <v-card rounded="xl" elevation="1" class="pa-6">
      <div class="text-body-1 mb-4">
        Select the weeks students do <strong>not</strong> need to submit WARs and peer evaluations.
      </div>

      <v-row>
        <v-col cols="12" md="4" v-for="week in generatedWeeks" :key="week">
          <v-checkbox
            v-model="selectedInactiveWeeks"
            :label="formatDate(week)"
            :value="week"
            hide-details
          />
        </v-col>
      </v-row>

      <div class="d-flex ga-2 mt-6">
        <v-btn color="primary" @click="saveWeeks">Save Active Weeks</v-btn>
        <v-btn variant="outlined" :to="`/sections/${section.id}`">Back to Section</v-btn>
      </div>
    </v-card>
  </div>

  <v-alert v-else type="error" variant="tonal">
    Section not found.
  </v-alert>
</template>

<script setup>
import { computed, ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getSection } from '../../api/sections'
import { saveActiveWeeks } from '../../api/activeWeeks'

const route = useRoute()

const section = ref(null)
const selectedInactiveWeeks = ref([])

async function loadSection() {
  try {
    section.value = await getSection(route.params.id)
    selectedInactiveWeeks.value = section.value.inactiveWeeks ?? []
  } catch (err) {
    alert(err.message)
  }
}

const generatedWeeks = computed(() => {
  if (!section.value) return []

  const weeks = []
  const start = new Date(section.value.startDate)
  const end = new Date(section.value.endDate)
  const current = new Date(start)

  while (current <= end) {
    weeks.push(current.toISOString().split('T')[0])
    current.setDate(current.getDate() + 7)
  }

  return weeks
})

function formatDate(date) {
  return new Date(date).toLocaleDateString()
}

async function saveWeeks() {
  try {
    await saveActiveWeeks(route.params.id, selectedInactiveWeeks.value)
    alert('Active weeks saved successfully')
  } catch (err) {
    alert(err.message)
  }
}

onMounted(loadSection)
</script>