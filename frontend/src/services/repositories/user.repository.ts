import { api } from '../api/axios';
import { UserProfile, UserSettings, NotificationItem } from '../../types/user';

interface BackendUserResponse {
  id: number | string;
  nome?: string;
  name?: string;
  email: string;
  criado_em?: string;
}

const SETTINGS_KEY = 'smartfinance_user_settings';

const defaultSettings: UserSettings = {
  theme: 'dark',
  language: 'pt-BR',
  currency: 'BRL',
  notifications: {
    email: true,
    push: true,
    weeklySummary: true,
    budgetAlerts: true,
    aiInsights: true,
  },
  security: {
    twoFactorEnabled: false,
    sessionTimeoutMinutes: 30,
  },
};

export const userRepository = {
  getProfile: async (): Promise<UserProfile> => {
    const response = await api.get<BackendUserResponse>('/usuarios/me');
    const u = response.data;

    return {
      id: String(u.id),
      name: u.nome || u.name || 'Usuário',
      email: u.email,
      phone: '',
      cpfCnpj: '',
      avatarUrl: '',
      occupation: '',
      companyName: '',
    };
  },

  updateProfile: async (data: Partial<UserProfile>): Promise<UserProfile> => {
    const current = await userRepository.getProfile();
    return {
      ...current,
      ...data,
    };
  },

  getSettings: async (): Promise<UserSettings> => {
    const saved = localStorage.getItem(SETTINGS_KEY);
    if (saved) {
      try {
        return JSON.parse(saved);
      } catch {
        // Fallback para defaults
      }
    }
    return defaultSettings;
  },

  updateSettings: async (data: Partial<UserSettings>): Promise<UserSettings> => {
    const current = await userRepository.getSettings();
    const updated = { ...current, ...data };
    localStorage.setItem(SETTINGS_KEY, JSON.stringify(updated));
    return updated;
  },

  getNotifications: async (): Promise<NotificationItem[]> => {
    return [];
  },

  markNotificationAsRead: async (_id: string): Promise<NotificationItem[]> => {
    return [];
  },

  clearAllNotifications: async (): Promise<NotificationItem[]> => {
    return [];
  }
};
