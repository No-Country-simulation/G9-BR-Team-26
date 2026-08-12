import { api } from '../api/axios';
import { TOKEN_EXPIRY_KEY, TOKEN_KEY } from '../api/session';
import { LoginCredentials, AuthResponse, User } from '../../types/auth';

interface BackendAuthResponse {
  accessToken?: string;
  token?: string;
  tokenType?: string;
  expiresIn?: number;
}

interface BackendUserResponse {
  id: number | string;
  nome?: string;
  name?: string;
  email: string;
  criado_em?: string;
}

export const authRepository = {
  signup: async (data: { nome: string; email: string; senha: string }): Promise<User> => {
    const response = await api.post<BackendUserResponse>('/auth/signup', data);
    const u = response.data;
    return {
      id: String(u.id),
      name: u.nome || u.name || 'Usuário',
      email: u.email,
      role: 'ADMIN',
    };
  },

  login: async (credentials: LoginCredentials): Promise<AuthResponse> => {
    const response = await api.post<BackendAuthResponse>('/auth/login', {
      email: credentials.email,
      senha: credentials.password,
    });

    const data = response.data;
    const token = data.accessToken || '';
    if (!token) throw new Error('A resposta de login não incluiu um token de acesso.');
    localStorage.setItem(TOKEN_KEY, token);
    localStorage.setItem(TOKEN_EXPIRY_KEY, String(Date.now() + (data.expiresIn || 3600) * 1000));
    // Buscar perfil do usuário autenticado após o login
    const user = await authRepository.getCurrentUserWithToken();

    return {
      token,
      expiresIn: data.expiresIn || 3600,
      user,
    };
  },

  getCurrentUser: async (): Promise<User> => {
    const response = await api.get<BackendUserResponse>('/usuarios/me');
    const u = response.data;

    return {
      id: String(u.id),
      name: u.nome || u.name || 'Usuário',
      email: u.email,
      role: 'ADMIN',
    };
  },

  getCurrentUserWithToken: async (): Promise<User> => {
    // O token já está no storage e será anexado pelo interceptor central.
    const response = await api.get<BackendUserResponse>('/usuarios/me');

    const u = response.data;

    return {
      id: String(u.id),
      name: u.nome || u.name || 'Usuário',
      email: u.email,
      role: 'ADMIN',
    };
  },

  logout: async (): Promise<{ success: boolean }> => {
    return { success: true };
  }
};
