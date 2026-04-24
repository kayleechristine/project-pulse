<template>
  <div>
    <div class="mb-6">
      <div class="text-h3 font-weight-bold">{{ isEdit ? 'Edit Team' : 'Create Team' }}</div>
      <div class="text-h6 text-medium-emphasis">
        {{ isEdit ? 'Update an existing senior design team.' : 'Create a new senior design team.' }}
      </div>
    </div>

    <v-card rounded="xl" elevation="1" class="pa-6">
      <v-form @submit.prevent="saveTeam">
        <v-row>
          <v-col cols="12" md="6">
            <v-select
              v-model="form.sectionId"
              label="Section"
              :items="sectionOptions"
              item-title="name"
              item-value="id"
              variant="outlined"
            />
          </v-col>

          <v-col cols="12" md="6">
            <v-text-field
              v-model="form.name"
              label="Team Name"
              variant="outlined"
            />
          </v-col>

          <v-col cols="12">
            <v-textarea
              v-model="form.description"
              label="Team Description"
              variant="outlined"
              rows="4"
            />
          </v-col>

          <v-col cols="12">
            <v-text-field
              v-model="form.websiteUrl"
              label="Team Website URL"
              variant="outlined"
            />
          </v-col>
        </v-row>

        <v-alert v-if="error" type="error" variant="tonal" class="mb-4">
          {{ error }}
        </v-alert>

        <div class="d-flex ga-2">
          <v-btn color="primary" type="submit">
            {{ isEdit ? 'Save Changes' : 'Create Team' }}
          </v-btn>
          <v-btn variant="outlined" to="/teams">Cancel</v-btn>
        </div>
      </v-form>
    </v-card>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import { sections, teams, getTeamById } from '../../data/mockAdminData'

const props = defineProps({
  id: String,
})

const router = useRouter()
const isEdit = computed(() => !!props.id)
const existing = computed(() => isEdit.value ? getTeamById(props.id) : null)
const sectionOptions = sections
const error = ref('')

const form = ref({
  sectionId: existing.value?.sectionId ?? null,
  name: existing.value?.name ?? '',
  description: existing.value?.description ?? '',
  websiteUrl: existing.value?.websiteUrl ?? '',
})

function saveTeam() {
  error.value = ''

  if (!form.value.sectionId || !form.value.name || !form.value.description || !form.value.websiteUrl) {
    error.value = 'Please complete all fields.'
    return
  }

  const duplicate = teams.find(team =>
    team.name.toLowerCase() === form.value.name.toLowerCase() &&
    team.id !== existing.value?.id
  )

  if (duplicate) {
    error.value = 'Team name must be unique.'
    return
  }

  alert(isEdit.value ? 'Team updated (mock only).' : 'Team created (mock only).')
  router.push('/teams')
}
</script>