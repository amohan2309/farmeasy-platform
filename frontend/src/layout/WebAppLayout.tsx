import { Link, Outlet, useLocation, useNavigate } from 'react-router-dom';
import { clearSession } from '../api/client';

const NAV = [
  { to: '/apps', label: 'Apps', labelHi: 'ऐप्स' },
  { to: '/chatbot', label: 'Crop AI', labelHi: 'फसल AI' },
] as const;

export default function WebAppLayout() {
  const location = useLocation();
  const navigate = useNavigate();
  const locale = localStorage.getItem('locale') || 'en';

  function logout() {
    clearSession();
    navigate('/login');
  }

  return (
    <div className="web-app">
      <aside className="sidebar" aria-label="Main navigation">
        <div className="brand">
          <img src="/icon.svg" alt="" width={36} height={36} />
          <div>
            <strong>FarmEasy</strong>
            <span>Web App</span>
          </div>
        </div>
        <nav>
          {NAV.map((item) => (
            <Link
              key={item.to}
              to={item.to}
              className={location.pathname.startsWith(item.to) ? 'active' : ''}
            >
              {locale === 'hi' ? item.labelHi : item.label}
            </Link>
          ))}
        </nav>
        <button type="button" className="btn ghost sidebar-logout" onClick={logout}>
          Logout
        </button>
      </aside>

      <div className="web-main">
        <header className="web-header">
          <div className="web-header-inner">
            <h1 className="web-header-title">FarmEasy</h1>
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
                Logout
              </button>
            </div>
          </div>
        </header>

        <main className="web-content">
          <Outlet />
        </main>

        <nav className="bottom-nav hide-desktop" aria-label="Mobile navigation">
          {NAV.map((item) => (
            <Link
              key={item.to}
              to={item.to}
              className={location.pathname.startsWith(item.to) ? 'active' : ''}
            >
              {locale === 'hi' ? item.labelHi : item.label}
            </Link>
          ))}
        </nav>
      </div>
    </div>
  );
}
