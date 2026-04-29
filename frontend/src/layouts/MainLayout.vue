<template>
  <v-app>
    <v-navigation-drawer :rail="rail" width="280" rail-width="56" color="primary" permanent>
      <div style="height: 76px; overflow: hidden; position: relative;">
        <v-fade-transition>
          <div
            v-if="!rail"
            class="text-white"
            style="position: absolute; top: 0; bottom: 0; left: 0; right: 56px; display: flex; flex-direction: column; justify-content: center; padding-left: 16px; overflow: hidden;"
          >
            <div class="text-h5 font-weight-bold" style="white-space: nowrap;">Project Pulse</div>
            <div class="text-subtitle-1" style="white-space: nowrap;">Senior Design Portal</div>
          </div>
        </v-fade-transition>
        <div style="position: absolute; right: 0; top: 0; bottom: 0; width: 56px; display: flex; align-items: center; justify-content: center;">
          <v-btn
            :icon="rail ? 'mdi-menu' : 'mdi-close'"
            variant="text"
            color="white"
            :ripple="false"
            @click="rail = !rail"
          />
        </div>
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

const rail = ref(false)
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
  if (authStore.role === 'INSTRUCTOR') {
    return [
      { title: 'Dashboard', to: '/dashboard', icon: 'mdi-view-dashboard-outline' },
      { title: 'Reports', to: '/reports', icon: 'mdi-file-chart-outline' },
    ]
  }
  return [
    { title: 'Dashboard', to: '/dashboard', icon: 'mdi-view-dashboard-outline' },
    { title: 'Sections', to: '/sections', icon: 'mdi-google-classroom' },
    { title: 'Teams', to: '/teams', icon: 'mdi-account-group-outline' },
    { title: 'Rubrics', to: '/rubrics', icon: 'mdi-clipboard-text-outline' },
    { title: 'Students', to: '/students', icon: 'mdi-school-outline' },
    { title: 'Instructors', to: '/instructors', icon: 'mdi-account-tie-outline' },
  ]
})
</script>
