<template>
  <div>
    <div class="text-h6 font-weight-bold mb-6">Sign In</div>

    <v-alert v-if="successMessage" type="success" variant="tonal" class="mb-4" closable>
      {{ successMessage }}
    </v-alert>

    <v-alert v-if="errorMessage" type="error" variant="tonal" class="mb-4" closable>
      {{ errorMessage }}
    </v-alert>

    <v-form @submit.prevent="handleLogin">
      <v-text-field
        v-model="email"
        label="Email"
        type="email"
        variant="outlined"
        class="mb-3"
        :disabled="loading"
        required
      />
      <v-text-field
        v-model="password"
        label="Password"
        type="password"
        variant="outlined"
        class="mb-5"
        :disabled="loading"
        required
      />
      <v-btn
        type="submit"
        color="primary"
        block
        size="large"
        :loading="loading"
      >
        Sign In
      </v-btn>
    </v-form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const email = ref('')
const password = ref('')
const loading = ref(false)
const errorMessage = ref('')
const successMessage = ref(route.query.registered ? 'Account created successfully. Please sign in.' : '')

async function handleLogin() {
  errorMessage.value = ''
  loading.value = true

  try {
    await authStore.login(email.value, password.value)

    const role = authStore.role
    if (role === 'STUDENT') {
      router.push('/student/dashboard')
    } else {
      router.push('/dashboard')
    }
  } catch (err) {
    errorMessage.value = err.response?.data?.message ?? 'Login failed. Please try again.'
  } finally {
    loading.value = false
  }
}
</script>
