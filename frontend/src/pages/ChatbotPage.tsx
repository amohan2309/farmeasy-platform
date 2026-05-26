import { useState } from 'react';
import { Link } from 'react-router-dom';
import { analyzeImage } from '../api/client';

export default function ChatbotPage() {
  const [file, setFile] = useState<File | null>(null);
  const [report, setReport] = useState<Record<string, unknown> | null>(null);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  async function onAnalyze() {
    if (!file) return;
    setLoading(true);
    setError('');
    setReport(null);
    try {
      const res = await analyzeImage(file, 'hi', false);
      setReport(res);
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Failed');
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="page">
      <header className="topbar">
        <Link to="/apps" className="back">← Apps</Link>
        <h1>Crop Assistant</h1>
      </header>

      <div className="card">
        <label>Upload crop / leaf photo</label>
        <input type="file" accept="image/*" capture="environment" onChange={(e) => setFile(e.target.files?.[0] || null)} />
        <button type="button" className="btn primary" disabled={!file || loading} onClick={onAnalyze}>
          {loading ? 'Analyzing…' : 'Analyze'}
        </button>
      </div>

      {error && <p className="error">{error}</p>}
      {report && (
        <div className="card report">
          <h3>Report</h3>
          <p>{String(report.summary)}</p>
          <p className="muted">Confidence: {String(report.confidence)}</p>
          <p className="disclaimer">{String(report.disclaimer)}</p>
        </div>
      )}
    </div>
  );
}
