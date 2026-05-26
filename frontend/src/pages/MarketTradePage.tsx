import { useEffect, useState } from 'react';
import { getCropListings, getMandiPrices, t } from '../api/client';

export default function MarketTradePage() {
  const [listings, setListings] = useState<Record<string, unknown>[]>([]);
  const [prices, setPrices] = useState<Record<string, unknown>[]>([]);
  const locale = localStorage.getItem('locale') || 'en';

  useEffect(() => {
    Promise.all([getCropListings(), getMandiPrices()])
      .then(([l, p]) => {
        setListings(l as Record<string, unknown>[]);
        setPrices(p as Record<string, unknown>[]);
      })
      .catch(() => {});
  }, []);

  return (
    <div className="page">
      <h2>{t('Market & Trade', 'बाज़ार और व्यापार')}</h2>

      <section className="card">
        <h3>{t('Live mandi prices', 'लाइव मंडी भाव')}</h3>
        <ul className="price-list">
          {prices.map((p) => (
            <li key={String(p.id)}>
              <strong>{String(p.mandiName)}</strong> —{' '}
              {locale === 'hi' && p.cropNameHi ? String(p.cropNameHi) : String(p.cropName)}
              <span className="price"> ₹{Number(p.pricePerQuintal).toLocaleString('en-IN')}/q</span>
              <span className={`trend trend-${String(p.trend).toLowerCase()}`}>{String(p.trend)}</span>
            </li>
          ))}
        </ul>
      </section>

      <section className="card">
        <h3>{t('Sell crops (direct)', 'फसल बेचें (सीधा)')}</h3>
        <ul className="listing-list">
          {listings.map((l) => (
            <li key={String(l.id)}>
              <strong>{locale === 'hi' && l.cropNameHi ? String(l.cropNameHi) : String(l.cropName)}</strong>
              <span> — {String(l.quantityKg)} kg @ ₹{Number(l.pricePerKg).toFixed(2)}/kg</span>
              <br />
              <small>{String(l.location)} · {String(l.buyerType)}</small>
            </li>
          ))}
        </ul>
        <p className="muted">{t('No brokers — higher farmer profits', 'कोई दलाल नहीं — ज़्यादा किसान लाभ')}</p>
      </section>
    </div>
  );
}
