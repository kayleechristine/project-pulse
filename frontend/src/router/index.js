import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '../layouts/MainLayout.vue'
import DashboardView from '../views/DashboardView.vue'
import SectionsView from '../views/SectionsView.vue'

const PlaceholderView = {
  template: `
    <v-card rounded="xl" elevation="1" class="pa-6">
      <div class="text-h5 font-weight-bold mb-2">Coming Soon</div>
      <div class="text-body-1 text-medium-emphasis">
        This page is ready for your use case implementation.
      </div>
    </v-card>
  `
}

const routes = [
  { path: '/', redirect: '/dashboard' },
  {
    path: '/',
    component: MainLayout,
    children: [
      { path: 'dashboard', component: DashboardView },
      { path: 'sections', component: SectionsView },
      { path: 'teams', component: PlaceholderView },
      { path: 'students', component: PlaceholderView },
      { path: 'reports', component: PlaceholderView }
    ]
  }
]

export default createRouter({
  history: createWebHistory(),
  routes
})