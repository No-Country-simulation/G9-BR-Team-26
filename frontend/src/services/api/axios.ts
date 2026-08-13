import axios, { AxiosError, InternalAxiosRequestConfig } from 'axios';
import { hasValidStoredSession, invalidateSession, TOKEN_KEY } from './session';

const BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';
export { TOKEN_KEY } from './session';

export type ApiErrorType = 'UNAUTHORIZED' | 'FORBIDDEN' | 'NOT_FOUND' | 'VALIDATION' | 'SERVER' | 'NETWORK' | 'UNKNOWN';
export interface ApiError { type: ApiErrorType; status?: number; message: string; fieldErrors?: string[]; retryable: boolean; }
interface SpringErrorResponse { status?: number; message?: string; detalhes?: string[]; }
const GENERIC_ERROR = 'Ops, algo não funcionou. Tente novamente mais tarde.';

export function handleApiError(error: unknown): ApiError {
  if (typeof error === 'object' && error !== null && 'type' in error && 'message' in error && 'retryable' in error) return error as ApiError;
  if (axios.isAxiosError(error)) {
    const response = error.response;
    const body = response?.data as SpringErrorResponse | undefined;
    const status = response?.status;
    if (!response) return { type: 'NETWORK', message: GENERIC_ERROR, retryable: true };
    if (status === 401) return { type: 'UNAUTHORIZED', status, message: 'Sua sessão não é mais válida.', retryable: false };
    if (status === 403) return { type: 'FORBIDDEN', status, message: 'Sua sessão não é mais válida.', retryable: false };
    if (status === 400 || status === 422) return { type: 'VALIDATION', status, message: body?.detalhes?.[0] || body?.message || 'Revise os dados informados.', fieldErrors: body?.detalhes, retryable: false };
    if (status === 404) return { type: 'NOT_FOUND', status, message: GENERIC_ERROR, retryable: true };
    if (status && status >= 500) return { type: 'SERVER', status, message: GENERIC_ERROR, retryable: true };
  }
  return { type: 'UNKNOWN', message: GENERIC_ERROR, retryable: true };
}

export const api = axios.create({ baseURL: BASE_URL, headers: { 'Content-Type': 'application/json' }, timeout: 10000 });
const isProtectedRequest = (config: InternalAxiosRequestConfig) => !config.url?.startsWith('/auth/');

api.interceptors.request.use((config) => {
  if (!isProtectedRequest(config)) return config;
  if (!hasValidStoredSession()) {
    invalidateSession();
    return Promise.reject({ __apiError: { type: 'UNAUTHORIZED', message: 'Sua sessão não é mais válida.', retryable: false } satisfies ApiError });
  }
  config.headers.Authorization = `Bearer ${localStorage.getItem(TOKEN_KEY)!}`;
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error: AxiosError | { __apiError?: ApiError }) => {
    const normalized = '__apiError' in error && error.__apiError ? error.__apiError : handleApiError(error);
    const requestWasProtected = !axios.isAxiosError(error) || !error.config?.url?.startsWith('/auth/');
    if (requestWasProtected && (normalized.type === 'UNAUTHORIZED' || normalized.type === 'FORBIDDEN')) invalidateSession();
    return Promise.reject(normalized);
  },
);
