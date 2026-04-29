<template>
  <div>
    <div class="mb-6">
      <div class="text-h3 font-weight-bold">Instructor Details</div>
      <div class="text-h6 text-medium-emphasis">Supervised teams and account status.</div>
    </div>

    <v-alert v-if="error" type="error" variant="tonal" class="mb-4">{{ error }}</v-alert>

    <template v-if="details">
      <v-card rounded="xl" elevation="1" class="pa-6 mb-4">
        <div class="text-h5 font-weight-bold">{{ details.instructor.firstName }} {{ details.instructor.lastName }}</div>
        <div class="text-body-1 text-medium-emphasis">{{ details.instructor.email }}</div>
        <v-chip class="mt-3" color="primary" variant="tonal">{{ details.accountStatus }}</v-chip>
      </v-card>

      <v-card rounded="xl" elevation="1" class="pa-6">
        <div class="text-h6 font-weight-bold mb-2">Supervised Teams</div>
        <v-table>
          <tbody>
            <tr v-for="team in details.teams" :key="team.id">
              <td>{{ team.name }}</td>
              <td>{{ team.description }}</td>
              <td class="text-right">
                <v-btn size="small" variant="text" :to="`/teams/${team.id}`">View Team</v-btn>
              </td>
            </tr>
            <tr v-if="details.teams.length === 0">
              <td class="text-medium-emphasis pa-4">No supervised teams.</td>
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
import { getInstructor } from '../../api/instructors'

const route = useRoute()
const details = ref(null)
const error = ref('')

async function loadInstructor() {
  try {
    const response = await getInstructor(route.params.id)
    details.value = response.data.data
  } catch (err) {
    error.value = err.response?.data?.message ?? 'Failed to load instructor.'
  }
}

onMounted(loadInstructor)
</script>
