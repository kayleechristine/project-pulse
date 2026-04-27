<template>
  <v-app>
    <v-main class="bg-grey-lighten-4">
      <v-container class="fill-height d-flex align-center justify-center">
        <v-sheet width="440" class="pa-8 rounded-lg elevation-2">
          <div class="text-center mb-6">
            <div class="text-h5 font-weight-bold text-primary">Project Pulse</div>
            <div class="text-body-2 text-medium-emphasis">Senior Design Portal</div>
          </div>
          <router-view />
        </v-sheet>
      </v-container>
    </v-main>
  </v-app>
</template>
