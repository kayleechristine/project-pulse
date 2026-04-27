<template>
  <div>
    <div class="mb-6">
      <div class="text-h3 font-weight-bold">Section Details</div>
      <div class="text-h6 text-medium-emphasis">
        View a senior design section.
      </div>
    </div>

    <v-alert v-if="error" type="error" variant="tonal" class="mb-4">
      {{ error }}
    </v-alert>

    <v-card v-if="section" rounded="xl" elevation="1" class="pa-6">
      <div class="text-h4 font-weight-bold mb-2">{{ section.name }}</div>

      <div class="mb-2">
        <strong>Start Date:</strong> {{ section.startDate }}
      </div>

      <div class="mb-2">
        <strong>End Date:</strong> {{ section.endDate }}
      </div>

      <div class="mb-2">
        <strong>Rubric:</strong> {{ getRubricName(section.rubricId) }}
      </div>

      <div class="d-flex ga-2 mt-6">
        <v-btn color="primary" :to="`/sections/${section.id}/edit`">Edit Section</v-btn>
        <v-btn variant="outlined" :to="`/sections/${section.id}/active-weeks`">Active Weeks</v-btn>
        <v-btn variant="outlined" to="/sections">Back</v-btn>
      </div>
    </v-card>

    <v-card v-else rounded="xl" elevation="1" class="pa-6">
      Loading section...
    </v-card>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { getSection } from '../../api/sections'
import { getRubrics } from '../../api/rubric'

const route = useRoute()

const section = ref(null)
const rubrics = ref([])
const error = ref('')

function getRubricName(rubricId) {
  const rubric = rubrics.value.find(r => r.id === rubricId)
  return rubric?.name ?? 'N/A'
}

async function loadData() {
  try {
    section.value = await getSection(route.params.id)
    rubrics.value = await getRubrics()
  } catch (err) {
    error.value = err.message
  }
}

onMounted(loadData)
</script>