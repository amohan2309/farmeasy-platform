import { useEffect, useState } from 'react';
import { detectLocale, loginWithPassword, saveSession, sendPhoneOtp, verifyPhoneOtp } from '../api/client';

type Tab = 'password' | 'phone';

const DEMO_PHONE = '9876543210';
const DEMO_OTP = '123456';
const DEMO_USER = 'demo';
const DEMO_PASSWORD = 'FarmEasy123';

function loginErrorMessage(err: unknown, locale: string) {
  const code = err instanceof Error ? err.message : '';
  if (code === 'NETWORK') {
    return locale === 'hi'
      ? 'Backend नहीं मिला — terminal में: docker compose up -d db redis authentication gateway'
      : 'Cannot reach backend — run: docker compose up -d db redis authentication gateway';
  }
  if (code === 'INVALID') {
    return locale === 'hi'
      ? 'गलत लॉगिन — demo / FarmEasy123 (Password tab)'
      : 'Wrong login — use Password tab: demo / FarmEasy123';
  }
  return locale === 'hi'
    ? 'लॉगिन विफल — demo / FarmEasy123 आज़माएं'
    : 'Login failed — try Password tab: demo / FarmEasy123';
}

export default function LoginPage() {
  const [tab, setTab] = useState<Tab>('password');
  const [username, setUsername] = useState(DEMO_USER);
  const [password, setPassword] = useState(DEMO_PASSWORD);
  const [phone, setPhone] = useState(DEMO_PHONE);
  const [otp, setOtp] = useState(DEMO_OTP);
  const [otpSent, setOtpSent] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [backendOk, setBackendOk] = useState<boolean | null>(null);
  const locale = localStorage.getItem('locale') || 'en';

  useEffect(() => {
    if ('serviceWorker' in navigator) {
      navigator.serviceWorker.getRegistrations().then((regs) => {
        regs.forEach((r) => r.unregister());
      });
    }

    fetch('/api/auth/phone/send-otp', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ phoneNumber: DEMO_PHONE }),
    })
      .then((res) => setBackendOk(res.ok || res.status === 204))
      .catch(() => setBackendOk(false));

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

  function finishLogin(res: Awaited<ReturnType<typeof loginWithPassword>>) {
    saveSession(res);
    window.location.assign('/');
  }

  async function handlePasswordLogin(e: React.FormEvent) {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      finishLogin(await loginWithPassword(username.trim(), password));
    } catch (err) {
      setError(loginErrorMessage(err, locale));
    } finally {
      setLoading(false);
    }
  }

  async function handleSendOtp() {
    setLoading(true);
    setError('');
    try {
      await sendPhoneOtp(phone.trim());
      setOtpSent(true);
    } catch (err) {
      setError(loginErrorMessage(err, locale));
    } finally {
      setLoading(false);
    }
  }

  async function handleVerifyOtp(e: React.FormEvent) {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      finishLogin(await verifyPhoneOtp(phone.trim(), otp.trim()));
    } catch (err) {
      setError(
        locale === 'hi'
          ? 'OTP गलत — पहले Send OTP, फिर 123456'
          : 'Wrong OTP — tap Send OTP first, then enter 123456'
      );
    } finally {
      setLoading(false);
    }
  }

  async function quickDemoLogin() {
    setLoading(true);
    setError('');
    try {
      finishLogin(await loginWithPassword(DEMO_USER, DEMO_PASSWORD));
    } catch (err) {
      setError(loginErrorMessage(err, locale));
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

        <p className={`backend-status ${backendOk === true ? 'ok' : backendOk === false ? 'bad' : ''}`}>
          {backendOk === null && (locale === 'hi' ? 'Backend जाँच हो रही है…' : 'Checking backend…')}
          {backendOk === true && (locale === 'hi' ? '✓ Backend चालू है' : '✓ Backend is running')}
          {backendOk === false && (locale === 'hi' ? '✗ Backend नहीं चल रहा' : '✗ Backend is not running')}
        </p>

        <div className="demo-box">
          <button type="button" className="btn primary demo-login-btn" onClick={quickDemoLogin} disabled={loading}>
            {locale === 'hi' ? '▶ तुरंत लॉगिन (demo)' : '▶ Quick demo login'}
          </button>
          <p className="demo-box-hint">
            Password: <strong>demo</strong> / <strong>FarmEasy123</strong>
            <br />
            OTP: <strong>9876543210</strong> → Send OTP → <strong>123456</strong>
          </p>
        </div>

        <div className="tabs">
          <button type="button" className={tab === 'password' ? 'active' : ''} onClick={() => setTab('password')}>
            {locale === 'hi' ? 'पासवर्ड' : 'Password'}
          </button>
          <button type="button" className={tab === 'phone' ? 'active' : ''} onClick={() => setTab('phone')}>
            {locale === 'hi' ? 'मोबाइल OTP' : 'Mobile OTP'}
          </button>
        </div>

        {tab === 'phone' && (
          <form className="form-block" onSubmit={handleVerifyOtp}>
            <label>{locale === 'hi' ? 'मोबाइल नंबर' : 'Mobile number'}</label>
            <input value={phone} onChange={(e) => setPhone(e.target.value)} required />
            {!otpSent ? (
              <button type="button" className="btn primary" onClick={handleSendOtp} disabled={loading}>
                {locale === 'hi' ? 'OTP भेजें' : 'Send OTP'}
              </button>
            ) : (
              <>
                <label>OTP</label>
                <input value={otp} onChange={(e) => setOtp(e.target.value)} required />
                <button className="btn primary" disabled={loading}>{locale === 'hi' ? 'लॉगिन' : 'Login'}</button>
              </>
            )}
          </form>
        )}

        {tab === 'password' && (
          <form className="form-block" onSubmit={handlePasswordLogin}>
            <label>{locale === 'hi' ? 'उपयोगकर्ता' : 'Username'}</label>
            <input value={username} onChange={(e) => setUsername(e.target.value)} required />
            <label>{locale === 'hi' ? 'पासवर्ड' : 'Password'}</label>
            <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
            <button className="btn primary" disabled={loading}>{locale === 'hi' ? 'लॉगिन' : 'Login'}</button>
          </form>
        )}

        {error && <p className="error">{error}</p>}
      </div>
    </div>
  );
}
