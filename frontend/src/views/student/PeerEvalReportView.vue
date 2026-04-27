<template>
  <div>
    <div class="text-h5 font-weight-bold mb-1">My Peer Evaluation Report</div>
    <div class="text-body-2 text-medium-emphasis mb-6">Week {{ weekId }}</div>

    <v-alert v-if="error" type="error" variant="tonal" class="mb-4">{{ error }}</v-alert>

    <div v-if="loading" class="d-flex justify-center py-8">
      <v-progress-circular indeterminate color="primary" />
    </div>

    <template v-else-if="report">
      <v-card variant="outlined" rounded="lg" class="mb-6" max-width="280">
        <v-card-item>
          <v-card-title class="text-h3 font-weight-bold">{{ report.grade.toFixed(1) }}</v-card-title>
          <v-card-subtitle>Peer Evaluation Grade</v-card-subtitle>
        </v-card-item>
      </v-card>

      <div class="text-h6 font-weight-medium mb-3">Scores by Criterion</div>
      <PeerEvalScoreTable
        :criterion-averages="report.criterionAverages"
        :criteria="MOCK_CRITERIA"
        class="mb-6"
        style="max-width: 500px"
      />

      <div class="text-h6 font-weight-medium mb-3">Comments from Teammates</div>
      <div v-if="report.publicComments.length === 0" class="text-medium-emphasis text-body-2">
        No comments received yet.
      </div>
      <v-card
        v-for="(comment, i) in report.publicComments"
        :key="i"
        variant="tonal"
        rounded="lg"
        class="mb-2"
        max-width="600"
      >
        <v-card-text>{{ comment }}</v-card-text>
      </v-card>
    </template>

    <template v-else-if="!loading && !error">
      <div class="text-medium-emphasis text-body-2">No report available for this week yet.</div>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyReport } from '../../api/peerEval'
import PeerEvalScoreTable from '../../components/PeerEvalScoreTable.vue'

const MOCK_CRITERIA = [
  { id: 1, name: 'Contribution to Team Goals' },
  { id: 2, name: 'Quality of Work' },
  { id: 3, name: 'Communication' },
  { id: 4, name: 'Reliability / Dependability' },
  { id: 5, name: 'Collaboration / Teamwork' },
  { id: 6, name: 'Initiative / Problem Solving' },
]

const weekId = 1
const report = ref(null)
const loading = ref(true)
const error = ref('')

onMounted(async () => {
  try {
    const response = await getMyReport(weekId)
    report.value = response.data.data
  } catch (err) {
    error.value = err.response?.data?.message ?? 'Failed to load report.'
  } finally {
    loading.value = false
  }
})
</script>
