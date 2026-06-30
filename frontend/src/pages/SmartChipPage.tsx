import { useEffect, useState } from 'react';
import { getIotDevices, getIotReadings, getIotStatus, t, type IotDeviceStatus, type IotReading } from '../api/client';

const DEMO_CHIP = 'FE-CHIP-001';

export default function SmartChipPage() {
  const [status, setStatus] = useState<IotDeviceStatus | null>(null);
  const [readings, setReadings] = useState<IotReading[]>([]);

  useEffect(() => {
    getIotDevices()
      .then(async (devices) => {
        const chip = devices[0]?.chipId ?? DEMO_CHIP;
        const [s, r] = await Promise.all([getIotStatus(chip), getIotReadings(chip)]);
        setStatus(s);
        setReadings(r);
      })
      .catch(() => {});
  }, []);

  const latest = (status?.latestReading as Record<string, unknown>) || readings[0] || {};

  return (
    <div className="page">
      <h2>{t('Farm Easy Smart Chip', 'फार्म ईज़ी स्मार्ट चिप')}</h2>
      <p className="muted">{t('IoT automation — irrigation, soil, nutrients', 'आईओटी — सिंचाई, मिट्टी, पोषक')}</p>

      <div className="card chip-status">
        <p>
          <strong>{String(status?.farmName ?? t('Demo field', 'डेमो खेत'))}</strong>
        </p>
        <p>
          {t('Chip', 'चिप')}: <code>{String(status?.chipId ?? DEMO_CHIP)}</code> ·{' '}
          <span className="badge online">{String(status?.status ?? 'ONLINE')}</span>
        </p>
        {status?.solarPowered && <p className="muted">☀ {t('Solar powered', 'सोलर से चल रहा')}</p>}
      </div>

      <div className="stat-grid">
        <div className="card stat">
          <span className="stat-label">{t('Soil moisture', 'मिट्टी की नमी')}</span>
          <strong>{Number(latest.soilMoisturePercent ?? 0).toFixed(1)}%</strong>
        </div>
        <div className="card stat">
          <span className="stat-label">pH</span>
          <strong>{Number(latest.soilPh ?? 0).toFixed(1)}</strong>
        </div>
        <div className="card stat">
          <span className="stat-label">NPK (N-P-K)</span>
          <strong>
            {Number(latest.npkN ?? 0).toFixed(0)}-{Number(latest.npkP ?? 0).toFixed(0)}-
            {Number(latest.npkK ?? 0).toFixed(0)}
          </strong>
        </div>
        <div className="card stat">
          <span className="stat-label">{t('Temperature', 'तापमान')}</span>
          <strong>{Number(latest.temperatureCelsius ?? 0).toFixed(1)}°C</strong>
        </div>
      </div>

      <section className="card">
        <h3>{t('Automation', 'स्वचालन')}</h3>
        <ul>
          <li>{t('Smart irrigation ON/OFF by moisture & weather', 'नमी और मौसम से सिंचाई ON/OFF')}</li>
          <li>{t('Fertilizer dispensing via drip', 'ड्रिप से खाद छिड़काव')}</li>
          <li>{t('Wireless sync to Portal', 'पोर्टल से वायरलेस सिंक')}</li>
        </ul>
      </section>
    </div>
  );
}
