import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { loginWithPassword, saveSession, sendPhoneOtp, verifyPhoneOtp } from '../api/client';

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

  async function handlePasswordLogin(e: React.FormEvent) {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      const res = await loginWithPassword(username, password);
      saveSession(res);
      navigate('/apps');
    } catch {
      setError('Invalid username or password');
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
      setError('Could not send OTP');
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
      navigate('/apps');
    } catch {
      setError('Invalid OTP');
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="page">
      <header className="hero">
        <h1>FarmEasy</h1>
        <p>किसानों के लिए डिजिटल सहायक</p>
      </header>

      <div className="tabs">
        <button type="button" className={tab === 'phone' ? 'active' : ''} onClick={() => setTab('phone')}>
          Mobile OTP
        </button>
        <button type="button" className={tab === 'password' ? 'active' : ''} onClick={() => setTab('password')}>
          Password
        </button>
      </div>

      {tab === 'phone' && (
        <form className="card" onSubmit={handleVerifyOtp}>
          <label>Mobile number</label>
          <input value={phone} onChange={(e) => setPhone(e.target.value)} placeholder="+91..." required />
          {!otpSent ? (
            <button type="button" className="btn primary" onClick={handleSendOtp} disabled={loading}>
              Send OTP
            </button>
          ) : (
            <>
              <label>OTP</label>
              <input value={otp} onChange={(e) => setOtp(e.target.value)} placeholder="6-digit code" required />
              <button className="btn primary" disabled={loading}>Login</button>
            </>
          )}
        </form>
      )}

      {tab === 'password' && (
        <form className="card" onSubmit={handlePasswordLogin}>
          <label>Username or email</label>
          <input value={username} onChange={(e) => setUsername(e.target.value)} required />
          <label>Password</label>
          <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
          <button className="btn primary" disabled={loading}>Login</button>
        </form>
      )}

      {error && <p className="error">{error}</p>}
      <p className="hint">Aadhaar login — coming soon (UIDAI-compliant adapter)</p>
    </div>
  );
}
