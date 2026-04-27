<template>
  <div>
    <div class="mb-6">
      <div class="text-h3 font-weight-bold">{{ isEdit ? 'Edit Section' : 'Create Section' }}</div>
      <div class="text-h6 text-medium-emphasis">
        {{ isEdit ? 'Update an existing senior design section.' : 'Create a new senior design section.' }}
      </div>
    </div>

    <v-card rounded="xl" elevation="1" class="pa-6">
      <v-form @submit.prevent="saveSection">
        <v-row>
          <v-col cols="12" md="6">
            <v-text-field v-model="form.name" label="Section Name" variant="outlined" />
          </v-col>

          <v-col cols="12" md="3">
            <v-text-field v-model="form.startDate" label="Start Date" type="date" variant="outlined" />
          </v-col>

          <v-col cols="12" md="3">
            <v-text-field v-model="form.endDate" label="End Date" type="date" variant="outlined" />
          </v-col>

          <v-col cols="12">
            <v-select
              v-model="form.rubricId"
              label="Rubric"
              :items="rubricOptions"
              item-title="name"
              item-value="id"
              variant="outlined"
            />
          </v-col>
        </v-row>

        <v-alert v-if="error" type="error" variant="tonal" class="mb-4">
          {{ error }}
        </v-alert>

        <div class="d-flex ga-2">
          <v-btn color="primary" type="submit">
            {{ isEdit ? 'Save Changes' : 'Create Section' }}
          </v-btn>
          <v-btn variant="outlined" to="/sections">Cancel</v-btn>
        </div>
      </v-form>
    </v-card>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getSection, createSection, updateSection } from '../../api/sections'
import { getRubrics } from '../../api/rubric'

const route = useRoute()
const router = useRouter()

const isEdit = computed(() => !!route.params.id)
const error = ref('')
const rubricOptions = ref([])

const form = ref({
  name: '',
  startDate: '',
  endDate: '',
  rubricId: null,
})

async function loadData() {
  try {
    rubricOptions.value = await getRubrics()

    if (isEdit.value) {
      const section = await getSection(route.params.id)

      form.value = {
        name: section.name ?? '',
        startDate: section.startDate ?? '',
        endDate: section.endDate ?? '',
        rubricId: section.rubricId ?? section.rubric?.id ?? null,
      }
    }
  } catch (err) {
    error.value = err.message
  }
}

async function saveSection() {
  error.value = ''

  if (!form.value.name) {
    error.value = 'Section name is required.'
    return
  }

  if (!form.value.startDate || !form.value.endDate) {
    error.value = 'Start date and end date are required.'
    return
  }

  if (!form.value.rubricId) {
    error.value = 'Rubric is required.'
    return
  }

  try {
    if (isEdit.value) {
      await updateSection(route.params.id, form.value)
      alert('Section updated successfully')
    } else {
      await createSection(form.value)
      alert('Section created successfully')
    }

    router.push('/sections')
  } catch (err) {
    error.value = err.message
  }
}

onMounted(loadData)
</script>