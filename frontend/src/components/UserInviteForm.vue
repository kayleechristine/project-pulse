<template>
  <v-card variant="outlined" rounded="lg">
    <v-card-title class="pa-4 pb-2">{{ title }}</v-card-title>
    <v-card-text class="pa-4 pt-2">
      <v-alert v-if="error" type="error" variant="tonal" class="mb-4" closable @click:close="error = ''">
        {{ error }}
      </v-alert>

      <v-alert v-if="inviteLink" type="success" variant="tonal" class="mb-4" closable @click:close="inviteLink = ''">
        Invitation sent. If the email doesn't arrive, share this link directly:
        <div class="mt-2">
          <a :href="inviteLink" target="_blank" class="text-break">{{ inviteLink }}</a>
        </div>
      </v-alert>

      <v-text-field
        v-model="email"
        :label="emailLabel"
        type="email"
        variant="outlined"
        class="mb-4"
      />

      <v-select
        v-if="role === 'STUDENT'"
        v-model="sectionId"
        :items="sections"
        item-title="sectionName"
        item-value="id"
        label="Section"
        variant="outlined"
        clearable
        class="mb-4"
      />

      <v-btn color="primary" :loading="loading" @click="handleSubmit">
        Send Invitation
      </v-btn>
    </v-card-text>
  </v-card>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { inviteStudent } from '../api/students'
import { inviteInstructor } from '../api/instructors'
import { getSections } from '../api/sections'

const props = defineProps({
  role: {
    type: String,
    default: 'STUDENT',
  },
})

const emit = defineEmits(['invited'])

const email = ref('')
const sectionId = ref(null)
const sections = ref([])
const loading = ref(false)
const error = ref('')
const inviteLink = ref('')
const role = computed(() => props.role)
const title = computed(() => role.value === 'INSTRUCTOR' ? 'Invite Instructor' : 'Invite Student')
const emailLabel = computed(() => role.value === 'INSTRUCTOR' ? 'Instructor Email' : 'Student Email')

onMounted(async () => {
  if (role.value !== 'STUDENT') {
    return
  }

  try {
    sections.value = await getSections()
  } catch {
    sections.value = []
  }
})

async function handleSubmit() {
  if (!email.value) {
    error.value = 'Email is required.'
    return
  }

  error.value = ''
  inviteLink.value = ''
  loading.value = true
  try {
    const response = role.value === 'INSTRUCTOR'
      ? await inviteInstructor(email.value)
      : await inviteStudent(email.value, sectionId.value)
    const token = response.data.data
    inviteLink.value = `${window.location.origin}/register?token=${token}`
    emit('invited', email.value)
    email.value = ''
  } catch (err) {
    error.value = err.response?.data?.message ?? 'Failed to send invitation.'
  } finally {
    loading.value = false
  }
}
</script>
