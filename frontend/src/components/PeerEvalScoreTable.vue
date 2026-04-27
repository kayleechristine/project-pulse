<template>
  <v-table>
    <thead>
      <tr>
        <th>Criterion</th>
        <th>Average Score</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="row in rows" :key="row.criterionId">
        <td>{{ row.name }}</td>
        <td>{{ row.avg.toFixed(1) }}</td>
      </tr>
    </tbody>
  </v-table>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  criterionAverages: { type: Object, required: true },
  criteria: { type: Array, required: true },
})

const rows = computed(() =>
  props.criteria.map(c => ({
    criterionId: c.id,
    name: c.name,
    avg: props.criterionAverages[c.id] ?? 0,
  }))
)
</script>
