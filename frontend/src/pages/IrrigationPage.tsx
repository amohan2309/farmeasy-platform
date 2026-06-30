import { useEffect, useState } from 'react';
import { controlPump, getIrrigationSchedules, getWaterUsage, getWeatherAlerts, t, type IrrigationSchedule, type WaterUsage, type WeatherAlert } from '../api/client';

export default function IrrigationPage() {
  const [schedules, setSchedules] = useState<IrrigationSchedule[]>([]);
  const [usage, setUsage] = useState<WaterUsage | null>(null);
  const [alerts, setAlerts] = useState<WeatherAlert[]>([]);
  const [pumpMsg, setPumpMsg] = useState('');

  useEffect(() => {
    Promise.all([getIrrigationSchedules(), getWaterUsage(), getWeatherAlerts()])
      .then(([s, u, a]) => {
        setSchedules(s);
        setUsage(u);
        setAlerts(a);
      })
      .catch(() => {});
  }, []);

  async function pump(action: 'on' | 'off') {
    const res = await controlPump(action);
    setPumpMsg(String((res as { message?: string }).message ?? action));
  }

  return (
    <div className="page">
      <h2>{t('Irrigation', 'सिंचाई')}</h2>

      {alerts[0]?.alertMessage && (
        <p className="alert-banner">⚠ {String(alerts[0].alertMessage)}</p>
      )}

      <div className="stat-grid">
        <div className="card stat">
          <span className="stat-label">{t('Today (liters)', 'आज (लीटर)')}</span>
          <strong>{String(usage?.todayLiters ?? '—')}</strong>
        </div>
        <div className="card stat">
          <span className="stat-label">{t('Saved', 'बचत')}</span>
          <strong>{String(usage?.savedPercent ?? 0)}%</strong>
        </div>
      </div>

      <div className="pump-controls">
        <button type="button" className="btn" onClick={() => pump('on')}>
          {t('Pump ON', 'पंप चालू')}
        </button>
        <button type="button" className="btn ghost" onClick={() => pump('off')}>
          {t('Pump OFF', 'पंप बंद')}
        </button>
        {pumpMsg && <p className="muted">{pumpMsg}</p>}
      </div>

      <section className="card">
        <h3>{t('Schedules', 'समय सारिणी')}</h3>
        {schedules.length === 0 ? (
          <p className="muted">{t('No schedules yet', 'अभी कोई समय सारिणी नहीं')}</p>
        ) : (
          <ul>
            {schedules.map((s) => (
              <li key={String(s.id)}>
                {String(s.cropType)} — {String(s.startTime)} ({String(s.durationMinutes)} min)
                {s.autoMode ? ` · ${t('Auto', 'ऑटो')}` : ''}
              </li>
            ))}
          </ul>
        )}
      </section>
    </div>
  );
}
