<template>
  <v-app>
    <v-navigation-drawer v-model="drawer" width="280" color="primary">
      <div class="pa-4 text-white">
        <div class="text-h5 font-weight-bold">Project Pulse</div>
        <div class="text-subtitle-1">Senior Design Portal</div>
      </div>

      <v-divider class="mb-2" />

      <v-list nav density="comfortable">
        <v-list-item
          v-for="item in navItems"
          :key="item.title"
          :to="item.to"
          rounded="lg"
          color="white"
          class="mb-1"
        >
          <template #prepend>
            <v-icon :icon="item.icon" />
          </template>
          <v-list-item-title>{{ item.title }}</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-app-bar elevation="1" color="white">
      <v-app-bar-nav-icon @click="drawer = !drawer" />
      <v-toolbar-title class="font-weight-bold">Project Pulse</v-toolbar-title>
      <v-spacer />
      <v-menu>
        <template #activator="{ props }">
          <v-btn variant="text" prepend-icon="mdi-account-circle-outline" v-bind="props">
            {{ authStore.user?.firstName }} {{ authStore.user?.lastName }}
          </v-btn>
        </template>
        <v-list>
          <v-list-item
            v-if="authStore.role === 'STUDENT'"
            to="/student/account"
            prepend-icon="mdi-cog-outline"
            title="Account Settings"
          />
          <v-list-item
            prepend-icon="mdi-logout"
            title="Logout"
            @click="handleLogout"
          />
        </v-list>
      </v-menu>
    </v-app-bar>

    <v-main class="bg-background">
      <div class="pa-6">
        <router-view />
      </div>
    </v-main>
  </v-app>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const drawer = ref(true)
const authStore = useAuthStore()
const router = useRouter()

function handleLogout() {
  authStore.logout()
  router.push('/login')
}

const navItems = computed(() => {
  if (authStore.role === 'STUDENT') {
    return [
      { title: 'Dashboard', to: '/student/dashboard', icon: 'mdi-view-dashboard-outline' },
      { title: 'Peer Evaluation', to: '/student/peer-eval', icon: 'mdi-message-star-outline' },
      { title: 'My Report', to: '/student/report', icon: 'mdi-file-chart-outline' },
      { title: 'Weekly Activity Report', to: '/student/war', icon: 'mdi-clipboard-text-outline' },
    ]
  }
  return [
    { title: 'Dashboard', to: '/dashboard', icon: 'mdi-view-dashboard-outline' },
    { title: 'Sections', to: '/sections', icon: 'mdi-google-classroom' },
    { title: 'Teams', to: '/teams', icon: 'mdi-account-group-outline' },
    { title: 'Rubrics', to: '/rubrics', icon: 'mdi-clipboard-text-outline' },
    { title: 'Students', to: '/students', icon: 'mdi-school-outline' },
    { title: 'Reports', to: '/reports', icon: 'mdi-file-chart-outline' },
  ]
})
</script>