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

async function apiGet<T>(path: string): Promise<T> {
  const res = await fetch(`${API_BASE}${path}`, { headers: authHeaders() });
  if (!res.ok) throw new Error(`Request failed: ${path}`);
  return res.json();
}

async function apiPost<T>(path: string, body: unknown): Promise<T> {
  const res = await fetch(`${API_BASE}${path}`, {
    method: 'POST',
    headers: authHeaders(),
    body: JSON.stringify(body),
  });
  if (!res.ok) throw new Error(`Request failed: ${path}`);
  return res.json();
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
  return apiGet('/api/entitlements/me');
}

export async function getDashboardHome() {
  return apiGet<Record<string, unknown>>('/api/dashboard/home');
}

export async function listMarketplaceProducts(category?: string) {
  const q = category ? `?category=${category}` : '';
  return apiGet(`/api/marketplace/products${q}`);
}

export async function bookEquipment(productId: string, bookingDate: string) {
  const userId = localStorage.getItem('userId');
  return apiPost('/api/marketplace/equipment/bookings', {
    userId,
    productId,
    bookingDate,
  });
}

export async function getWeatherForecast(region = 'Punjab') {
  return apiGet(`/api/weather/forecast?region=${encodeURIComponent(region)}`);
}

export async function getWeatherAlerts(region = 'Punjab') {
  return apiGet(`/api/weather/alerts?region=${encodeURIComponent(region)}`);
}

export async function getIrrigationSchedules() {
  return apiGet('/api/irrigation/schedules');
}

export async function controlPump(action: 'on' | 'off') {
  return apiPost(`/api/irrigation/pump/${action}`, {});
}

export async function getWaterUsage() {
  return apiGet('/api/irrigation/water-usage');
}

export async function getIotDevices() {
  return apiGet('/api/iot/devices');
}

export async function getIotStatus(chipId: string) {
  return apiGet(`/api/iot/devices/${chipId}/status`);
}

export async function getIotReadings(chipId: string) {
  return apiGet(`/api/iot/devices/${chipId}/readings`);
}

export async function getCropListings() {
  return apiGet('/api/crop-selling/listings');
}

export async function createCropListing(listing: Record<string, unknown>) {
  return apiPost('/api/crop-selling/listings', listing);
}

export async function getMandiPrices() {
  return apiGet('/api/prices/mandi');
}

export async function listApps() {
  return apiGet('/api/apps?locale=hi');
}

export async function listChapters(appId: string) {
  return apiGet(`/api/content/apps/${appId}/chapters`);
}

export async function listTopics(chapterId: string) {
  return apiGet(`/api/content/chapters/${chapterId}/topics`);
}

export async function listSubtopics(topicId: string) {
  return apiGet(`/api/content/topics/${topicId}/subtopics`);
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

export function t(en: string, hi: string) {
  return (localStorage.getItem('locale') || 'en') === 'hi' ? hi : en;
}
