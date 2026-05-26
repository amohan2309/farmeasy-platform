import { Navigate, Route, Routes } from 'react-router-dom';
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
      <Route path="/apps" element={<PrivateRoute><AppsPage /></PrivateRoute>} />
      <Route path="/apps/:appId" element={<PrivateRoute><AppMenuPage /></PrivateRoute>} />
      <Route path="/chatbot" element={<PrivateRoute><ChatbotPage /></PrivateRoute>} />
      <Route path="*" element={<Navigate to="/login" replace />} />
    </Routes>
  );
}
