import { useState } from 'react';
import { analyzeImage } from '../api/client';

export default function ChatbotPage() {
  const [file, setFile] = useState<File | null>(null);
  const [preview, setPreview] = useState<string | null>(null);
  const [report, setReport] = useState<Record<string, unknown> | null>(null);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const locale = localStorage.getItem('locale') || 'hi';

  function onFileChange(f: File | null) {
    setFile(f);
    if (preview) URL.revokeObjectURL(preview);
    setPreview(f ? URL.createObjectURL(f) : null);
  }

  async function onAnalyze() {
    if (!file) return;
    setLoading(true);
    setError('');
    setReport(null);
    try {
      const res = await analyzeImage(file, locale, false);
      setReport(res);
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Failed');
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="page-inner">
      <h2>{locale === 'hi' ? 'फसल सहायक' : 'Crop Assistant'}</h2>
      <p className="muted">{locale === 'hi' ? 'गैलरी या कैमरा से फोटो अपलोड करें' : 'Upload a photo from gallery or camera'}</p>

      <div className="card">
        <input
          type="file"
          accept="image/*"
          capture="environment"
          onChange={(e) => onFileChange(e.target.files?.[0] || null)}
        />
        {preview && <img src={preview} alt="Crop preview" className="preview-img" />}
        <button type="button" className="btn primary" disabled={!file || loading} onClick={onAnalyze}>
          {loading ? (locale === 'hi' ? 'विश्लेषण…' : 'Analyzing…') : (locale === 'hi' ? 'विश्लेषण करें' : 'Analyze')}
        </button>
      </div>

      {error && <p className="error">{error}</p>}
      {report && (
        <div className="card report">
          <h3>{locale === 'hi' ? 'रिपोर्ट' : 'Report'}</h3>
          <p>{String(report.summary)}</p>
          <p className="muted">{locale === 'hi' ? 'विश्वास' : 'Confidence'}: {String(report.confidence)}</p>
          <p className="disclaimer">{String(report.disclaimer)}</p>
        </div>
      )}
    </div>
  );
}
