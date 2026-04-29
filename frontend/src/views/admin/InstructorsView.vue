<template>
  <div>
    <div class="d-flex justify-space-between align-center mb-6">
      <div>
        <div class="text-h3 font-weight-bold">Instructors</div>
        <div class="text-h6 text-medium-emphasis">Search, invite, and manage instructor accounts.</div>
      </div>

      <v-btn color="primary" to="/instructors/invite" prepend-icon="mdi-email-plus">
        Invite Instructor
      </v-btn>
    </div>

    <v-alert v-if="error" type="error" variant="tonal" class="mb-4">{{ error }}</v-alert>

    <v-card rounded="xl" elevation="1">
      <div class="pa-4">
        <v-text-field
          v-model="search"
          label="Search instructors"
          prepend-inner-icon="mdi-magnify"
          variant="outlined"
          density="comfortable"
          hide-details
          @keyup.enter="loadInstructors"
        />
      </div>
      <v-table>
        <thead>
          <tr>
            <th>Name</th>
            <th>Email</th>
            <th>Status</th>
            <th class="text-right">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="instructor in instructors" :key="instructor.id">
            <td>{{ instructor.firstName }} {{ instructor.lastName }}</td>
            <td>{{ instructor.email }}</td>
            <td>{{ instructor.accountStatus }}</td>
            <td class="text-right">
              <v-btn size="small" variant="text" :to="`/instructors/${instructor.id}`">View</v-btn>
              <v-btn
                v-if="instructor.accountStatus === 'ACTIVE'"
                size="small"
                variant="text"
                color="warning"
                @click="deactivate(instructor)"
              >
                Deactivate
              </v-btn>
              <v-btn
                v-else
                size="small"
                variant="text"
                color="primary"
                @click="reactivate(instructor)"
              >
                Reactivate
              </v-btn>
            </td>
          </tr>
          <tr v-if="instructors.length === 0">
            <td colspan="4" class="text-center pa-6 text-medium-emphasis">No instructors found.</td>
          </tr>
        </tbody>
      </v-table>
    </v-card>
  </div>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue'
import { deactivateInstructor, reactivateInstructor, searchInstructors } from '../../api/instructors'

const instructors = ref([])
const search = ref('')
const error = ref('')

async function loadInstructors() {
  try {
    const response = await searchInstructors(search.value)
    instructors.value = response.data.data
  } catch (err) {
    error.value = err.response?.data?.message ?? 'Failed to load instructors.'
  }
}

async function deactivate(instructor) {
  try {
    await deactivateInstructor(instructor.id)
    await loadInstructors()
  } catch (err) {
    error.value = err.response?.data?.message ?? 'Failed to deactivate instructor.'
  }
}

async function reactivate(instructor) {
  try {
    await reactivateInstructor(instructor.id)
    await loadInstructors()
  } catch (err) {
    error.value = err.response?.data?.message ?? 'Failed to reactivate instructor.'
  }
}

watch(search, () => loadInstructors())
onMounted(loadInstructors)
</script>
