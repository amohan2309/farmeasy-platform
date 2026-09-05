import { Navigate, Route, Routes } from 'react-router-dom';
import WebAppLayout from './layout/WebAppLayout';
import LoginPage from './pages/LoginPage';
import LauncherPage from './pages/LauncherPage';
import HomePage from './pages/HomePage';
import MarketplacePage from './pages/MarketplacePage';
import MarketTradePage from './pages/MarketTradePage';
import SmartChipPage from './pages/SmartChipPage';
import IrrigationPage from './pages/IrrigationPage';
import AppsPage from './pages/AppsPage';
import AppMenuPage from './pages/AppMenuPage';
import ChatbotPage from './pages/ChatbotPage';

function PrivateRoute({ children }: { children: React.ReactNode }) {
  const token = localStorage.getItem('accessToken');
  return token ? <>{children}</> : <Navigate to="/login" replace />;
}

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route
        element={
          <PrivateRoute>
            <WebAppLayout />
          </PrivateRoute>
        }
      >
        <Route path="/" element={<LauncherPage />} />
        <Route path="/dashboard" element={<HomePage />} />
        <Route path="/marketplace" element={<MarketplacePage />} />
        <Route path="/market" element={<MarketTradePage />} />
        <Route path="/smart-chip" element={<SmartChipPage />} />
        <Route path="/irrigation" element={<IrrigationPage />} />
        <Route path="/learn" element={<AppsPage />} />
        <Route path="/learn/:appId" element={<AppMenuPage />} />
        <Route path="/assistant" element={<ChatbotPage />} />
        <Route path="/apps" element={<Navigate to="/" replace />} />
        <Route path="/apps/:appId" element={<Navigate to="/learn" replace />} />
        <Route path="/chatbot" element={<Navigate to="/assistant" replace />} />
      </Route>
      <Route path="*" element={<Navigate to="/login" replace />} />
    </Routes>
  );
}
