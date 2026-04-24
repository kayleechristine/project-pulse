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
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import { sections, rubrics, getSectionById } from '../../data/mockAdminData'

const props = defineProps({
  id: String,
})

const router = useRouter()
const isEdit = computed(() => !!props.id)
const existing = computed(() => isEdit.value ? getSectionById(props.id) : null)

const rubricOptions = rubrics
const error = ref('')

const form = ref({
  name: existing.value?.name ?? '',
  startDate: existing.value?.startDate ?? '',
  endDate: existing.value?.endDate ?? '',
  rubricId: existing.value?.rubricId ?? null,
})

function saveSection() {
  error.value = ''

  if (!form.value.name || !form.value.startDate || !form.value.endDate || !form.value.rubricId) {
    error.value = 'Please complete all fields.'
    return
  }

  if (form.value.startDate > form.value.endDate) {
    error.value = 'Start date cannot be after end date.'
    return
  }

  const duplicate = sections.find(section =>
    section.name.toLowerCase() === form.value.name.toLowerCase() &&
    section.id !== existing.value?.id
  )

  if (duplicate) {
    error.value = 'Section name must be unique.'
    return
  }

  alert(isEdit.value ? 'Section updated (mock only).' : 'Section created (mock only).')
  router.push('/sections')
}
</script>