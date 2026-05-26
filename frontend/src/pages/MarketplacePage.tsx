import { useEffect, useState } from 'react';
import { bookEquipment, listMarketplaceProducts, t } from '../api/client';

type Product = {
  id: string;
  category: string;
  nameEn: string;
  nameHi?: string;
  priceInr: number;
  unit: string;
  supplier?: string;
};

const TABS = ['SEED', 'FERTILIZER', 'PESTICIDE', 'EQUIPMENT'] as const;

export default function MarketplacePage() {
  const [tab, setTab] = useState<(typeof TABS)[number]>('SEED');
  const [products, setProducts] = useState<Product[]>([]);
  const [msg, setMsg] = useState('');

  useEffect(() => {
    listMarketplaceProducts(tab)
      .then(setProducts)
      .catch(() => setProducts([]));
  }, [tab]);

  async function hireEquipment(product: Product) {
    try {
      await bookEquipment(product.id, new Date().toISOString().slice(0, 10));
      setMsg(t('Equipment booked!', 'उपकरण बुक हो गया!'));
    } catch {
      setMsg(t('Booking failed', 'बुकिंग विफल'));
    }
  }

  const locale = localStorage.getItem('locale') || 'en';

  return (
    <div className="page">
      <h2>{t('Marketplace', 'बाज़ार')}</h2>
      <p className="muted">{t('Seeds, inputs & equipment hire', 'बीज, खाद और उपकरण किराया')}</p>
      {msg && <p className="success">{msg}</p>}

      <div className="tabs">
        {TABS.map((c) => (
          <button key={c} type="button" className={tab === c ? 'active' : ''} onClick={() => setTab(c)}>
            {c}
          </button>
        ))}
      </div>

      <div className="product-grid">
        {products.map((p) => (
          <article key={p.id} className="card product-card">
            <h4>{locale === 'hi' && p.nameHi ? p.nameHi : p.nameEn}</h4>
            <p className="price">₹{Number(p.priceInr).toLocaleString('en-IN')}</p>
            <p className="muted">{p.unit}</p>
            {p.supplier && <p className="muted">{p.supplier}</p>}
            {p.category === 'EQUIPMENT' && (
              <button type="button" className="btn" onClick={() => hireEquipment(p)}>
                {t('Book', 'बुक करें')}
              </button>
            )}
          </article>
        ))}
      </div>
    </div>
  );
}
