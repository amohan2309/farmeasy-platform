import { useEffect, useState } from 'react';
import { getDashboardHome, t } from '../api/client';

export default function HomePage() {
  const [data, setData] = useState<Record<string, unknown> | null>(null);
  const [error, setError] = useState('');

  useEffect(() => {
    getDashboardHome()
      .then(setData)
      .catch(() => setError(t('Could not load dashboard', 'डैशबोर्ड लोड नहीं हो सका')));
  }, []);

  const profit = data?.profitSummary as Record<string, number> | undefined;
  const stats = data?.quickStats as Record<string, unknown> | undefined;

  return (
    <div className="page">
      <h2>{t('Farm Easy Portal', 'फार्म ईज़ी पोर्टल')}</h2>
      <p className="muted">{t('Seeds to sales — your digital farm hub', 'बीज से बिक्री तक — आपका डिजिटल खेत केंद्र')}</p>

      {error && <p className="error">{error}</p>}

      <div className="stat-grid">
        <div className="card stat">
          <span className="stat-label">{t('Season profit (est.)', 'सीज़न लाभ (अनुमान)')}</span>
          <strong>₹{profit?.estimatedProfitInr?.toLocaleString('en-IN') ?? '—'}</strong>
          <small>+{profit?.profitChangePercent ?? 0}% {t('vs last season', 'पिछले सीज़न से')}</small>
        </div>
        <div className="card stat">
          <span className="stat-label">{t('Water saved', 'पानी की बचत')}</span>
          <strong>{String(stats?.waterSavedPercent ?? 18)}%</strong>
          <small>{t('via Smart Chip', 'स्मार्ट चिप से')}</small>
        </div>
        <div className="card stat">
          <span className="stat-label">{t('Active crop listings', 'सक्रिय फसल सूची')}</span>
          <strong>{String(stats?.activeListings ?? 0)}</strong>
        </div>
        <div className="card stat">
          <span className="stat-label">{t('Smart Chip', 'स्मार्ट चिप')}</span>
          <strong>{stats?.smartChipOnline ? t('Online', 'ऑनलाइन') : t('Offline', 'ऑफलाइन')}</strong>
        </div>
      </div>

      <section className="card">
        <h3>{t('Journey', 'यात्रा')}</h3>
        <ol className="journey-list">
          <li>{t('Buy seeds & book equipment', 'बीज खरीदें और उपकरण बुक करें')}</li>
          <li>{t('Smart Chip automates irrigation', 'स्मार्ट चिप सिंचाई स्वचालित करती है')}</li>
          <li>{t('Sell directly to mandi or factory', 'मंडी या फैक्टरी को सीधे बेचें')}</li>
          <li>{t('Review profits & plan next season', 'लाभ देखें और अगला सीज़न प्लान करें')}</li>
        </ol>
      </section>
    </div>
  );
}
