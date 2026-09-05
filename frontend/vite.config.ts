import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import { VitePWA } from 'vite-plugin-pwa';

export default defineConfig({
  plugins: [
    react(),
    VitePWA({
      registerType: 'autoUpdate',
      includeAssets: ['icon.svg'],
      manifest: {
        name: 'FarmEasy — Farmer Web App',
        short_name: 'FarmEasy',
        description: 'Indian farmer super-app: schemes, crop AI, regional languages',
        theme_color: '#2d6a4f',
        background_color: '#f1f8f4',
        display: 'standalone',
        orientation: 'portrait-primary',
        start_url: '/',
        scope: '/',
        lang: 'en',
        categories: ['agriculture', 'productivity'],
        icons: [
          { src: '/icon.svg', sizes: 'any', type: 'image/svg+xml', purpose: 'any' },
        ],
      },
      workbox: {
        globPatterns: ['**/*.{js,css,html,ico,png,svg,woff2}'],
        runtimeCaching: [
          {
            urlPattern: /^https?:\/\/.*\/api\/(?!auth).*/i,
            handler: 'NetworkFirst',
            options: {
              cacheName: 'farmeasy-api',
              networkTimeoutSeconds: 10,
              expiration: { maxEntries: 32, maxAgeSeconds: 300 },
            },
          },
        ],
      },
      devOptions: {
        enabled: false,
      },
    }),
  ],
  server: {
    port: 5173,
    host: true,
    proxy: {
      '/api': {
        target: process.env.VITE_API_PROXY || 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
  preview: {
    port: 3000,
    host: true,
    proxy: {
      '/api': {
        target: process.env.VITE_API_PROXY || 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
});
