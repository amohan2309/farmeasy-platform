import { Link, Outlet, useLocation, useNavigate } from 'react-router-dom';
import { clearSession } from '../api/client';
import { isLauncherPath } from '../data/appTiles';

export default function WebAppLayout() {
  const location = useLocation();
  const navigate = useNavigate();
  const onLauncher = isLauncherPath(location.pathname);

  function logout() {
    clearSession();
    navigate('/login');
  }

  if (onLauncher) {
    return (
      <div className="web-app web-app--launcher">
        <Outlet />
      </div>
    );
  }

  return (
    <div className="web-app web-app--feature">
      <header className="feature-bar">
        <Link to="/" className="feature-bar-btn" aria-label="Home">
          <img src="/icons/home.svg" alt="" width={44} height={44} />
        </Link>
        <button type="button" className="feature-bar-btn" onClick={logout} aria-label="Logout">
          <img src="/icons/logout.svg" alt="" width={44} height={44} />
        </button>
      </header>

      <main className="web-content web-content--feature">
        <Outlet />
      </main>
    </div>
  );
}
