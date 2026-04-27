<template>
  <div>
    <div class="text-h5 font-weight-bold mb-1">Peer Evaluation</div>
    <div class="text-body-2 text-medium-emphasis mb-6">
      Evaluate each of your teammates for Week {{ MOCK_WEEK_ID }}.
    </div>

    <v-tabs v-model="activeTab" color="primary" class="mb-6">
      <v-tab v-for="teammate in MOCK_TEAMMATES" :key="teammate.id" :value="teammate.id">
        {{ teammate.name }}
        <v-icon
          v-if="submitted.includes(teammate.id)"
          icon="mdi-check-circle"
          color="success"
          size="18"
          class="ml-1"
        />
      </v-tab>
    </v-tabs>

    <v-window v-model="activeTab">
      <v-window-item v-for="teammate in MOCK_TEAMMATES" :key="teammate.id" :value="teammate.id">
        <PeerEvalForm
          :teammate="teammate"
          :criteria="MOCK_CRITERIA"
          :week-id="MOCK_WEEK_ID"
          @submitted="onSubmitted"
        />
      </v-window-item>
    </v-window>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import PeerEvalForm from '../../components/PeerEvalForm.vue'

const MOCK_WEEK_ID = 1

const MOCK_TEAMMATES = [
  { id: 10, name: 'John Doe' },
  { id: 11, name: 'Tim Smith' },
  { id: 12, name: 'Lily Fisher' },
]

const MOCK_CRITERIA = [
  { id: 1, name: 'Contribution to Team Goals' },
  { id: 2, name: 'Quality of Work' },
  { id: 3, name: 'Communication' },
  { id: 4, name: 'Reliability / Dependability' },
  { id: 5, name: 'Collaboration / Teamwork' },
  { id: 6, name: 'Initiative / Problem Solving' },
]

const activeTab = ref(MOCK_TEAMMATES[0].id)
const submitted = ref([])

function onSubmitted(teammateId) {
  if (!submitted.value.includes(teammateId)) {
    submitted.value.push(teammateId)
  }
}
</script>
