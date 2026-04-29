<template>
  <div>
    <v-alert type="success" variant="tonal" class="mb-6" icon="mdi-check-circle">
      Evaluation submitted. You cannot edit it after submission.
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
          <td>{{ scoreFor(criterion.id) }}</td>
        </tr>
      </tbody>
    </v-table>

    <v-textarea
      :model-value="submission.publicComments"
      label="Public Comments (visible to teammates)"
      variant="outlined"
      rows="3"
      class="mb-3"
      readonly
    />
    <v-textarea
      :model-value="submission.privateComments"
      label="Private Comments (visible to instructor only)"
      variant="outlined"
      rows="3"
      readonly
    />
  </div>
</template>

<script setup>
const props = defineProps({
  submission: { type: Object, required: true },
  criteria: { type: Array, required: true },
})

function scoreFor(criterionId) {
  return props.submission.scores.find(s => s.criterionId === criterionId)?.score ?? '—'
}
</script>
