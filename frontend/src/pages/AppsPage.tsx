import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { getEntitlements, listApps, type Entitlements } from '../api/client';

type FarmApp = { id: string; code: string; titleEn: string; titleHi: string; descriptionEn: string };

export default function AppsPage() {
  const [apps, setApps] = useState<FarmApp[]>([]);
  const [entitlements, setEntitlements] = useState<Entitlements | null>(null);
  const [error, setError] = useState('');
  const locale = localStorage.getItem('locale') || 'en';

  const hasLayer3 = entitlements?.features?.includes('LAYER3_UI');

  useEffect(() => {
    Promise.all([listApps(), getEntitlements()])
      .then(([appList, ent]) => {
        setApps(appList);
        setEntitlements(ent);
      })
      .catch(() => setError('Could not load applications'));
  }, []);

  return (
    <div className="page-inner">
      <div className="page-head">
        <h2>{locale === 'hi' ? 'एप्लिकेशन' : 'Applications'}</h2>
        <span className="badge">{entitlements?.planCode || 'FREE'}</span>
      </div>

      {!hasLayer3 && (
        <div className="banner upgrade">
          {locale === 'hi'
            ? 'फ्री प्लान: ऐप सूची। प्रो में अध्याय मेनू और असीमित फसल स्कैन।'
            : 'Free plan: app list only. Pro unlocks chapter menus and unlimited crop scans.'}
        </div>
      )}

      <div className="app-grid">
        {apps.map((app) => (
          <Link
            key={app.id}
            to={hasLayer3 ? `/learn/${app.id}` : '#'}
            className={`app-card ${!hasLayer3 ? 'disabled' : ''}`}
            onClick={(e) => { if (!hasLayer3) e.preventDefault(); }}
          >
            <h3>{locale === 'hi' ? (app.titleHi || app.titleEn) : app.titleEn}</h3>
            <p>{app.descriptionEn}</p>
          </Link>
        ))}
      </div>

      {error && <p className="error">{error}</p>}
    </div>
  );
}
