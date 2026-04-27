<template>
  <div>
    <div class="text-h6 font-weight-bold mb-6">Create Your Account</div>

    <v-alert v-if="tokenError" type="error" variant="tonal" class="mb-4">
      {{ tokenError }}
      <div class="mt-2">
        <router-link to="/login">Go to login</router-link>
      </div>
    </v-alert>

    <v-alert v-if="errorMessage" type="error" variant="tonal" class="mb-4" closable>
      {{ errorMessage }}
    </v-alert>

    <v-form v-if="!tokenError" @submit.prevent="handleRegister">
      <v-text-field
        v-model="email"
        label="Email"
        variant="outlined"
        class="mb-3"
        readonly
        hint="Your email is set by your invitation."
        persistent-hint
      />
      <v-text-field
        v-model="firstName"
        label="First Name"
        variant="outlined"
        class="mb-3"
        :disabled="loading"
        required
      />
      <v-text-field
        v-model="lastName"
        label="Last Name"
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
        class="mb-3"
        :disabled="loading"
        hint="At least 8 characters."
        persistent-hint
        required
      />
      <v-text-field
        v-model="confirmPassword"
        label="Confirm Password"
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
        :disabled="tokenLoading"
      >
        Create Account
      </v-btn>
    </v-form>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { validateInviteToken, register } from '../api/auth'

const router = useRouter()
const route = useRoute()

const email = ref('')
const firstName = ref('')
const lastName = ref('')
const password = ref('')
const confirmPassword = ref('')
const loading = ref(false)
const tokenLoading = ref(true)
const tokenError = ref('')
const errorMessage = ref('')

onMounted(async () => {
  const token = route.query.token

  if (!token) {
    tokenError.value = 'No invitation token found. Please use the link from your invitation email.'
    tokenLoading.value = false
    return
  }

  try {
    const response = await validateInviteToken(token)
    email.value = response.data.data
  } catch (err) {
    tokenError.value = err.response?.data?.message ?? 'This invitation link is invalid or has already been used.'
  } finally {
    tokenLoading.value = false
  }
})

async function handleRegister() {
  errorMessage.value = ''

  if (password.value !== confirmPassword.value) {
    errorMessage.value = 'Passwords do not match.'
    return
  }

  loading.value = true

  try {
    await register(route.query.token, firstName.value, lastName.value, password.value)
    router.push('/login?registered=true')
  } catch (err) {
    errorMessage.value = err.response?.data?.message ?? 'Registration failed. Please try again.'
  } finally {
    loading.value = false
  }
}
</script>
