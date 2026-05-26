import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { detectLocale, loginWithPassword, saveSession, sendPhoneOtp, verifyPhoneOtp } from '../api/client';

type Tab = 'password' | 'phone';

export default function LoginPage() {
  const navigate = useNavigate();
  const [tab, setTab] = useState<Tab>('phone');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [phone, setPhone] = useState('');
  const [otp, setOtp] = useState('');
  const [otpSent, setOtpSent] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const locale = localStorage.getItem('locale') || 'en';

  useEffect(() => {
    if (!navigator.geolocation) return;
    navigator.geolocation.getCurrentPosition(
      async (pos) => {
        try {
          const { locale: detected } = await detectLocale(pos.coords.latitude, pos.coords.longitude);
          localStorage.setItem('locale', detected);
        } catch { /* ignore */ }
      },
      () => undefined,
      { timeout: 5000 }
    );
  }, []);

  async function afterLogin() {
    navigate('/apps');
  }

  async function handlePasswordLogin(e: React.FormEvent) {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      const res = await loginWithPassword(username, password);
      saveSession(res);
      await afterLogin();
    } catch {
      setError(locale === 'hi' ? 'गलत उपयोगकर्ता या पासवर्ड' : 'Invalid username or password');
    } finally {
      setLoading(false);
    }
  }

  async function handleSendOtp() {
    setLoading(true);
    setError('');
    try {
      await sendPhoneOtp(phone);
      setOtpSent(true);
    } catch {
      setError(locale === 'hi' ? 'OTP नहीं भेजा जा सका' : 'Could not send OTP');
    } finally {
      setLoading(false);
    }
  }

  async function handleVerifyOtp(e: React.FormEvent) {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      const res = await verifyPhoneOtp(phone, otp);
      saveSession(res);
      await afterLogin();
    } catch {
      setError(locale === 'hi' ? 'गलत OTP' : 'Invalid OTP');
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="login-shell">
      <div className="login-card">
        <header className="hero">
          <img src="/icon.svg" alt="" width={64} height={64} />
          <h1>FarmEasy</h1>
          <p>{locale === 'hi' ? 'किसानों के लिए वेब ऐप' : 'Web application for farmers'}</p>
        </header>

        <div className="tabs">
          <button type="button" className={tab === 'phone' ? 'active' : ''} onClick={() => setTab('phone')}>
            {locale === 'hi' ? 'मोबाइल OTP' : 'Mobile OTP'}
          </button>
          <button type="button" className={tab === 'password' ? 'active' : ''} onClick={() => setTab('password')}>
            {locale === 'hi' ? 'पासवर्ड' : 'Password'}
          </button>
        </div>

        {tab === 'phone' && (
          <form className="form-block" onSubmit={handleVerifyOtp}>
            <label>{locale === 'hi' ? 'मोबाइल नंबर' : 'Mobile number'}</label>
            <input value={phone} onChange={(e) => setPhone(e.target.value)} placeholder="+91..." required />
            {!otpSent ? (
              <button type="button" className="btn primary" onClick={handleSendOtp} disabled={loading}>
                {locale === 'hi' ? 'OTP भेजें' : 'Send OTP'}
              </button>
            ) : (
              <>
                <label>OTP</label>
                <input value={otp} onChange={(e) => setOtp(e.target.value)} placeholder="6-digit" required />
                <button className="btn primary" disabled={loading}>{locale === 'hi' ? 'लॉगिन' : 'Login'}</button>
              </>
            )}
          </form>
        )}

        {tab === 'password' && (
          <form className="form-block" onSubmit={handlePasswordLogin}>
            <label>{locale === 'hi' ? 'ईमेल / उपयोगकर्ता' : 'Email / username'}</label>
            <input value={username} onChange={(e) => setUsername(e.target.value)} required />
            <label>{locale === 'hi' ? 'पासवर्ड' : 'Password'}</label>
            <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
            <button className="btn primary" disabled={loading}>{locale === 'hi' ? 'लॉगिन' : 'Login'}</button>
          </form>
        )}

        {error && <p className="error">{error}</p>}
        <p className="hint">{locale === 'hi' ? 'आधार लॉगिन — जल्द आ रहा है' : 'Aadhaar login — coming soon'}</p>
      </div>
    </div>
  );
}
