export const TOKEN_KEY = 'smartfinance_auth_token';
export const TOKEN_EXPIRY_KEY = 'smartfinance_auth_expires_at';
export const UNAUTHORIZED_EVENT = 'smartfinance:unauthorized';

export function clearSession() {
  localStorage.removeItem(TOKEN_KEY);
  localStorage.removeItem(TOKEN_EXPIRY_KEY);
}

/** Checks only JWT shape; its signature is validated by the API. */
export function isTokenWellFormed(token: string | null): token is string {
  if (!token || token.split('.').length !== 3) return false;
  try {
    const payload = token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/');
    JSON.parse(atob(payload));
    return true;
  } catch {
    return false;
  }
}

export function hasValidStoredSession() {
  const token = localStorage.getItem(TOKEN_KEY);
  const expiresAt = Number(localStorage.getItem(TOKEN_EXPIRY_KEY));
  return isTokenWellFormed(token) && Number.isFinite(expiresAt) && expiresAt > Date.now();
}

export function invalidateSession() {
  clearSession();
  window.dispatchEvent(new CustomEvent(UNAUTHORIZED_EVENT));
}
