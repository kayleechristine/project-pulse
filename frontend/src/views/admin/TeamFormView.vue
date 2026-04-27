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
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getTeam, createTeam, updateTeam } from '../../api/teams'
import { getSections } from '../../api/sections'

const route = useRoute()
const router = useRouter()

const isEdit = computed(() => !!route.params.id)
const sectionOptions = ref([])
const error = ref('')

const form = ref({
  sectionId: null,
  name: '',
  description: '',
  websiteUrl: '',
})

async function loadData() {
  try {
    sectionOptions.value = await getSections()

    if (isEdit.value) {
      const team = await getTeam(route.params.id)

      form.value = {
        sectionId: team.sectionId ?? team.section?.id ?? null,
        name: team.name ?? '',
        description: team.description ?? '',
        websiteUrl: team.websiteUrl ?? '',
      }
    }
  } catch (err) {
    error.value = err.message
  }
}

async function saveTeam() {
  error.value = ''

  if (!form.value.sectionId) {
    error.value = 'Section is required.'
    return
  }

  if (!form.value.name) {
    error.value = 'Team name is required.'
    return
  }

  try {
    if (isEdit.value) {
      await updateTeam(route.params.id, form.value)
      alert('Team updated successfully')
    } else {
      await createTeam(form.value)
      alert('Team created successfully')
    }

    router.push('/teams')
  } catch (err) {
    error.value = err.message
  }
}

onMounted(loadData)
</script>