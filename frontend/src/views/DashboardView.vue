<template>
  <div>
    <div class="d-flex flex-wrap align-center justify-space-between mb-6 ga-3">
      <div>
        <div class="text-h4 font-weight-bold">Dashboard</div>
        <div class="text-body-1 text-medium-emphasis">
          Overview of sections, teams, students, and reports.
        </div>
      </div>

      <div class="d-flex ga-2">
        <v-btn color="primary" prepend-icon="mdi-plus" to="/sections">
          New Section
        </v-btn>
        <v-btn variant="outlined" prepend-icon="mdi-account-group-outline">
          Create Team
        </v-btn>
      </div>
    </div>

    <v-row>
      <v-col cols="12" md="3">
        <v-card rounded="xl" elevation="1" class="pa-4">
          <div class="text-overline">Sections</div>
          <div class="text-h4 font-weight-bold">4</div>
          <div class="text-body-2 text-medium-emphasis">Active academic sections</div>
        </v-card>
      </v-col>

      <v-col cols="12" md="3">
        <v-card rounded="xl" elevation="1" class="pa-4">
          <div class="text-overline">Teams</div>
          <div class="text-h4 font-weight-bold">18</div>
          <div class="text-body-2 text-medium-emphasis">Senior design teams</div>
        </v-card>
      </v-col>

      <v-col cols="12" md="3">
        <v-card rounded="xl" elevation="1" class="pa-4">
          <div class="text-overline">Students</div>
          <div class="text-h4 font-weight-bold">92</div>
          <div class="text-body-2 text-medium-emphasis">Registered students</div>
        </v-card>
      </v-col>

      <v-col cols="12" md="3">
        <v-card rounded="xl" elevation="1" class="pa-4">
          <div class="text-overline">Pending Reports</div>
          <div class="text-h4 font-weight-bold">11</div>
          <div class="text-body-2 text-medium-emphasis">WAR / peer evaluation follow-up</div>
        </v-card>
      </v-col>
    </v-row>

    <v-row class="mt-2">
      <v-col cols="12" lg="7">
        <v-card rounded="xl" elevation="1">
          <v-card-title class="font-weight-bold">Quick Actions</v-card-title>
          <v-card-text>
            <div class="d-flex flex-wrap ga-3">
              <v-btn color="primary" variant="tonal" prepend-icon="mdi-file-document-edit-outline">
                Weekly Activity Reports
              </v-btn>
              <v-btn color="primary" variant="tonal" prepend-icon="mdi-star-box-outline">
                Peer Evaluations
              </v-btn>
              <v-btn color="primary" variant="tonal" prepend-icon="mdi-chart-box-outline">
                Generate Reports
              </v-btn>
              <v-btn color="primary" variant="tonal" prepend-icon="mdi-email-outline">
                Invite Users
              </v-btn>
            </div>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" lg="5">
        <v-card rounded="xl" elevation="1">
          <v-card-title class="font-weight-bold">Recent Activity</v-card-title>
          <v-list lines="two">
            <v-list-item
              title="Section 2025-2026 updated"
              subtitle="Rubric and active weeks were modified"
            />
            <v-list-item
              title="Team assignments published"
              subtitle="Students were assigned to teams"
            />
            <v-list-item
              title="WAR report generated"
              subtitle="Instructor report is ready to review"
            />
          </v-list>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<script setup>
</script>