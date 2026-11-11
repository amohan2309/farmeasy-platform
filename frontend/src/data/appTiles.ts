export type AppTile = {
  id: string;
  to: string;
  icon: string;
  /** Screen-reader only — not shown in UI */
  ariaLabel: string;
};

export const APP_TILES: AppTile[] = [
  { id: 'shop', to: '/marketplace', icon: '/icons/shop.svg', ariaLabel: 'Shop' },
  { id: 'sell', to: '/market', icon: '/icons/sell.svg', ariaLabel: 'Sell crop' },
  { id: 'smart-chip', to: '/smart-chip', icon: '/icons/smart-chip.svg', ariaLabel: 'Smart Chip' },
  { id: 'water', to: '/irrigation', icon: '/icons/water.svg', ariaLabel: 'Water and irrigation' },
  { id: 'weather', to: '/weather', icon: '/icons/weather.svg', ariaLabel: 'Weather' },
  { id: 'learn', to: '/learn', icon: '/icons/learn.svg', ariaLabel: 'Learn' },
  { id: 'ai', to: '/assistant', icon: '/icons/ai.svg', ariaLabel: 'Crop AI camera' },
];

export const LAUNCHER_PATH = '/';

export function isLauncherPath(pathname: string) {
  return pathname === LAUNCHER_PATH;
}
