<template>
  <v-app>
    <v-navigation-drawer permanent width="260" color="primary">
      <div class="pa-6 text-white">
        <div class="text-h5 font-weight-bold">Project Pulse</div>
        <div class="text-body-2 mt-1" style="opacity: 0.8;">
          Senior Design Tracker
        </div>
      </div>

      <v-divider class="border-opacity-25" color="white" />

      <v-list nav density="comfortable" class="mt-2">
        <v-list-item
          v-for="item in navItems"
          :key="item.title"
          :to="item.to"
          rounded="lg"
          class="mx-3 mb-2 text-white"
        >
          <template #prepend>
            <v-icon color="white">{{ item.icon }}</v-icon>
          </template>

          <v-list-item-title>{{ item.title }}</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-main style="background: #F8FAFC; min-height: 100vh;">
      <v-app-bar flat color="surface" height="72">
        <v-container fluid class="d-flex align-center justify-space-between">
          <div>
            <div class="text-h6 font-weight-bold">{{ title }}</div>
            <div class="text-body-2 text-medium-emphasis">
              Project Pulse web app
            </div>
          </div>

          <v-chip color="accent" variant="flat">
            Local Development
          </v-chip>
        </v-container>
      </v-app-bar>

      <v-container fluid class="pa-6">
        <slot />
      </v-container>
    </v-main>
  </v-app>
</template>

<script setup>
defineProps({
  title: {
    type: String,
    default: 'Dashboard'
  }
})

const navItems = [
  { title: 'Dashboard', to: '/dashboard', icon: 'mdi-view-dashboard-outline' },
  { title: 'Sections', to: '/sections', icon: 'mdi-folder-outline' }
]
</script>