export type AppTile = {
  id: string;
  to: string;
  icon: string;
  /** Screen-reader only — not shown in UI */
  ariaLabel: string;
};

export const APP_TILES: AppTile[] = [
  { id: 'shop', to: '/marketplace', icon: '/icons/shop.svg', ariaLabel: 'Buy seeds and tools' },
  { id: 'water', to: '/irrigation', icon: '/icons/water.svg', ariaLabel: 'Water pump and irrigation' },
  { id: 'smart-chip', to: '/smart-chip', icon: '/icons/smart-chip.svg', ariaLabel: 'Soil sensor in field' },
  { id: 'ai', to: '/assistant', icon: '/icons/ai.svg', ariaLabel: 'Photo crop disease check' },
  { id: 'sell', to: '/market', icon: '/icons/sell.svg', ariaLabel: 'Sell crop for money' },
  { id: 'learn', to: '/learn', icon: '/icons/learn.svg', ariaLabel: 'Watch farming videos' },
];

export const LAUNCHER_PATH = '/';

export function isLauncherPath(pathname: string) {
  return pathname === LAUNCHER_PATH;
}
