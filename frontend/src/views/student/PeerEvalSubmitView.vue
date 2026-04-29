<template>
  <div>
    <div class="text-h5 font-weight-bold mb-1">Peer Evaluation</div>
    <div class="text-body-2 text-medium-emphasis mb-6">
      Evaluate each of your teammates for Week {{ weekId }}.
    </div>

    <v-alert v-if="error" type="error" variant="tonal" class="mb-4">{{ error }}</v-alert>

    <div v-if="loading" class="d-flex justify-center py-8">
      <v-progress-circular indeterminate color="primary" />
    </div>

    <template v-else-if="teammates.length > 0">
      <v-tabs v-model="activeTab" color="primary" class="mb-6">
        <v-tab v-for="teammate in teammates" :key="teammate.id" :value="teammate.id">
          {{ teammate.name }}
          <v-icon
            v-if="submissions[teammate.id]"
            icon="mdi-check-circle"
            color="success"
            size="18"
            class="ml-1"
          />
        </v-tab>
      </v-tabs>

      <v-window v-model="activeTab">
        <v-window-item v-for="teammate in teammates" :key="teammate.id" :value="teammate.id">
          <PeerEvalSubmittedSummary
            v-if="submissions[teammate.id]"
            :submission="submissions[teammate.id]"
            :criteria="criteria"
          />
          <PeerEvalForm
            v-else
            :teammate="teammate"
            :criteria="criteria"
            :week-id="weekId"
            @submitted="onSubmitted"
          />
        </v-window-item>
      </v-window>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import PeerEvalForm from '../../components/PeerEvalForm.vue'
import PeerEvalSubmittedSummary from '../../components/PeerEvalSubmittedSummary.vue'
import { getMyTeam } from '../../api/teams'
import { getActiveWeeksBySection } from '../../api/activeWeeks'
import { getRubrics } from '../../api/rubric'
import { getMySubmissions } from '../../api/peerEval'

const weekId = ref(null)
const teammates = ref([])
const criteria = ref([])
const submissions = ref({})
const loading = ref(true)
const error = ref('')
const activeTab = ref(null)

async function loadSubmissions() {
  if (!weekId.value) return
  const res = await getMySubmissions(weekId.value)
  const map = {}
  for (const sub of res.data.data) {
    map[sub.evaluateeId] = sub
  }
  submissions.value = map
}

onMounted(async () => {
  try {
    const [teamRes, rubricRes] = await Promise.all([getMyTeam(), getRubrics()])

    const teamData = teamRes.data.data
    const rubrics = rubricRes.data

    const weeksRes = await getActiveWeeksBySection(teamData.sectionId)
    const activeWeek = weeksRes.data.find(w => w.active)
    if (!activeWeek) {
      error.value = 'No active week found. Peer evaluations are not currently open.'
      return
    }

    weekId.value = activeWeek.id
    teammates.value = teamData.teammates.map(t => ({
      id: t.id,
      name: `${t.firstName} ${t.lastName}`,
    }))
    criteria.value = rubrics[0]?.criteria ?? []

    await loadSubmissions()

    if (teammates.value.length > 0) {
      activeTab.value = teammates.value[0].id
    }
  } catch (err) {
    error.value = err.response?.data?.message ?? 'Failed to load peer evaluation.'
  } finally {
    loading.value = false
  }
})

async function onSubmitted() {
  await loadSubmissions()
}
</script>
