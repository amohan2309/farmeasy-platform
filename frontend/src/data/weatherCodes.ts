/** WMO Weather interpretation codes used by Open-Meteo. */
const WEATHER_CODES: Record<number, { en: string; hi: string }> = {
  0: { en: 'Clear sky', hi: 'साफ़ आसमान' },
  1: { en: 'Mainly clear', hi: 'अधिकतर साफ़' },
  2: { en: 'Partly cloudy', hi: 'आंशिक बादल' },
  3: { en: 'Overcast', hi: 'घने बादल' },
  45: { en: 'Fog', hi: 'कोहरा' },
  48: { en: 'Depositing rime fog', hi: 'पाला कोहरा' },
  51: { en: 'Light drizzle', hi: 'हल्की फुहार' },
  53: { en: 'Moderate drizzle', hi: 'मध्यम फुहार' },
  55: { en: 'Dense drizzle', hi: 'घनी फुहार' },
  56: { en: 'Light freezing drizzle', hi: 'हल्की जमा देने वाली फुहार' },
  57: { en: 'Dense freezing drizzle', hi: 'घनी जमा देने वाली फुहार' },
  61: { en: 'Slight rain', hi: 'हल्की बारिश' },
  63: { en: 'Moderate rain', hi: 'मध्यम बारिश' },
  65: { en: 'Heavy rain', hi: 'तेज़ बारिश' },
  66: { en: 'Light freezing rain', hi: 'हल्की ओला-बारिश' },
  67: { en: 'Heavy freezing rain', hi: 'तेज़ ओला-बारिश' },
  71: { en: 'Slight snow', hi: 'हल्की बर्फ़' },
  73: { en: 'Moderate snow', hi: 'मध्यम बर्फ़' },
  75: { en: 'Heavy snow', hi: 'तेज़ बर्फ़बारी' },
  77: { en: 'Snow grains', hi: 'बर्फ़ के कण' },
  80: { en: 'Slight rain showers', hi: 'हल्की बौछार' },
  81: { en: 'Moderate rain showers', hi: 'मध्यम बौछार' },
  82: { en: 'Violent rain showers', hi: 'तेज़ बौछार' },
  85: { en: 'Slight snow showers', hi: 'हल्की बर्फ़ीली बौछार' },
  86: { en: 'Heavy snow showers', hi: 'तेज़ बर्फ़ीली बौछार' },
  95: { en: 'Thunderstorm', hi: 'आंधी-तूफ़ान' },
  96: { en: 'Thunderstorm with slight hail', hi: 'हल्की ओलावृष्टि के साथ तूफ़ान' },
  99: { en: 'Thunderstorm with heavy hail', hi: 'तेज़ ओलावृष्टि के साथ तूफ़ान' },
};

export function weatherCodeLabel(code: number | null | undefined, locale: string): string {
  if (code == null || Number.isNaN(code)) return '—';
  const rounded = Math.round(code);
  const entry = WEATHER_CODES[rounded];
  if (!entry) return locale === 'hi' ? `मौसम कोड ${rounded}` : `Weather code ${rounded}`;
  return locale === 'hi' ? entry.hi : entry.en;
}

const COMPASS = ['N', 'NNE', 'NE', 'ENE', 'E', 'ESE', 'SE', 'SSE', 'S', 'SSW', 'SW', 'WSW', 'W', 'WNW', 'NW', 'NNW'];

export function compassFromDegrees(degrees: number | null | undefined): string | null {
  if (degrees == null || Number.isNaN(degrees)) return null;
  const index = Math.round(((degrees % 360) + 360) % 360 / 22.5) % 16;
  return COMPASS[index];
}
