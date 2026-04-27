<template>
  <div>
    <div class="text-h5 font-weight-bold mb-1">Welcome, {{ authStore.user?.firstName }}</div>
    <div class="text-body-2 text-medium-emphasis mb-6">Senior Design Portal</div>

    <v-row>
      <v-col cols="12" md="4">
        <v-card variant="outlined" rounded="lg" :to="'/student/war'">
          <v-card-item>
            <template #prepend>
              <v-icon icon="mdi-clipboard-text-outline" color="primary" size="28" />
            </template>
            <v-card-title>Weekly Activity Report</v-card-title>
            <v-card-subtitle>Log your work for the week</v-card-subtitle>
          </v-card-item>
        </v-card>
      </v-col>

      <v-col cols="12" md="4">
        <v-card variant="outlined" rounded="lg" :to="'/student/peer-eval'">
          <v-card-item>
            <template #prepend>
              <v-icon icon="mdi-message-star-outline" color="primary" size="28" />
            </template>
            <v-card-title>Peer Evaluation</v-card-title>
            <v-card-subtitle>Evaluate your teammates</v-card-subtitle>
          </v-card-item>
        </v-card>
      </v-col>

      <v-col cols="12" md="4">
        <v-card variant="outlined" rounded="lg" :to="'/student/report'">
          <v-card-item>
            <template #prepend>
              <v-icon icon="mdi-file-chart-outline" color="primary" size="28" />
            </template>
            <v-card-title>My Report</v-card-title>
            <v-card-subtitle>View your peer evaluation scores</v-card-subtitle>
          </v-card-item>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<script setup>
import { useAuthStore } from '../../stores/auth'

const authStore = useAuthStore()
</script>
