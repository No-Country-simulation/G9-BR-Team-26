export interface UserProfile {
  id: string;
  name: string;
  email: string;
  phone: string;
  cpfCnpj: string;
  avatarUrl: string;
  occupation: string;
  companyName: string;
}

export interface UserSettings {
  theme: 'light' | 'dark' | 'system';
  language: 'pt-BR' | 'en-US' | 'es-ES';
  currency: 'BRL' | 'USD' | 'EUR';
  notifications: {
    email: boolean;
    push: boolean;
    weeklySummary: boolean;
    budgetAlerts: boolean;
    aiInsights: boolean;
  };
  security: {
    twoFactorEnabled: boolean;
    sessionTimeoutMinutes: number;
  };
}

export interface NotificationItem {
  id: string;
  title: string;
  message: string;
  type: 'INFO' | 'WARNING' | 'SUCCESS' | 'ALERT';
  read: boolean;
  createdAt: string;
  link?: string;
}
