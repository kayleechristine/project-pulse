<template>
  <v-card variant="outlined" rounded="lg">
    <v-card-title class="pa-4 pb-2">{{ activity ? 'Edit Activity' : 'Add Activity' }}</v-card-title>
    <v-card-text class="pa-4 pt-2">
      <v-alert v-if="error" type="error" variant="tonal" class="mb-4" closable @click:close="error = ''">
        {{ error }}
      </v-alert>

      <v-select
        v-model="form.category"
        :items="categoryOptions"
        item-title="label"
        item-value="value"
        label="Category"
        variant="outlined"
        class="mb-3"
      />
      <v-textarea
        v-model="form.description"
        label="Description"
        variant="outlined"
        rows="3"
        class="mb-3"
      />
      <v-row>
        <v-col cols="6">
          <v-text-field
            v-model.number="form.plannedHours"
            label="Planned Hours"
            type="number"
            min="0"
            step="0.5"
            variant="outlined"
          />
        </v-col>
        <v-col cols="6">
          <v-text-field
            v-model.number="form.actualHours"
            label="Actual Hours"
            type="number"
            min="0"
            step="0.5"
            variant="outlined"
          />
        </v-col>
      </v-row>
      <v-select
        v-model="form.status"
        :items="statusOptions"
        item-title="label"
        item-value="value"
        label="Status"
        variant="outlined"
        class="mb-4"
      />

      <div class="d-flex ga-3">
        <v-btn color="primary" :loading="loading" @click="handleSubmit">
          {{ activity ? 'Save Changes' : 'Add Activity' }}
        </v-btn>
        <v-btn variant="text" :disabled="loading" @click="$emit('cancelled')">
          Cancel
        </v-btn>
      </div>
    </v-card-text>
  </v-card>
</template>

<script setup>
import { ref, watch } from 'vue'
import { addActivity, updateActivity } from '../api/war'

const props = defineProps({
  activity: { type: Object, default: null },
  weekId: { type: Number, required: true },
})

const emit = defineEmits(['saved', 'cancelled'])

const categoryOptions = [
  { value: 'DEVELOPMENT', label: 'Development' },
  { value: 'TESTING', label: 'Testing' },
  { value: 'BUGFIX', label: 'Bug Fix' },
  { value: 'COMMUNICATION', label: 'Communication' },
  { value: 'DOCUMENTATION', label: 'Documentation' },
  { value: 'DESIGN', label: 'Design' },
  { value: 'PLANNING', label: 'Planning' },
  { value: 'LEARNING', label: 'Learning' },
  { value: 'DEPLOYMENT', label: 'Deployment' },
  { value: 'SUPPORT', label: 'Support' },
  { value: 'MISCELLANEOUS', label: 'Miscellaneous' },
]

const statusOptions = [
  { value: 'IN_PROGRESS', label: 'In Progress' },
  { value: 'UNDER_TESTING', label: 'Under Testing' },
  { value: 'DONE', label: 'Done' },
]

const emptyForm = () => ({
  category: null,
  description: '',
  plannedHours: null,
  actualHours: null,
  status: null,
})

const form = ref(emptyForm())
const loading = ref(false)
const error = ref('')

watch(() => props.activity, (val) => {
  if (val) {
    form.value = {
      category: val.category,
      description: val.description,
      plannedHours: val.plannedHours,
      actualHours: val.actualHours,
      status: val.status,
    }
  } else {
    form.value = emptyForm()
  }
}, { immediate: true })

async function handleSubmit() {
  if (!form.value.category || !form.value.description || !form.value.plannedHours || !form.value.status) {
    error.value = 'Category, description, planned hours, and status are required.'
    return
  }

  error.value = ''
  loading.value = true
  try {
    const payload = { ...form.value, weekId: props.weekId }
    if (props.activity) {
      await updateActivity(props.activity.id, payload)
    } else {
      await addActivity(payload)
    }
    emit('saved')
  } catch (err) {
    error.value = err.response?.data?.message ?? 'Failed to save activity.'
  } finally {
    loading.value = false
  }
}
</script>
