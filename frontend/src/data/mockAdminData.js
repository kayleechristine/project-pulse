export const rubrics = [
    {
      id: 1,
      name: 'Peer Eval Rubric v1',
      criteria: [
        {
          id: 1,
          name: 'Quality of work',
          description: 'How do you rate the quality of this teammate’s work?',
          maxScore: 10,
        },
        {
          id: 2,
          name: 'Productivity',
          description: 'How productive is this teammate?',
          maxScore: 10,
        },
        {
          id: 3,
          name: 'Initiative',
          description: 'How proactive is this teammate?',
          maxScore: 10,
        },
        {
          id: 4,
          name: 'Courtesy',
          description: 'Does this teammate treat others with respect?',
          maxScore: 10,
        },
        {
          id: 5,
          name: 'Open-mindedness',
          description: 'How well does this teammate handle criticism?',
          maxScore: 10,
        },
        {
          id: 6,
          name: 'Engagement in meetings',
          description: 'How is this teammate’s performance during meetings?',
          maxScore: 10,
        },
      ],
    },
  ]
  
  export const sections = [
    {
      id: 1,
      name: '2025-2026',
      startDate: '2025-08-25',
      endDate: '2026-05-01',
      rubricId: 1,
      inactiveWeeks: ['2025-12-22', '2025-12-29'],
    },
    {
      id: 2,
      name: '2024-2025',
      startDate: '2024-08-26',
      endDate: '2025-05-02',
      rubricId: 1,
      inactiveWeeks: ['2024-12-23', '2024-12-30'],
    },
  ]
  
  export const teams = [
    {
      id: 1,
      sectionId: 1,
      name: 'Project Pulse Team',
      description: 'Admin and reporting platform for senior design.',
      websiteUrl: 'https://projectpulse.team',
    },
    {
      id: 2,
      sectionId: 1,
      name: 'Peer Eval Tool Team',
      description: 'Peer evaluation workflow and grading.',
      websiteUrl: 'https://example.com/peer-eval',
    },
    {
      id: 3,
      sectionId: 2,
      name: 'Legacy Team',
      description: 'Previous academic year team.',
      websiteUrl: 'https://example.com/legacy',
    },
  ]
  
  export function getRubricById(id) {
    return rubrics.find(r => r.id === Number(id))
  }
  
  export function getSectionById(id) {
    return sections.find(s => s.id === Number(id))
  }
  
  export function getTeamsBySectionId(sectionId) {
    return teams.filter(t => t.sectionId === Number(sectionId))
  }
  
  export function getTeamById(id) {
    return teams.find(t => t.id === Number(id))
  }