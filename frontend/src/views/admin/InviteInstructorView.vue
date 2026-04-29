<template>
  <div>
    <div class="mb-6">
      <div class="text-h3 font-weight-bold">Invite Instructors</div>
      <div class="text-h6 text-medium-emphasis">Send an email invitation to a new instructor.</div>
    </div>

    <UserInviteForm role="INSTRUCTOR" style="max-width: 600px" />
  </div>
</template>

<script setup>
import UserInviteForm from '../../components/UserInviteForm.vue'
</script>
