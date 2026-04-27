<template>
  <div>
    <v-alert v-if="success" type="success" variant="tonal" class="mb-4" closable @click:close="success = false">
      Evaluation submitted for {{ teammate.name }}.
    </v-alert>
    <v-alert v-if="error" type="error" variant="tonal" class="mb-4" closable @click:close="error = ''">
      {{ error }}
    </v-alert>

    <v-table class="mb-4">
      <thead>
        <tr>
          <th>Criterion</th>
          <th style="width: 140px">Score (1–10)</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="criterion in criteria" :key="criterion.id">
          <td>{{ criterion.name }}</td>
          <td>
            <v-text-field
              v-model.number="scores[criterion.id]"
              type="number"
              min="1"
              max="10"
              density="compact"
              variant="outlined"
              hide-details
              style="max-width: 100px"
            />
          </td>
        </tr>
      </tbody>
    </v-table>

    <v-textarea
      v-model="publicComments"
      label="Public Comments (visible to teammates)"
      variant="outlined"
      rows="3"
      class="mb-3"
    />
    <v-textarea
      v-model="privateComments"
      label="Private Comments (visible to instructor only)"
      variant="outlined"
      rows="3"
      class="mb-5"
    />

    <v-btn color="primary" :loading="loading" @click="handleSubmit">
      Submit Evaluation
    </v-btn>
  </div>
</template>

<script setup>
import { ref, reactive, watch } from 'vue'
import { submitEvaluation } from '../api/peerEval'

const props = defineProps({
  teammate: { type: Object, required: true },
  criteria: { type: Array, required: true },
  weekId: { type: Number, required: true },
})

const emit = defineEmits(['submitted'])

const scores = reactive({})
const publicComments = ref('')
const privateComments = ref('')
const loading = ref(false)
const success = ref(false)
const error = ref('')

watch(() => props.teammate.id, () => {
  for (const key in scores) delete scores[key]
  publicComments.value = ''
  privateComments.value = ''
  success.value = false
  error.value = ''
})

async function handleSubmit() {
  error.value = ''
  success.value = false

  const scoreEntries = props.criteria.map(c => ({
    criterionId: c.id,
    score: scores[c.id] ?? 0,
  }))

  const invalid = scoreEntries.find(e => e.score < 1 || e.score > 10)
  if (invalid) {
    error.value = 'All scores must be between 1 and 10.'
    return
  }

  loading.value = true
  try {
    await submitEvaluation({
      evaluateeId: props.teammate.id,
      weekId: props.weekId,
      publicComments: publicComments.value,
      privateComments: privateComments.value,
      scores: scoreEntries,
    })
    success.value = true
    emit('submitted', props.teammate.id)
  } catch (err) {
    error.value = err.response?.data?.message ?? 'Submission failed. Please try again.'
  } finally {
    loading.value = false
  }
}
</script>
