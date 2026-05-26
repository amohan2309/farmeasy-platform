const API_BASE = import.meta.env.VITE_API_URL || '';

export type LoginResponse = {
  userId: string;
  accessToken: string;
  refreshToken: string;
};

export type Entitlements = {
  planCode: string;
  features: string[];
  limits: Record<string, number>;
};

function authHeaders(): HeadersInit {
  const token = localStorage.getItem('accessToken');
  const userId = localStorage.getItem('userId');
  const headers: Record<string, string> = { 'Content-Type': 'application/json' };
  if (token) headers['Authorization'] = `Bearer ${token}`;
  if (userId) headers['X-User-Id'] = userId;
  return headers;
}

export async function loginWithPassword(usernameOrEmail: string, password: string) {
  const res = await fetch(`${API_BASE}/api/auth/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ usernameOrEmail, password }),
  });
  if (!res.ok) throw new Error('Login failed');
  return (await res.json()) as LoginResponse;
}

export async function sendPhoneOtp(phoneNumber: string) {
  await fetch(`${API_BASE}/api/auth/phone/send-otp`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ phoneNumber }),
  });
}

export async function verifyPhoneOtp(phoneNumber: string, otpCode: string) {
  const res = await fetch(`${API_BASE}/api/auth/phone/verify`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ phoneNumber, otpCode }),
  });
  if (!res.ok) throw new Error('OTP verification failed');
  return (await res.json()) as LoginResponse;
}

export async function getEntitlements(): Promise<Entitlements> {
  const res = await fetch(`${API_BASE}/api/entitlements/me`, { headers: authHeaders() });
  if (!res.ok) throw new Error('Failed to load entitlements');
  return res.json();
}

export async function listApps() {
  const res = await fetch(`${API_BASE}/api/apps?locale=hi`, { headers: authHeaders() });
  if (!res.ok) throw new Error('Failed to load apps');
  return res.json();
}

export async function listChapters(appId: string) {
  const res = await fetch(`${API_BASE}/api/content/apps/${appId}/chapters`, { headers: authHeaders() });
  if (!res.ok) throw new Error('Failed to load chapters');
  return res.json();
}

export async function listTopics(chapterId: string) {
  const res = await fetch(`${API_BASE}/api/content/chapters/${chapterId}/topics`, { headers: authHeaders() });
  return res.json();
}

export async function listSubtopics(topicId: string) {
  const res = await fetch(`${API_BASE}/api/content/topics/${topicId}/subtopics`, { headers: authHeaders() });
  return res.json();
}

export async function detectLocale(lat: number, lng: number) {
  const res = await fetch(`${API_BASE}/api/i18n/locale/detect?lat=${lat}&lng=${lng}`);
  return res.json();
}

export async function analyzeImage(file: File, locale: string, detailed: boolean) {
  const form = new FormData();
  form.append('image', file);
  form.append('locale', locale);
  form.append('detailedReport', String(detailed));
  const res = await fetch(`${API_BASE}/api/chatbot/analyze?locale=${locale}&detailedReport=${detailed}`, {
    method: 'POST',
    headers: { 'X-User-Id': localStorage.getItem('userId') || '' },
    body: form,
  });
  if (!res.ok) {
    const msg = res.status === 402 ? 'Daily limit reached. Upgrade to Pro.' : 'Analysis failed';
    throw new Error(msg);
  }
  return res.json();
}

export function saveSession(data: LoginResponse) {
  localStorage.setItem('accessToken', data.accessToken);
  localStorage.setItem('refreshToken', data.refreshToken);
  localStorage.setItem('userId', data.userId);
}

export function clearSession() {
  localStorage.removeItem('accessToken');
  localStorage.removeItem('refreshToken');
  localStorage.removeItem('userId');
}
