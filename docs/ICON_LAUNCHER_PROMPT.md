# FarmEasy Icon-Only Launcher — Design Plan & Build Prompt

## Problem

Current UI uses a **text sidebar / bottom nav** (Home, Shop, Sell, Smart Chip, Water, Learn, AI). This fails for **illiterate farmers** who cannot read labels and need to recognize features by **picture alone**.

## Goal

Replace navigation with an **iPhone-style home screen**: large **icon tiles only** (no words on the launcher). Tap a tile → open that feature. Tap home → return to tile grid.

## Audience

- Low literacy / illiterate end users
- First-time smartphone users
- Must understand app purpose from **icons only**

## UX Principles

1. **No text on launcher** — zero labels under icons
2. **Large touch targets** — minimum 88×88 px icons, generous spacing
3. **Familiar iOS pattern** — rounded-square icons on soft gradient background
4. **One action per tile** — tap opens feature immediately
5. **Simple way back** — pictorial home button (house/grid icon), no sidebar
6. **High contrast icons** — bold shapes, strong colors, recognizable farming symbols

## Information Architecture

| Tile | Icon meaning | Route | Visual |
|------|--------------|-------|--------|
| Shop | Buy seeds, fertilizer, tools | `/marketplace` | Bag of seeds |
| Sell | Mandi prices, sell crop | `/market` | Wheat + rupee |
| Smart Chip | Soil sensor in field | `/smart-chip` | Chip + plant |
| Water | Pump & irrigation | `/irrigation` | Water drop + tap |
| Learn | Schemes & training | `/learn` | Play button / book |
| Crop AI | Photo crop check | `/assistant` | Camera + leaf |

Post-login landing: **`/` (launcher)** — not sidebar, not Learn page.

## Layout Modes

### Launcher mode (`/`)
- Full-screen tile grid
- No sidebar, no bottom nav, no header text
- Optional: small FarmEasy logo top-center (brand only)
- 3×2 grid on phone, 4×2 on tablet/desktop

### Feature mode (all other routes)
- Minimal top bar: **home icon** (back to launcher) + **logout icon**
- No persistent sidebar or bottom tab bar
- Feature pages keep their content (can simplify text later)

## Components to Build

1. `LauncherPage.tsx` — icon grid home screen
2. `appTiles.ts` — tile config (route, icon, color)
3. `public/icons/*.svg` — 6 pictorial app icons
4. Update `WebAppLayout.tsx` — launcher vs feature chrome
5. Update `App.tsx` — `/` → LauncherPage, `/dashboard` → old HomePage
6. Update `LoginPage.tsx` — redirect to `/` after login
7. `styles.css` — `.launcher-*` iOS-style tile styles

## Visual Spec

- Background: soft green gradient (`#d8f3dc` → `#f1f8f4`)
- Icon shape: rounded square, `border-radius: 22%` (iOS squircle feel)
- Icon size: 72–96 px inner graphic, 100–112 px tile
- Grid gap: 24–32 px
- Tap feedback: scale 0.92 + shadow reduce
- Safe area padding for notched phones

## Success Criteria

- [ ] After login, user sees **only icons**, no nav words
- [ ] All 6 modules reachable from launcher
- [ ] Tap icon opens correct page
- [ ] Home button returns to launcher from any page
- [ ] Works on mobile (primary) and desktop
- [ ] Icons are distinct and farming-related without reading

## Build Prompt (for execution)

> Restructure FarmEasy frontend for illiterate farmers: replace sidebar/bottom-nav with a full-screen iPhone-style icon launcher at `/`. Show 6 large pictorial tiles (Shop, Sell, Smart Chip, Water, Learn, Crop AI) with **no text labels**. Create colorful SVG icons in `public/icons/`. On feature pages, hide sidebar/bottom nav; show only a pictorial home button to return to launcher. Redirect post-login to `/`. Move dashboard stats to `/dashboard` (optional tile later). Match iOS home screen aesthetics: rounded icon squares, soft gradient background, large touch targets.
