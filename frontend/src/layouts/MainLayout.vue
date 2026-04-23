<template>
  <v-app>
    <v-navigation-drawer v-model="drawer" width="260" color="primary">
      <div class="pa-4 text-white">
        <div class="text-h6 font-weight-bold">Project Pulse</div>
        <div class="text-body-2 opacity-80">Senior Design Portal</div>
      </div>

      <v-divider color="white" class="mb-2" />

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
            <v-icon :icon="item.icon" color="white" />
          </template>
          <v-list-item-title class="text-white">
            {{ item.title }}
          </v-list-item-title>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-app-bar elevation="1" color="white">
      <v-app-bar-nav-icon @click="drawer = !drawer" />
      <v-toolbar-title class="font-weight-bold">Project Pulse</v-toolbar-title>

      <v-spacer />

      <v-btn variant="text" prepend-icon="mdi-bell-outline">Notifications</v-btn>
      <v-btn variant="text" prepend-icon="mdi-account-circle-outline">Profile</v-btn>
    </v-app-bar>

    <v-main class="bg-background">
      <div class="pa-6">
        <router-view />
      </div>
    </v-main>
  </v-app>
</template>

<script setup>
import { ref } from 'vue'

const drawer = ref(true)

const navItems = [
  { title: 'Dashboard', to: '/dashboard', icon: 'mdi-view-dashboard-outline' },
  { title: 'Sections', to: '/sections', icon: 'mdi-google-classroom' },
  { title: 'Teams', to: '/teams', icon: 'mdi-account-group-outline' },
  { title: 'Students', to: '/students', icon: 'mdi-school-outline' },
  { title: 'Reports', to: '/reports', icon: 'mdi-file-chart-outline' }
]
</script>