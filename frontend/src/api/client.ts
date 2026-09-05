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

export type FarmApp = {
  id: string;
  code: string;
  titleEn: string;
  titleHi: string;
  descriptionEn: string;
};

export type ContentChapter = {
  id: string;
  titleEn: string;
  titleHi: string;
};

export type ContentTopic = {
  id: string;
  titleEn: string;
  titleHi: string;
};

export type ContentSubtopic = {
  id: string;
  titleEn: string;
  titleHi: string;
  contentType: string;
};

export type MarketplaceProduct = {
  id: string;
  category: string;
  nameEn: string;
  nameHi?: string;
  priceInr: number;
  unit: string;
  supplier?: string;
};

export type WeatherAlert = {
  alertMessage?: string;
};

export type IrrigationSchedule = {
  id: string;
  cropType?: string;
  startTime?: string;
  durationMinutes?: number;
  autoMode?: boolean;
};

export type WaterUsage = {
  todayLiters?: number;
  savedPercent?: number;
};

export type IotDevice = {
  chipId: string;
};

export type IotDeviceStatus = {
  farmName?: string;
  chipId?: string;
  status?: string;
  solarPowered?: boolean;
  latestReading?: Record<string, unknown>;
};

export type IotReading = Record<string, unknown>;

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
  let res: Response;
  try {
    res = await fetch(`${API_BASE}/api/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ usernameOrEmail, password }),
    });
  } catch {
    throw new Error('NETWORK');
  }
  if (!res.ok) {
    const msg = await res.text().catch(() => '');
    if (res.status === 500 && msg.includes('Invalid credentials')) throw new Error('INVALID');
    if (res.status === 500 && msg.includes('User not found')) throw new Error('INVALID');
    throw new Error(`HTTP_${res.status}`);
  }
  return (await res.json()) as LoginResponse;
}

export async function sendPhoneOtp(phoneNumber: string) {
  let res: Response;
  try {
    res = await fetch(`${API_BASE}/api/auth/phone/send-otp`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ phoneNumber }),
    });
  } catch {
    throw new Error('NETWORK');
  }
  if (!res.ok) throw new Error('HTTP');
}

export async function verifyPhoneOtp(phoneNumber: string, otpCode: string) {
  let res: Response;
  try {
    res = await fetch(`${API_BASE}/api/auth/phone/verify`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ phoneNumber, otpCode }),
    });
  } catch {
    throw new Error('NETWORK');
  }
  if (!res.ok) throw new Error('INVALID');
  return (await res.json()) as LoginResponse;
}

export async function getEntitlements(): Promise<Entitlements> {
  return apiGet('/api/entitlements/me');
}

export async function getDashboardHome() {
  return apiGet<Record<string, unknown>>('/api/dashboard/home');
}

export async function listMarketplaceProducts(category?: string): Promise<MarketplaceProduct[]> {
  const q = category ? `?category=${category}` : '';
  return apiGet<MarketplaceProduct[]>(`/api/marketplace/products${q}`);
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

export async function getWeatherAlerts(region = 'Punjab'): Promise<WeatherAlert[]> {
  return apiGet<WeatherAlert[]>(`/api/weather/alerts?region=${encodeURIComponent(region)}`);
}

export async function getIrrigationSchedules(): Promise<IrrigationSchedule[]> {
  return apiGet<IrrigationSchedule[]>('/api/irrigation/schedules');
}

export async function controlPump(action: 'on' | 'off') {
  return apiPost(`/api/irrigation/pump/${action}`, {});
}

export async function getWaterUsage(): Promise<WaterUsage> {
  return apiGet<WaterUsage>('/api/irrigation/water-usage');
}

export async function getIotDevices(): Promise<IotDevice[]> {
  return apiGet<IotDevice[]>('/api/iot/devices');
}

export async function getIotStatus(chipId: string): Promise<IotDeviceStatus> {
  return apiGet<IotDeviceStatus>(`/api/iot/devices/${chipId}/status`);
}

export async function getIotReadings(chipId: string): Promise<IotReading[]> {
  return apiGet<IotReading[]>(`/api/iot/devices/${chipId}/readings`);
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

export async function listApps(): Promise<FarmApp[]> {
  return apiGet<FarmApp[]>('/api/apps?locale=hi');
}

export async function listChapters(appId: string): Promise<ContentChapter[]> {
  return apiGet<ContentChapter[]>(`/api/content/apps/${appId}/chapters`);
}

export async function listTopics(chapterId: string): Promise<ContentTopic[]> {
  return apiGet<ContentTopic[]>(`/api/content/chapters/${chapterId}/topics`);
}

export async function listSubtopics(topicId: string): Promise<ContentSubtopic[]> {
  return apiGet<ContentSubtopic[]>(`/api/content/topics/${topicId}/subtopics`);
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
