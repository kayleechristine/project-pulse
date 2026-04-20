<template>
  <AppShell title="Sections">
    <v-row>
      <v-col cols="12" md="5">
        <v-card rounded="xl" elevation="0" class="pa-6">
          <div class="text-h6 font-weight-bold mb-4">Create Section</div>

          <v-alert
            v-if="success"
            type="success"
            variant="tonal"
            class="mb-4"
          >
            {{ success }}
          </v-alert>

          <v-alert
            v-if="error"
            type="error"
            variant="tonal"
            class="mb-4"
          >
            {{ error }}
          </v-alert>

          <v-form @submit.prevent="createSection">
            <v-text-field
              v-model="form.sectionName"
              label="Section Name"
              placeholder="2025-2026"
              variant="outlined"
              class="mb-3"
            />

            <v-text-field
              v-model="form.startDate"
              label="Start Date"
              type="date"
              variant="outlined"
              class="mb-3"
            />

            <v-text-field
              v-model="form.endDate"
              label="End Date"
              type="date"
              variant="outlined"
              class="mb-4"
            />

            <v-btn color="accent" type="submit" block size="large">
              Create Section
            </v-btn>
          </v-form>
        </v-card>
      </v-col>

      <v-col cols="12" md="7">
        <v-card rounded="xl" elevation="0" class="pa-6">
          <div class="d-flex justify-space-between align-center mb-4">
            <div class="text-h6 font-weight-bold">All Sections</div>
            <v-btn variant="outlined" @click="loadSections">
              Refresh
            </v-btn>
          </div>

          <v-table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Section Name</th>
                <th>Start Date</th>
                <th>End Date</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="section in sections" :key="section.id">
                <td>{{ section.id }}</td>
                <td>{{ section.sectionName }}</td>
                <td>{{ section.startDate }}</td>
                <td>{{ section.endDate }}</td>
              </tr>
              <tr v-if="sections.length === 0">
                <td colspan="4" class="text-center text-medium-emphasis py-4">
                  No sections yet.
                </td>
              </tr>
            </tbody>
          </v-table>
        </v-card>
      </v-col>
    </v-row>
  </AppShell>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import axios from 'axios'
import AppShell from '../layouts/AppShell.vue'

const sections = ref([])
const success = ref('')
const error = ref('')

const form = reactive({
  sectionName: '',
  startDate: '',
  endDate: ''
})

async function loadSections() {
  try {
    const response = await axios.get('http://localhost:8080/api/sections')
    sections.value = response.data
  } catch (err) {
    error.value = 'Failed to load sections.'
  }
}

async function createSection() {
  success.value = ''
  error.value = ''

  try {
    await axios.post('http://localhost:8080/api/sections', form)
    success.value = 'Section created successfully.'
    form.sectionName = ''
    form.startDate = ''
    form.endDate = ''
    await loadSections()
  } catch (err) {
    error.value = err?.response?.data?.error || 'Failed to create section.'
  }
}

onMounted(loadSections)
</script>