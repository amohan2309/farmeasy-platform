import { Link, Outlet, useLocation, useNavigate } from 'react-router-dom';
import { clearSession, t } from '../api/client';

const NAV = [
  { to: '/', label: 'Home', labelHi: 'होम', icon: '🏠' },
  { to: '/marketplace', label: 'Shop', labelHi: 'दुकान', icon: '🛒' },
  { to: '/market', label: 'Sell', labelHi: 'बेचें', icon: '📈' },
  { to: '/smart-chip', label: 'Smart Chip', labelHi: 'स्मार्ट चिप', icon: '📡' },
  { to: '/irrigation', label: 'Water', labelHi: 'पानी', icon: '💧' },
  { to: '/learn', label: 'Learn', labelHi: 'सीखें', icon: '📚' },
  { to: '/assistant', label: 'AI', labelHi: 'AI', icon: '🌾' },
] as const;

export default function WebAppLayout() {
  const location = useLocation();
  const navigate = useNavigate();
  const locale = localStorage.getItem('locale') || 'en';

  function logout() {
    clearSession();
    navigate('/login');
  }

  function isActive(path: string) {
    if (path === '/') return location.pathname === '/';
    return location.pathname.startsWith(path);
  }

  return (
    <div className="web-app">
      <aside className="sidebar" aria-label="Main navigation">
        <div className="brand">
          <img src="/icon.svg" alt="" width={36} height={36} />
          <div>
            <strong>Farm Easy</strong>
            <span>{t('Portal', 'पोर्टल')}</span>
          </div>
        </div>
        <nav>
          {NAV.map((item) => (
            <Link key={item.to} to={item.to} className={isActive(item.to) ? 'active' : ''}>
              <span className="nav-icon">{item.icon}</span>
              {locale === 'hi' ? item.labelHi : item.label}
            </Link>
          ))}
        </nav>
        <button type="button" className="btn ghost sidebar-logout" onClick={logout}>
          {t('Logout', 'लॉग आउट')}
        </button>
      </aside>

      <div className="web-main">
        <header className="web-header">
          <div className="web-header-inner">
            <h1 className="web-header-title">Farm Easy</h1>
            <div className="web-header-actions">
              <select
                aria-label="Language"
                value={locale}
                onChange={(e) => {
                  localStorage.setItem('locale', e.target.value);
                  window.location.reload();
                }}
              >
                <option value="en">English</option>
                <option value="hi">हिंदी</option>
              </select>
              <button type="button" className="btn ghost hide-desktop" onClick={logout}>
                {t('Logout', 'लॉग आउट')}
              </button>
            </div>
          </div>
        </header>

        <main className="web-content">
          <Outlet />
        </main>

        <nav className="bottom-nav hide-desktop" aria-label="Mobile navigation">
          {NAV.slice(0, 5).map((item) => (
            <Link key={item.to} to={item.to} className={isActive(item.to) ? 'active' : ''}>
              <span className="nav-icon">{item.icon}</span>
              {locale === 'hi' ? item.labelHi : item.label}
            </Link>
          ))}
        </nav>
      </div>
    </div>
  );
}
