<template>
  <div>
    <div class="mb-6">
      <div class="text-h3 font-weight-bold">{{ isEdit ? 'Edit Rubric' : 'Create Rubric' }}</div>
      <div class="text-h6 text-medium-emphasis">
        Build the rubric criteria used for peer evaluations.
      </div>
    </div>

    <v-card rounded="xl" elevation="1" class="pa-6">
      <v-form @submit.prevent="saveRubric">
        <v-text-field
          v-model="form.name"
          label="Rubric Name"
          variant="outlined"
          class="mb-4"
        />

        <div class="d-flex justify-space-between align-center mb-4">
          <div class="text-h5 font-weight-bold">Criteria</div>
          <v-btn color="primary" variant="tonal" prepend-icon="mdi-plus" @click="addCriterion">
            Add Criterion
          </v-btn>
        </div>

        <v-card
          v-for="(criterion, index) in form.criteria"
          :key="index"
          rounded="lg"
          variant="outlined"
          class="pa-4 mb-4"
        >
          <div class="d-flex justify-space-between align-center mb-3">
            <div class="text-h6 font-weight-bold">Criterion {{ index + 1 }}</div>
            <v-btn
              variant="text"
              color="error"
              icon="mdi-delete-outline"
              @click="removeCriterion(index)"
            />
          </div>

          <v-row>
            <v-col cols="12" md="4">
              <v-text-field v-model="criterion.name" label="Criterion Name" variant="outlined" />
            </v-col>
            <v-col cols="12" md="6">
              <v-text-field v-model="criterion.description" label="Description" variant="outlined" />
            </v-col>
            <v-col cols="12" md="2">
              <v-text-field
                v-model.number="criterion.maxScore"
                type="number"
                label="Max Score"
                variant="outlined"
              />
            </v-col>
          </v-row>
        </v-card>

        <v-alert v-if="error" type="error" variant="tonal" class="mb-4">
          {{ error }}
        </v-alert>

        <div class="d-flex ga-2">
          <v-btn color="primary" type="submit">
            {{ isEdit ? 'Save Changes' : 'Create Rubric' }}
          </v-btn>
          <v-btn variant="outlined" to="/rubrics">Cancel</v-btn>
        </div>
      </v-form>
    </v-card>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import { createRubric } from '../../api/rubric'

const props = defineProps({
  id: String,
})

const router = useRouter()
const isEdit = computed(() => !!props.id)
const error = ref('')

const form = ref({
  name: '',
  criteria: [
    { name: '', description: '', maxScore: 10 },
  ],
})

function addCriterion() {
  form.value.criteria.push({
    name: '',
    description: '',
    maxScore: 10,
  })
}

function removeCriterion(index) {
  form.value.criteria.splice(index, 1)
}

async function saveRubric() {
  error.value = ''

  if (!form.value.name) {
    error.value = 'Rubric name is required.'
    return
  }

  if (form.value.criteria.length === 0) {
    error.value = 'At least one criterion is required.'
    return
  }

  for (const criterion of form.value.criteria) {
    if (!criterion.name || !criterion.description || !criterion.maxScore) {
      error.value = 'Each criterion must have a name, description, and max score.'
      return
    }

    if (Number(criterion.maxScore) <= 0) {
      error.value = 'Criterion max score must be positive.'
      return
    }
  }

  try {
    await createRubric(form.value)
    alert('Rubric saved successfully')
    router.push('/rubrics')
  } catch (err) {
    error.value = err.message
  }
}
</script>