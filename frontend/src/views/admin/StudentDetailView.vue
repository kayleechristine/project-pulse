<template>
  <div>
    <div class="mb-6">
      <div class="text-h3 font-weight-bold">Student Details</div>
      <div class="text-h6 text-medium-emphasis">Team, WARs, and peer evaluation records.</div>
    </div>

    <v-alert v-if="error" type="error" variant="tonal" class="mb-4">{{ error }}</v-alert>

    <template v-if="details">
      <v-card rounded="xl" elevation="1" class="pa-6 mb-4">
        <div class="text-h5 font-weight-bold">{{ details.student.firstName }} {{ details.student.lastName }}</div>
        <div class="text-body-1 text-medium-emphasis">{{ details.student.email }}</div>
        <v-chip class="mt-3" color="primary" variant="tonal">{{ details.student.accountStatus }}</v-chip>
      </v-card>

      <v-card rounded="xl" elevation="1" class="pa-6 mb-4">
        <div class="text-h6 font-weight-bold mb-2">Team</div>
        <div v-if="details.team">{{ details.team.name }}</div>
        <div v-else class="text-medium-emphasis">No team assigned.</div>
      </v-card>

      <v-card rounded="xl" elevation="1" class="pa-6 mb-4">
        <div class="text-h6 font-weight-bold mb-2">WARs</div>
        <v-table density="compact">
          <tbody>
            <tr v-for="war in details.wars" :key="war.id">
              <td>{{ war.weekId }}</td>
              <td>{{ war.category }}</td>
              <td>{{ war.status }}</td>
              <td>{{ war.plannedHours }} planned / {{ war.actualHours ?? 0 }} actual</td>
            </tr>
            <tr v-if="details.wars.length === 0">
              <td class="text-medium-emphasis pa-4">No WAR activity found.</td>
            </tr>
          </tbody>
        </v-table>
      </v-card>

      <v-card rounded="xl" elevation="1" class="pa-6">
        <div class="text-h6 font-weight-bold mb-2">Peer Evaluations</div>
        <v-table density="compact">
          <tbody>
            <tr v-for="evaluation in details.peerEvaluations" :key="evaluation.id">
              <td>Week {{ evaluation.weekId }}</td>
              <td>Evaluator {{ evaluation.evaluatorId }}</td>
              <td>Evaluatee {{ evaluation.evaluateeId }}</td>
              <td>{{ evaluation.submittedAt }}</td>
            </tr>
            <tr v-if="details.peerEvaluations.length === 0">
              <td class="text-medium-emphasis pa-4">No peer evaluations found.</td>
            </tr>
          </tbody>
        </v-table>
      </v-card>
    </template>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { getStudent } from '../../api/students'

const route = useRoute()
const details = ref(null)
const error = ref('')

async function loadStudent() {
  try {
    const response = await getStudent(route.params.id)
    details.value = response.data.data
  } catch (err) {
    error.value = err.response?.data?.message ?? 'Failed to load student.'
  }
}

onMounted(loadStudent)
</script>
