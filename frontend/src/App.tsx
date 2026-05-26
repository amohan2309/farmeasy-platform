import { Navigate, Route, Routes } from 'react-router-dom';
import WebAppLayout from './layout/WebAppLayout';
import LoginPage from './pages/LoginPage';
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
        <Route path="/apps" element={<AppsPage />} />
        <Route path="/apps/:appId" element={<AppMenuPage />} />
        <Route path="/chatbot" element={<ChatbotPage />} />
      </Route>
      <Route path="/" element={<Navigate to="/apps" replace />} />
      <Route path="*" element={<Navigate to="/login" replace />} />
    </Routes>
  );
}
