// SECURITY (XSS risk): the JWT and its expiry are stored in localStorage below,
// which is readable by any JavaScript running on this origin. A successful XSS
// attack (e.g. via a compromised dependency or unsanitized user content) would
// have unrestricted read access to the token. The recommended fix is to migrate
// authentication to an httpOnly + Secure + SameSite=Strict cookie set by the
// backend, so the token is never exposed to client-side JS at all — but that
// requires changing the auth contract between frontend and backend (login/refresh
// responses, CSRF handling, etc.), which is out of scope for this change. Until
// that migration happens, keep CSP as strict as possible to reduce XSS exposure.
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
