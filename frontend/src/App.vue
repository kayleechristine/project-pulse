<template>
  <div style="font-family: Arial, sans-serif; max-width: 900px; margin: 40px auto;">
    <h1>Project Pulse</h1>

    <h2>Create Section</h2>
    <form @submit.prevent="createSection">
      <div style="margin-bottom: 12px;">
        <input v-model="form.sectionName" placeholder="Section Name" />
      </div>
      <div style="margin-bottom: 12px;">
        <input v-model="form.startDate" type="date" />
      </div>
      <div style="margin-bottom: 12px;">
        <input v-model="form.endDate" type="date" />
      </div>
      <button type="submit">Create</button>
    </form>

    <p v-if="message">{{ message }}</p>

    <h2>Sections</h2>
    <button @click="loadSections">Refresh</button>
    <ul>
      <li v-for="section in sections" :key="section.id">
        {{ section.sectionName }} | {{ section.startDate }} to {{ section.endDate }}
      </li>
    </ul>
  </div>
</template>

<script setup>
import axios from 'axios'
import { onMounted, reactive, ref } from 'vue'

const sections = ref([])
const message = ref('')

const form = reactive({
  sectionName: '',
  startDate: '',
  endDate: ''
})

async function loadSections() {
  const response = await axios.get('http://localhost:8080/api/sections')
  sections.value = response.data
}

async function createSection() {
  try {
    await axios.post('http://localhost:8080/api/sections', form)
    message.value = 'Section created successfully.'
    form.sectionName = ''
    form.startDate = ''
    form.endDate = ''
    await loadSections()
  } catch (error) {
    message.value = error?.response?.data?.error || 'Failed to create section.'
  }
}

onMounted(loadSections)
</script>