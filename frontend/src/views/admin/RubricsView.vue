<template>
  <div>
    <div class="d-flex flex-wrap align-center justify-space-between mb-6 ga-3">
      <div>
        <div class="text-h3 font-weight-bold">Rubrics</div>
        <div class="text-h6 text-medium-emphasis">
          Create and manage peer evaluation rubrics.
        </div>
      </div>

      <v-btn color="primary" prepend-icon="mdi-plus" to="/rubrics/new">
        Create Rubric
      </v-btn>
    </div>

    <v-row>
      <v-col cols="12" md="6" v-for="rubric in rubrics" :key="rubric.id">
        <v-card rounded="xl" elevation="1" class="pa-5 h-100">
          <div class="d-flex justify-space-between align-center mb-3">
            <div class="text-h5 font-weight-bold">{{ rubric.name }}</div>
            <v-btn variant="outlined" size="small" :to="`/rubrics/${rubric.id}/edit`">Edit</v-btn>
          </div>

          <div class="text-body-1 mb-3">
            {{ rubric.criteria.length }} criteria
          </div>

          <v-list density="compact">
            <v-list-item
              v-for="criterion in rubric.criteria"
              :key="criterion.id"
              :title="criterion.name"
              :subtitle="`${criterion.description} (Max: ${criterion.maxScore})`"
            />
          </v-list>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<script setup>
import { rubrics } from '../../data/mockAdminData'
</script>