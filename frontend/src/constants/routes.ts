export const ROUTES = {
  LOGIN: '/login',
  DASHBOARD: '/dashboard',
  TRANSACTIONS: '/transacoes',
  GOALS: '/metas',
  DEBT_SIMULATION: '/simulacao-quitacao',
  ANALYSIS: '/analise',
  AI: '/ia',
  REPORTS: '/relatorios',
  SETTINGS: '/configuracoes',
} as const;

export type AppRoute = typeof ROUTES[keyof typeof ROUTES];
