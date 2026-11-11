import { useCallback, useEffect, useMemo, useState } from 'react';
import { getHourlyWeatherForecast, t, type HourlyForecast, type HourlyWeatherPoint } from '../api/client';
import { compassFromDegrees, weatherCodeLabel } from '../data/weatherCodes';
import { DEFAULT_WEATHER_LOCATION } from '../data/weatherLocation';

function formatMetric(value: number | null | undefined, unit?: string, digits = 1) {
  if (value == null || Number.isNaN(value)) return '—';
  const formatted = Number(value).toFixed(digits);
  return unit ? `${formatted} ${unit}` : formatted;
}

function formatWind(speed: number | null, speedUnit: string | undefined, degrees: number | null) {
  const speedText = formatMetric(speed, speedUnit);
  const compass = compassFromDegrees(degrees);
  if (speedText === '—') return '—';
  if (degrees == null) return speedText;
  return compass ? `${speedText} · ${Math.round(degrees)}° ${compass}` : `${speedText} · ${Math.round(degrees)}°`;
}

/** Format Open-Meteo local timestamps using the API timezone, without converting to the browser zone. */
function formatForecastTime(time: string, timeZone: string, locale: string) {
  const [datePart, timePart = '00:00'] = time.split('T');
  const [year, month, day] = datePart.split('-').map(Number);
  const [hour, minute] = timePart.split(':').map(Number);
  const asUtc = new Date(Date.UTC(year, month - 1, day, hour, minute || 0));
  return new Intl.DateTimeFormat(locale === 'hi' ? 'hi-IN' : 'en-IN', {
    weekday: 'short',
    day: 'numeric',
    month: 'short',
    hour: 'numeric',
    minute: '2-digit',
    hour12: true,
    timeZone: 'UTC',
  }).format(asUtc);
}

function currentHourKey(timeZone: string) {
  const zone = timeZone && timeZone !== 'auto' ? timeZone : 'Asia/Kolkata';
  try {
    const parts = new Intl.DateTimeFormat('en-CA', {
      timeZone: zone,
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      hourCycle: 'h23',
    }).formatToParts(new Date());
    const get = (type: string) => parts.find((p) => p.type === type)?.value ?? '';
    return `${get('year')}-${get('month')}-${get('day')}T${get('hour')}:00`;
  } catch {
    return '';
  }
}

function Metric({
  label,
  value,
}: {
  label: string;
  value: string;
}) {
  return (
    <div className="weather-metric">
      <span className="stat-label">{label}</span>
      <strong>{value}</strong>
    </div>
  );
}

