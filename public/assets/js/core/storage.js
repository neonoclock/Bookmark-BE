const AUTH_KEY = "AUTH";

export function saveAuth(data) {
  localStorage.setItem(AUTH_KEY, JSON.stringify(data));
}

export function loadAuth() {
  const raw = localStorage.getItem(AUTH_KEY);
  if (!raw) return null;
  return JSON.parse(raw);
}

export function clearAuth() {
  localStorage.removeItem(AUTH_KEY);
}

export function getToken() {
  const auth = loadAuth();
  return auth?.token || null;
}
