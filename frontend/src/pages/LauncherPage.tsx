import { Link } from 'react-router-dom';
import { APP_TILES } from '../data/appTiles';

export default function LauncherPage() {
  return (
    <div className="launcher">
      <div className="launcher-brand" aria-hidden="true">
        <img src="/icon.svg" alt="" width={48} height={48} />
      </div>

      <div className="launcher-grid" role="list">
        {APP_TILES.map((tile) => (
          <Link
            key={tile.id}
            to={tile.to}
            className="launcher-tile"
            role="listitem"
            aria-label={tile.ariaLabel}
          >
            <img src={tile.icon} alt="" className="launcher-tile-icon" draggable={false} />
          </Link>
        ))}
      </div>
    </div>
  );
}
