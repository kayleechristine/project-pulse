import { createVuetify } from 'vuetify'

export default createVuetify({
  theme: {
    defaultTheme: 'light',
    themes: {
      light: {
        colors: {
          primary: '#0F172A',
          secondary: '#1E293B',
          accent: '#2563EB',
          background: '#F8FAFC',
          surface: '#FFFFFF',
          success: '#16A34A',
          warning: '#D97706',
          error: '#DC2626'
        }
      }
    }
  }
})