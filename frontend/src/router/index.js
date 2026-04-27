import { createRouter, createWebHistory } from 'vue-router'
import AuthLayout from '../layouts/AuthLayout.vue'
import MainLayout from '../layouts/MainLayout.vue'
import LoginView from '../views/LoginView.vue'
import DashboardView from '../views/DashboardView.vue'
import SectionsView from '../views/admin/SectionsView.vue'
import SectionDetailView from '../views/admin/SectionDetailView.vue'
import SectionFormView from '../views/admin/SectionFormView.vue'
import ActiveWeeksView from '../views/admin/ActiveWeeksView.vue'
import TeamsView from '../views/admin/TeamsView.vue'
import TeamDetailView from '../views/admin/TeamDetailView.vue'
import TeamFormView from '../views/admin/TeamFormView.vue'
import RubricsView from '../views/admin/RubricsView.vue'
import RubricFormView from '../views/admin/RubricFormView.vue'

const routes = [
  { path: '/', redirect: '/login' },
  {
    path: '/',
    component: AuthLayout,
    children: [
      { path: 'login', component: LoginView },
    ],
  },
  {
    path: '/',
    component: MainLayout,
    children: [
      { path: 'dashboard', component: DashboardView },

      { path: 'sections', component: SectionsView },
      { path: 'sections/new', component: SectionFormView },
      { path: 'sections/:id', component: SectionDetailView, props: true },
      { path: 'sections/:id/edit', component: SectionFormView, props: true },
      { path: 'sections/:id/active-weeks', component: ActiveWeeksView, props: true },

      { path: 'teams', component: TeamsView },
      { path: 'teams/new', component: TeamFormView },
      { path: 'teams/:id', component: TeamDetailView, props: true },
      { path: 'teams/:id/edit', component: TeamFormView, props: true },

      { path: 'rubrics', component: RubricsView },
      { path: 'rubrics/new', component: RubricFormView },
      { path: 'rubrics/:id/edit', component: RubricFormView, props: true },
    ],
  },
]

export default createRouter({
  history: createWebHistory(),
  routes,
})