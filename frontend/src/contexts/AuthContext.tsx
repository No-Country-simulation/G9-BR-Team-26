import React, { createContext, useContext, useEffect, useState } from 'react';
import { User, LoginCredentials } from '../types/auth';
import { authRepository } from '../services/repositories/auth.repository';
import { clearSession, hasValidStoredSession, invalidateSession, TOKEN_EXPIRY_KEY, TOKEN_KEY, UNAUTHORIZED_EVENT } from '../services/api/session';

interface AuthContextType { user: User | null; token: string | null; isAuthenticated: boolean; isLoading: boolean; login: (credentials: LoginCredentials) => Promise<void>; logout: () => Promise<void>; }
const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [token, setToken] = useState<string | null>(() => hasValidStoredSession() ? localStorage.getItem(TOKEN_KEY) : null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const initAuth = async () => {
      if (!token || !hasValidStoredSession()) {
        clearSession();
        setToken(null); setUser(null); setIsLoading(false); return;
      }
      try { setUser(await authRepository.getCurrentUser()); }
      catch { clearSession(); setToken(null); setUser(null); }
      finally { setIsLoading(false); }
    };
    void initAuth();
  }, [token]);

  useEffect(() => {
    if (!token) return;
    const delay = Number(localStorage.getItem(TOKEN_EXPIRY_KEY)) - Date.now();
    if (delay <= 0) { invalidateSession(); return; }
    const timer = window.setTimeout(invalidateSession, delay);
    return () => window.clearTimeout(timer);
  }, [token]);

  useEffect(() => {
    const onUnauthorized = () => { clearSession(); setToken(null); setUser(null); setIsLoading(false); };
    window.addEventListener(UNAUTHORIZED_EVENT, onUnauthorized);
    return () => window.removeEventListener(UNAUTHORIZED_EVENT, onUnauthorized);
  }, []);

  const login = async (credentials: LoginCredentials) => {
    setIsLoading(true);
    try {
      const response = await authRepository.login(credentials);
      localStorage.setItem(TOKEN_KEY, response.token);
      localStorage.setItem(TOKEN_EXPIRY_KEY, String(Date.now() + response.expiresIn * 1000));
      setToken(response.token); setUser(response.user);
    } finally { setIsLoading(false); }
  };
  const logout = async () => {
    setIsLoading(true);
    try { await authRepository.logout(); }
    finally { clearSession(); setToken(null); setUser(null); setIsLoading(false); }
  };

  return <AuthContext.Provider value={{ user, token, isAuthenticated: !!user && hasValidStoredSession(), isLoading, login, logout }}>{children}</AuthContext.Provider>;
};
export const useAuth = () => { const context = useContext(AuthContext); if (!context) throw new Error('useAuth deve ser usado dentro de um AuthProvider'); return context; };
