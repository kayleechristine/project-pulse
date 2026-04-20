import { createRouter, createWebHistory } from 'vue-router'
import DashboardView from '../views/DashboardView.vue'
import SectionsView from '../views/SectionsView.vue'

const routes = [
  { path: '/', redirect: '/dashboard' },
  { path: '/dashboard', component: DashboardView },
  { path: '/sections', component: SectionsView }
]

export default createRouter({
  history: createWebHistory(),
  routes
})