export default function WeatherPage() {
  const [forecast, setForecast] = useState<HourlyForecast | null>(null);
  const [selectedTime, setSelectedTime] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const locale = localStorage.getItem('locale') || 'en';

  const load = useCallback(async () => {
    setLoading(true);
    setError('');
    const fallback = DEFAULT_WEATHER_LOCATION;
    const requestForecast = (location: typeof fallback) => getHourlyWeatherForecast(location);

    // Ask the browser for a fresh position each time the forecast is loaded.
    // Location access is optional; denied or unavailable access uses Patna.
    const location = await new Promise<typeof fallback>((resolve) => {
      if (!navigator.geolocation) {
        resolve(fallback);
        return;
      }
      navigator.geolocation.getCurrentPosition(
        ({ coords }) => resolve({
          name: t('Your location', 'आपका स्थान'),
          latitude: coords.latitude,
          longitude: coords.longitude,
          timezone: 'auto',
        }),
        () => resolve(fallback),
        { enableHighAccuracy: false, maximumAge: 5 * 60 * 1000, timeout: 8000 },
      );
    });

    try {
      let data;
      try {
        data = await requestForecast(location);
      } catch (err) {
        // A location-specific lookup can fail independently of geolocation.
        // Retry against the configured default before showing an error.
        if (location.latitude === fallback.latitude && location.longitude === fallback.longitude) throw err;
        data = await requestForecast(fallback);
      }
      setForecast(data);
      const key = currentHourKey(data.timezone);
      const match = data.hours.find((h) => h.time.startsWith(key)) ?? data.hours[0];
      setSelectedTime(match?.time ?? '');
    } catch {
      setForecast(null);
      setError(t('Could not load weather forecast', 'मौसम पूर्वानुमान लोड नहीं हो सका'));
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    load();
  }, [load]);

  const hour: HourlyWeatherPoint | undefined = useMemo(
    () => forecast?.hours.find((h) => h.time === selectedTime) ?? forecast?.hours[0],
    [forecast, selectedTime],
  );

  const units = forecast?.units ?? {};
  const upcoming = useMemo(() => {
    if (!forecast) return [];
    const start = forecast.hours.findIndex((h) => h.time === (hour?.time ?? selectedTime));
    const from = start >= 0 ? start : 0;
    return forecast.hours.slice(from, from + 12);
  }, [forecast, hour, selectedTime]);

  return (
    <div className="page">
      <div className="page-head">
        <h2>{t('Weather', 'मौसम')}</h2>
        <button type="button" className="btn ghost" onClick={load} disabled={loading}>
          {loading ? t('Refreshing…', 'ताज़ा हो रहा…') : t('Refresh', 'ताज़ा करें')}
        </button>
      </div>
      <p className="muted">
        {forecast?.locationName ?? DEFAULT_WEATHER_LOCATION.name}
        {forecast
          ? ` · ${forecast.latitude.toFixed(4)}, ${forecast.longitude.toFixed(4)} · ${forecast.timezone}${
              forecast.timezoneAbbreviation ? ` (${forecast.timezoneAbbreviation})` : ''
            }`
          : ''}
      </p>

      {loading && !forecast && <p className="muted">{t('Loading forecast…', 'पूर्वानुमान लोड हो रहा है…')}</p>}
      {error && <p className="error">{error}</p>}
      {!loading && !error && forecast && forecast.hours.length === 0 && (
        <p className="muted">{t('No hourly forecast data', 'कोई घंटेवार पूर्वानुमान नहीं')}</p>
      )}

      {forecast && hour && (
        <>
          <label className="hour-picker">
            {t('Hour', 'घंटा')}
            <select value={hour.time} onChange={(e) => setSelectedTime(e.target.value)}>
              {forecast.hours.map((h) => (
                <option key={h.time} value={h.time}>
                  {formatForecastTime(h.time, forecast.timezone, locale)}
                </option>
              ))}
            </select>
          </label>

          <div className="stat-grid">
            <Metric
              label={t('Conditions', 'स्थिति')}
              value={weatherCodeLabel(hour.weatherCode, locale)}
            />
            <Metric
              label={t('Temperature', 'तापमान')}
              value={formatMetric(hour.temperature2m, units.temperature2m)}
            />
            <Metric
              label={t('Feels like', 'अनुभूत')}
              value={formatMetric(hour.apparentTemperature, units.apparentTemperature)}
            />
            <Metric
              label={t('Humidity', 'नमी')}
              value={formatMetric(hour.relativeHumidity2m, units.relativeHumidity2m, 0)}
            />
          </div>

          <section className="card">
            <h3 className="section-title">{t('Weather', 'मौसम')}</h3>
            <div className="stat-grid weather-metrics">
              <Metric label={t('Dew point', 'ओसांक')} value={formatMetric(hour.dewPoint2m, units.dewPoint2m)} />
              <Metric label={t('Visibility', 'दृश्यता')} value={formatMetric(hour.visibility, units.visibility, 0)} />
              <Metric label={t('Temp 80 m', 'ताप 80 मी')} value={formatMetric(hour.temperature80m, units.temperature80m)} />
              <Metric label={t('Temp 120 m', 'ताप 120 मी')} value={formatMetric(hour.temperature120m, units.temperature120m)} />
              <Metric label={t('Temp 180 m', 'ताप 180 मी')} value={formatMetric(hour.temperature180m, units.temperature180m)} />
            </div>
          </section>

          <section className="card">
            <h3 className="section-title">{t('Precipitation', 'वर्षा')}</h3>
            <div className="stat-grid weather-metrics">
              <Metric label={t('Precipitation', 'वर्षा')} value={formatMetric(hour.precipitation, units.precipitation)} />
              <Metric label={t('Rain', 'बारिश')} value={formatMetric(hour.rain, units.rain)} />
              <Metric label={t('Showers', 'बौछार')} value={formatMetric(hour.showers, units.showers)} />
            </div>
          </section>

          <section className="card">
            <h3 className="section-title">{t('Soil', 'मिट्टी')}</h3>
            <div className="stat-grid weather-metrics">
              <Metric label={t('Temp 0 cm', 'ताप 0 सेमी')} value={formatMetric(hour.soilTemperature0cm, units.soilTemperature0cm)} />
              <Metric label={t('Temp 6 cm', 'ताप 6 सेमी')} value={formatMetric(hour.soilTemperature6cm, units.soilTemperature6cm)} />
              <Metric label={t('Temp 18 cm', 'ताप 18 सेमी')} value={formatMetric(hour.soilTemperature18cm, units.soilTemperature18cm)} />
              <Metric label={t('Temp 54 cm', 'ताप 54 सेमी')} value={formatMetric(hour.soilTemperature54cm, units.soilTemperature54cm)} />
              <Metric label={t('Moisture 0–1 cm', 'नमी 0–1 सेमी')} value={formatMetric(hour.soilMoisture0To1cm, units.soilMoisture0To1cm, 3)} />
              <Metric label={t('Moisture 1–3 cm', 'नमी 1–3 सेमी')} value={formatMetric(hour.soilMoisture1To3cm, units.soilMoisture1To3cm, 3)} />
              <Metric label={t('Moisture 3–9 cm', 'नमी 3–9 सेमी')} value={formatMetric(hour.soilMoisture3To9cm, units.soilMoisture3To9cm, 3)} />
              <Metric label={t('Moisture 9–27 cm', 'नमी 9–27 सेमी')} value={formatMetric(hour.soilMoisture9To27cm, units.soilMoisture9To27cm, 3)} />
              <Metric label={t('Moisture 27–81 cm', 'नमी 27–81 सेमी')} value={formatMetric(hour.soilMoisture27To81cm, units.soilMoisture27To81cm, 3)} />
            </div>
          </section>

          <section className="card">
            <h3 className="section-title">{t('Wind', 'हवा')}</h3>
            <div className="stat-grid weather-metrics">
              <Metric
                label={t('Wind 10 m', 'हवा 10 मी')}
                value={formatWind(hour.windSpeed10m, units.windSpeed10m, hour.windDirection10m)}
              />
              <Metric
                label={t('Wind 80 m', 'हवा 80 मी')}
                value={formatWind(hour.windSpeed80m, units.windSpeed80m, hour.windDirection80m)}
              />
              <Metric
                label={t('Wind 120 m', 'हवा 120 मी')}
                value={formatWind(hour.windSpeed120m, units.windSpeed120m, hour.windDirection120m)}
              />
              <Metric
                label={t('Wind 180 m', 'हवा 180 मी')}
                value={formatWind(hour.windSpeed180m, units.windSpeed180m, hour.windDirection180m)}
              />
              <Metric label={t('Gusts 10 m', 'झोंके 10 मी')} value={formatMetric(hour.windGusts10m, units.windGusts10m)} />
            </div>
          </section>

          <section className="card">
            <h3 className="section-title">{t('Evapotranspiration', 'वाष्पोत्सर्जन')}</h3>
            <div className="stat-grid weather-metrics">
              <Metric
                label={t('ET₀ (FAO)', 'ET₀ (FAO)')}
                value={formatMetric(hour.et0FaoEvapotranspiration, units.et0FaoEvapotranspiration, 2)}
              />
              <Metric
                label={t('Evapotranspiration', 'वाष्पोत्सर्जन')}
                value={formatMetric(hour.evapotranspiration, units.evapotranspiration, 2)}
              />
            </div>
          </section>

          <section className="card">
            <h3 className="section-title">{t('Next hours', 'अगले घंटे')}</h3>
            {upcoming.length === 0 ? (
              <p className="muted">{t('No hourly forecast data', 'कोई घंटेवार पूर्वानुमान नहीं')}</p>
            ) : (
              <div className="forecast-table-wrap">
                <table className="forecast-table">
                  <thead>
                    <tr>
                      <th>{t('Time', 'समय')}</th>
                      <th>{t('Sky', 'आकाश')}</th>
                      <th>{t('Temp', 'ताप')}</th>
                      <th>{t('Rain', 'बारिश')}</th>
                      <th>{t('Wind', 'हवा')}</th>
                    </tr>
                  </thead>
                  <tbody>
                    {upcoming.map((h) => (
                      <tr key={h.time}>
                        <td>{formatForecastTime(h.time, forecast.timezone, locale)}</td>
                        <td>{weatherCodeLabel(h.weatherCode, locale)}</td>
                        <td>{formatMetric(h.temperature2m, units.temperature2m)}</td>
                        <td>{formatMetric(h.precipitation, units.precipitation)}</td>
                        <td>{formatWind(h.windSpeed10m, units.windSpeed10m, h.windDirection10m)}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </section>
        </>
      )}
    </div>
  );
}
