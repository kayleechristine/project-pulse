import { createRouter, createWebHistory } from 'vue-router'
import AuthLayout from '../layouts/AuthLayout.vue'
import MainLayout from '../layouts/MainLayout.vue'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import StudentDashboard from '../views/student/StudentDashboard.vue'
import AccountSettingsView from '../views/student/AccountSettingsView.vue'
import PeerEvalSubmitView from '../views/student/PeerEvalSubmitView.vue'
import PeerEvalReportView from '../views/student/PeerEvalReportView.vue'
import WarView from '../views/student/WarView.vue'
import WarReportView from '../views/student/WarReportView.vue'
import DashboardView from '../views/DashboardView.vue'

import SectionsView from '../views/admin/SectionsView.vue'
import SectionDetailView from '../views/admin/SectionDetailView.vue'
import SectionFormView from '../views/admin/SectionFormView.vue'
import TeamsView from '../views/admin/TeamsView.vue'
import TeamDetailView from '../views/admin/TeamDetailView.vue'
import TeamFormView from '../views/admin/TeamFormView.vue'
import RubricsView from '../views/admin/RubricsView.vue'
import RubricFormView from '../views/admin/RubricFormView.vue'
import ActiveWeeksView from '../views/admin/ActiveWeeksView.vue'
import InviteStudentView from '../views/admin/InviteStudentView.vue'
import StudentsView from '../views/admin/StudentsView.vue'
import StudentDetailView from '../views/admin/StudentDetailView.vue'
import InviteInstructorView from '../views/admin/InviteInstructorView.vue'
import InstructorsView from '../views/admin/InstructorsView.vue'
import InstructorDetailView from '../views/admin/InstructorDetailView.vue'
import TeamStudentsView from '../views/admin/TeamStudentsView.vue'
import TeamWarReportView from '../views/instructor/TeamWarReportView.vue'
import SectionReportView from '../views/instructor/SectionReportView.vue'
import StudentPeerReportView from '../views/instructor/StudentPeerReportView.vue'
import StudentWarReportView from '../views/instructor/StudentWarReportView.vue'

const PlaceholderView = {
  template: `
    <div class="pa-6">
      <h1>Coming Soon</h1>
      <p>This page is ready for your teammate's use case implementation.</p>
    </div>
  `
}

const routes = [
  { path: '/', redirect: '/login' },
  {
    path: '/',
    component: AuthLayout,
    children: [
      { path: 'login', component: LoginView },
      { path: 'register', component: RegisterView },
    ],
  },
  {
    path: '/student',
    component: MainLayout,
    children: [
      { path: 'dashboard', component: StudentDashboard },
      { path: 'account', component: AccountSettingsView },
      { path: 'peer-eval', component: PeerEvalSubmitView },
      { path: 'report', component: PeerEvalReportView },
      { path: 'war', component: WarView },
      { path: 'war-report', component: WarReportView },
    ],
  },
  {
    path: '/',
    component: MainLayout,
    children: [
      { path: 'dashboard', component: DashboardView },

      { path: 'sections', component: SectionsView },
      { path: 'sections/new', component: SectionFormView },
      { path: 'sections/:id', component: SectionDetailView },
      { path: 'sections/:id/edit', component: SectionFormView },
      { path: 'sections/:id/active-weeks', component: ActiveWeeksView },

      { path: 'teams', component: TeamsView },
      { path: 'teams/new', component: TeamFormView },
      { path: 'teams/:id', component: TeamDetailView },
      { path: 'teams/:id/edit', component: TeamFormView },
      { path: 'teams/:id/students', component: TeamStudentsView },

      { path: 'rubrics', component: RubricsView },
      { path: 'rubrics/new', component: RubricFormView },

      { path: 'students', component: StudentsView },
      { path: 'students/invite', component: InviteStudentView },
      { path: 'students/:id', component: StudentDetailView },
      { path: 'instructors', component: InstructorsView },
      { path: 'instructors/invite', component: InviteInstructorView },
      { path: 'instructors/:id', component: InstructorDetailView },
      { path: 'reports', component: TeamWarReportView },
      { path: 'reports/section-peer-eval', component: SectionReportView },
      { path: 'reports/student-peer-eval', component: StudentPeerReportView },
      { path: 'reports/student-war', component: StudentWarReportView }
    ]
  }
]

export default createRouter({
  history: createWebHistory(),
  routes
})
