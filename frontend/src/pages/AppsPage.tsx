import { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { clearSession, getEntitlements, listApps, type Entitlements } from '../api/client';

type FarmApp = { id: string; code: string; titleEn: string; titleHi: string; descriptionEn: string };

export default function AppsPage() {
  const navigate = useNavigate();
  const [apps, setApps] = useState<FarmApp[]>([]);
  const [entitlements, setEntitlements] = useState<Entitlements | null>(null);
  const [error, setError] = useState('');

  const hasLayer3 = entitlements?.features?.includes('LAYER3_UI');

  useEffect(() => {
    Promise.all([listApps(), getEntitlements()])
      .then(([appList, ent]) => {
        setApps(appList);
        setEntitlements(ent);
      })
      .catch(() => setError('Could not load applications'));
  }, []);

  function logout() {
    clearSession();
    navigate('/login');
  }

  return (
    <div className="page">
      <header className="topbar">
        <h1>Applications</h1>
        <div className="topbar-actions">
          <span className="badge">{entitlements?.planCode || 'FREE'}</span>
          <button type="button" className="btn ghost" onClick={logout}>Logout</button>
        </div>
      </header>

      {!hasLayer3 && (
        <div className="banner upgrade">
          Free plan: 2-layer UI. Upgrade to <strong>Pro</strong> for chapter menus and unlimited crop scans.
        </div>
      )}

      <div className="app-grid">
        {apps.map((app) => (
          <Link
            key={app.id}
            to={hasLayer3 ? `/apps/${app.id}` : '#'}
            className={`app-card ${!hasLayer3 ? 'disabled' : ''}`}
            onClick={(e) => { if (!hasLayer3) e.preventDefault(); }}
          >
            <h3>{app.titleHi || app.titleEn}</h3>
            <p>{app.descriptionEn}</p>
          </Link>
        ))}
      </div>

      <Link to="/chatbot" className="fab">Crop AI</Link>
      {error && <p className="error">{error}</p>}
    </div>
  );
}
