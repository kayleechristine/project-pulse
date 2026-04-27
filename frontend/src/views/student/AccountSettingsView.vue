<template>
  <div>
    <div class="text-h5 font-weight-bold mb-1">Account Settings</div>
    <div class="text-body-2 text-medium-emphasis mb-6">Update your personal information.</div>

    <v-alert v-if="successMessage" type="success" variant="tonal" class="mb-4" closable>
      {{ successMessage }}
    </v-alert>

    <v-alert v-if="errorMessage" type="error" variant="tonal" class="mb-4" closable>
      {{ errorMessage }}
    </v-alert>

    <v-card variant="outlined" rounded="lg" max-width="560">
      <v-card-text class="pa-6">
        <v-form @submit.prevent="handleSave">
          <v-text-field
            v-model="form.firstName"
            label="First Name"
            variant="outlined"
            class="mb-3"
            :disabled="loading"
            required
          />
          <v-text-field
            v-model="form.lastName"
            label="Last Name"
            variant="outlined"
            class="mb-3"
            :disabled="loading"
            required
          />
          <v-text-field
            v-model="form.email"
            label="Email"
            type="email"
            variant="outlined"
            class="mb-5"
            :disabled="loading"
            required
          />
          <div class="d-flex ga-3">
            <v-btn
              type="submit"
              color="primary"
              :loading="loading"
            >
              Save Changes
            </v-btn>
            <v-btn variant="text" :disabled="loading" @click="resetForm">
              Cancel
            </v-btn>
          </div>
        </v-form>
      </v-card-text>
    </v-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getProfile, updateProfile } from '../../api/users'
import { useAuthStore } from '../../stores/auth'

const authStore = useAuthStore()

const form = ref({ firstName: '', lastName: '', email: '' })
const original = ref({ firstName: '', lastName: '', email: '' })
const loading = ref(false)
const successMessage = ref('')
const errorMessage = ref('')

onMounted(async () => {
  try {
    const response = await getProfile()
    const data = response.data.data
    form.value = { firstName: data.firstName, lastName: data.lastName, email: data.email }
    original.value = { ...form.value }
  } catch {
    errorMessage.value = 'Failed to load profile. Please refresh the page.'
  }
})

function resetForm() {
  form.value = { ...original.value }
  successMessage.value = ''
  errorMessage.value = ''
}

async function handleSave() {
  successMessage.value = ''
  errorMessage.value = ''
  loading.value = true

  try {
    const response = await updateProfile(form.value)
    const data = response.data.data

    form.value = { firstName: data.firstName, lastName: data.lastName, email: data.email }
    original.value = { ...form.value }

    authStore.user = { ...authStore.user, ...form.value }
    localStorage.setItem('user', JSON.stringify(authStore.user))

    successMessage.value = 'Your profile has been updated.'
  } catch (err) {
    errorMessage.value = err.response?.data?.message ?? 'Update failed. Please try again.'
  } finally {
    loading.value = false
  }
}
</script>
