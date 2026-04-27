<template>
  <div>
    <div class="d-flex justify-space-between align-center mb-6">
      <div>
        <div class="text-h3 font-weight-bold">Rubrics</div>
        <div class="text-h6 text-medium-emphasis">
          Create and manage peer evaluation rubrics.
        </div>
      </div>

      <v-btn color="primary" to="/rubrics/new" prepend-icon="mdi-plus">
        Create Rubric
      </v-btn>
    </div>

    <v-alert v-if="error" type="error" variant="tonal" class="mb-4">
      {{ error }}
    </v-alert>

    <v-row>
      <v-col
        v-for="rubric in rubrics"
        :key="rubric.id"
        cols="12"
        md="6"
      >
        <v-card rounded="xl" elevation="1" class="pa-6">
          <div class="d-flex justify-space-between align-start mb-4">
            <div>
              <div class="text-h5 font-weight-bold">{{ rubric.name }}</div>
              <div class="text-body-1 text-medium-emphasis">
                {{ rubric.criteria?.length || 0 }} criteria
              </div>
            </div>

            <v-btn
              variant="outlined"
              size="small"
              :to="`/rubrics/${rubric.id}/edit`"
            >
              Edit
            </v-btn>
          </div>

          <div
            v-for="criterion in rubric.criteria"
            :key="criterion.id"
            class="mb-3"
          >
            <div class="font-weight-medium">
              {{ criterion.name }}
            </div>
            <div class="text-body-2 text-medium-emphasis">
              {{ criterion.description }} (Max: {{ criterion.maxScore }})
            </div>
          </div>
        </v-card>
      </v-col>

      <v-col v-if="rubrics.length === 0" cols="12">
        <v-card rounded="xl" elevation="1" class="pa-6 text-center text-medium-emphasis">
          No rubrics found. Create one to get started.
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { getRubrics } from '../../api/rubric'

const rubrics = ref([])
const error = ref('')

async function loadRubrics() {
  try {
    rubrics.value = await getRubrics()
  } catch (err) {
    error.value = err.message
  }
}

onMounted(loadRubrics)
</script>