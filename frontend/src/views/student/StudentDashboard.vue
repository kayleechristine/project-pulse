<template>
  <div>
    <div class="text-h5 font-weight-bold mb-1">Welcome, {{ authStore.user?.firstName }}</div>
    <div class="text-body-2 text-medium-emphasis mb-6">Senior Design Portal</div>

    <v-row>
      <v-col cols="12" md="4">
        <v-card variant="outlined" rounded="lg" :to="'/student/war'">
          <v-card-item>
            <template #prepend>
              <v-icon icon="mdi-clipboard-text-outline" color="primary" size="28" />
            </template>
            <v-card-title>Weekly Activity Report</v-card-title>
            <v-card-subtitle>Log your work for the week</v-card-subtitle>
          </v-card-item>
        </v-card>
      </v-col>

      <v-col cols="12" md="4">
        <v-card variant="outlined" rounded="lg" :to="'/student/peer-eval'">
          <v-card-item>
            <template #prepend>
              <v-icon icon="mdi-message-star-outline" color="primary" size="28" />
            </template>
            <v-card-title>Peer Evaluation</v-card-title>
            <v-card-subtitle>Evaluate your teammates</v-card-subtitle>
            <template v-if="evalBadge" #append>
              <v-chip
                :color="evalBadge.color"
                size="small"
                variant="tonal"
                :prepend-icon="evalBadge.icon"
              >
                {{ evalBadge.label }}
              </v-chip>
            </template>
          </v-card-item>
        </v-card>
      </v-col>

      <v-col cols="12" md="4">
        <v-card variant="outlined" rounded="lg" :to="'/student/report'">
          <v-card-item>
            <template #prepend>
              <v-icon icon="mdi-file-chart-outline" color="primary" size="28" />
            </template>
            <v-card-title>My Report</v-card-title>
            <v-card-subtitle>View your peer evaluation scores</v-card-subtitle>
          </v-card-item>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useAuthStore } from '../../stores/auth'
import { getMyTeam } from '../../api/teams'
import { getActiveWeeksBySection } from '../../api/activeWeeks'
import { getMySubmissions } from '../../api/peerEval'

const authStore = useAuthStore()

const evalStatus = ref(null)

const evalBadge = computed(() => {
  if (!evalStatus.value) return null
  const { submitted, total } = evalStatus.value
  if (total === 0) return null
  if (submitted === total) {
    return { label: 'Complete', color: 'success', icon: 'mdi-check' }
  }
  return { label: `${submitted} / ${total} submitted`, color: 'warning', icon: 'mdi-alert-circle-outline' }
})

onMounted(async () => {
  try {
    const teamRes = await getMyTeam()
    const teamData = teamRes.data.data

    const weeksRes = await getActiveWeeksBySection(teamData.sectionId)
    const activeWeek = weeksRes.data.find(w => w.active)
    if (!activeWeek) return

    const submissionsRes = await getMySubmissions(activeWeek.id)
    evalStatus.value = {
      submitted: submissionsRes.data.data.length,
      total: teamData.teammates.length,
    }
  } catch {
    // no badge if team or week can't be resolved
  }
})
</script>
