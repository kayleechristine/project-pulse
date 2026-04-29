<template>
  <v-card variant="outlined" rounded="lg">
    <v-card-title class="pa-4 pb-2">Invite Student</v-card-title>
    <v-card-text class="pa-4 pt-2">
      <v-alert v-if="error" type="error" variant="tonal" class="mb-4" closable @click:close="error = ''">
        {{ error }}
      </v-alert>
      <v-alert v-if="success" type="success" variant="tonal" class="mb-4" closable @click:close="success = ''">
        {{ success }}
      </v-alert>

      <v-text-field
        v-model="email"
        label="Student Email"
        type="email"
        variant="outlined"
        class="mb-4"
      />

      <v-btn color="primary" :loading="loading" @click="handleSubmit">
        Send Invitation
      </v-btn>
    </v-card-text>
  </v-card>
</template>

<script setup>
import { ref } from 'vue'
import { sendInvitation } from '../api/invitations'

const emit = defineEmits(['invited'])

const email = ref('')
const loading = ref(false)
const error = ref('')
const success = ref('')

async function handleSubmit() {
  if (!email.value) {
    error.value = 'Email is required.'
    return
  }

  error.value = ''
  success.value = ''
  loading.value = true
  try {
    await sendInvitation(email.value, 'STUDENT')
    success.value = `Invitation sent to ${email.value}.`
    emit('invited', email.value)
    email.value = ''
  } catch (err) {
    error.value = err.response?.data?.message ?? 'Failed to send invitation.'
  } finally {
    loading.value = false
  }
}
</script>
