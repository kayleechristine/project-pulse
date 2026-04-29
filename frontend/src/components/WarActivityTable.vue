<template>
  <v-table>
    <thead>
      <tr>
        <th>Category</th>
        <th>Description</th>
        <th>Planned Hours</th>
        <th>Actual Hours</th>
        <th>Status</th>
        <th style="width: 96px">Actions</th>
      </tr>
    </thead>
    <tbody>
      <tr v-if="activities.length === 0">
        <td colspan="6" class="text-center text-medium-emphasis py-6">
          No activities logged for this week.
        </td>
      </tr>
      <tr v-for="activity in activities" :key="activity.id">
        <td>
          <v-chip :color="categoryColor(activity.category)" size="small" variant="tonal">
            {{ categoryLabel(activity.category) }}
          </v-chip>
        </td>
        <td>{{ activity.description }}</td>
        <td>{{ activity.plannedHours }}</td>
        <td>{{ activity.actualHours ?? '—' }}</td>
        <td>{{ statusLabel(activity.status) }}</td>
        <td style="width: 96px; white-space: nowrap">
          <v-btn icon size="small" variant="text" @click="$emit('edit', activity)">
            <v-icon icon="mdi-pencil-outline" />
          </v-btn>
          <v-btn icon size="small" variant="text" color="error" @click="$emit('delete', activity)">
            <v-icon icon="mdi-delete-outline" />
          </v-btn>
        </td>
      </tr>
    </tbody>
  </v-table>
</template>

<script setup>
defineProps({
  activities: { type: Array, required: true },
})

defineEmits(['edit', 'delete'])

const CATEGORY_LABELS = {
  DEVELOPMENT: 'Development', TESTING: 'Testing', BUGFIX: 'Bug Fix',
  COMMUNICATION: 'Communication', DOCUMENTATION: 'Documentation', DESIGN: 'Design',
  PLANNING: 'Planning', LEARNING: 'Learning', DEPLOYMENT: 'Deployment',
  SUPPORT: 'Support', MISCELLANEOUS: 'Miscellaneous',
}

const CATEGORY_COLORS = {
  DEVELOPMENT: 'blue',
  TESTING: 'teal',
  BUGFIX: 'red',
  COMMUNICATION: 'cyan',
  DOCUMENTATION: 'purple',
  DESIGN: 'pink',
  PLANNING: 'indigo',
  LEARNING: 'green',
  DEPLOYMENT: 'orange',
  SUPPORT: 'amber',
  MISCELLANEOUS: 'grey',
}

const STATUS_LABELS = {
  IN_PROGRESS: 'In Progress',
  UNDER_TESTING: 'Under Testing',
  DONE: 'Done',
}

function categoryLabel(val) { return CATEGORY_LABELS[val] ?? val }
function categoryColor(val) { return CATEGORY_COLORS[val] ?? 'grey' }
function statusLabel(val) { return STATUS_LABELS[val] ?? val }
</script>
