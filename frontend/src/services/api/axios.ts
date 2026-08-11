import axios, { AxiosError, InternalAxiosRequestConfig } from 'axios';

const BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';
export const TOKEN_KEY = 'smartfinance_auth_token';

export type ApiErrorType = 'UNAUTHORIZED' | 'FORBIDDEN' | 'NOT_FOUND' | 'VALIDATION' | 'SERVER' | 'NETWORK' | 'UNKNOWN';
export interface ApiError {
  type: ApiErrorType;
  status?: number;
  message: string;
  fieldErrors?: string[];
  retryable: boolean;
}

interface SpringErrorResponse {
  status?: number;
  message?: string;
  detalhes?: string[];
}

export function handleApiError(error: unknown): ApiError {
  if (typeof error === 'object' && error !== null && 'type' in error && 'message' in error && 'retryable' in error) {
    return error as ApiError;
  }
  if (axios.isAxiosError(error)) {
    const response = error.response;
    const body = response?.data as SpringErrorResponse | string | undefined;
    const details = typeof body === 'object' && body ? body.detalhes : undefined;
    const backendMessage = typeof body === 'object' && body ? body.message : undefined;
    const status = response?.status;
    if (!response) return { type: 'NETWORK', message: 'Não foi possível conectar ao servidor. Tente novamente.', retryable: true };
    if (status === 401) return { type: 'UNAUTHORIZED', status, message: 'Sua sessão expirou. Entre novamente para continuar.', retryable: false };
    if (status === 403) return { type: 'FORBIDDEN', status, message: 'Você não tem permissão para realizar esta ação.', retryable: false };
    if (status === 404) return { type: 'NOT_FOUND', status, message: backendMessage || 'O recurso solicitado não foi encontrado.', retryable: false };
    if (status === 400 || status === 422) return { type: 'VALIDATION', status, message: details?.[0] || backendMessage || 'Revise os dados informados.', fieldErrors: details, retryable: false };
    if (status && status >= 500) return { type: 'SERVER', status, message: 'O serviço está indisponível no momento. Tente novamente.', retryable: true };
  }
  return { type: 'UNKNOWN', message: 'Não foi possível concluir a operação. Tente novamente.', retryable: true };
}

export const api = axios.create({ baseURL: BASE_URL, headers: { 'Content-Type': 'application/json' }, timeout: 10000 });

const isProtectedRequest = (config: InternalAxiosRequestConfig) => !config.url?.startsWith('/auth/');

api.interceptors.request.use((config) => {
  if (!isProtectedRequest(config)) return config;
  const token = localStorage.getItem(TOKEN_KEY);
  if (!token) {
    return Promise.reject({ __apiError: { type: 'UNAUTHORIZED', message: 'Sua sessão expirou. Entre novamente para continuar.', retryable: false } satisfies ApiError });
  }
  config.headers.Authorization = `Bearer ${token}`;
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error: AxiosError | { __apiError?: ApiError }) => {
    const normalized = '__apiError' in error && error.__apiError ? error.__apiError : handleApiError(error);
    if (normalized.type === 'UNAUTHORIZED') {
      localStorage.removeItem(TOKEN_KEY);
      window.dispatchEvent(new CustomEvent('smartfinance:unauthorized'));
    }
    return Promise.reject(normalized);
  },
);
