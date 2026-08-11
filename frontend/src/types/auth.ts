export interface User {
  id: string;
  name: string;
  email: string;
  avatarUrl?: string;
  role: 'ADMIN' | 'USER' | 'FINANCE_MANAGER';
  companyName?: string;
}

export interface LoginCredentials {
  email: string;
  password?: string;
  rememberMe?: boolean;
}

export interface AuthResponse {
  user: User;
  token: string;
  expiresIn: number;
}

export interface Session {
  user: User | null;
  token: string | null;
  isAuthenticated: boolean;
}
