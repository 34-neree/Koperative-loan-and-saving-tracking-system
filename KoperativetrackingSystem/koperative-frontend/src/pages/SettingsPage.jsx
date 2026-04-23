import { useState, useEffect } from 'react';
import TopBar from '../components/TopBar';
import { sharesAPI } from '../api';

export default function SettingsPage() {
  const [config, setConfig] = useState({ sharePrice:'', minimumSharesForLoan:'' });
  const [saving, setSaving] = useState(false);
  const [saved, setSaved] = useState(false);

  useEffect(() => {
    sharesAPI.getConfig().then(r => setConfig({
      sharePrice: r.data.sharePrice || '',
      minimumSharesForLoan: r.data.minimumSharesForLoan || ''
    })).catch(() => {});
  }, []);

  const handleSave = async(e) => {
    e.preventDefault();
    setSaving(true); setSaved(false);
    try {
      await sharesAPI.updateConfig(config);
      setSaved(true);
      setTimeout(() => setSaved(false), 3000);
    } catch(err) { alert(err.response?.data?.message || 'Error saving'); }
    finally { setSaving(false); }
  };

  return (
    <>
      <TopBar title="System Settings" />
      <div className="page-content">
        <div className="card" style={{ maxWidth: 600 }}>
          <h2 style={{ fontSize: 18, fontWeight: 700, marginBottom: 8 }}>Share Capital Configuration</h2>
          <p style={{ color: '#6B7280', fontSize: 14, marginBottom: 24 }}>
            Configure the share price and minimum shares required for loan eligibility.
          </p>
          {saved && (
            <div style={{ background: '#E8F5E9', color: '#2E7D32', padding: '10px 16px', borderRadius: 8, marginBottom: 16, fontWeight: 600, fontSize: 14 }}>
              ✅ Settings saved successfully!
            </div>
          )}
          <form onSubmit={handleSave}>
            <div className="form-group">
              <label className="form-label">Share Price (RWF)</label>
              <input className="form-input" type="number" min="0" required
                value={config.sharePrice}
                onChange={e => setConfig({...config, sharePrice: e.target.value})} />
              <p style={{ fontSize: 12, color: '#9CA3AF', marginTop: 4 }}>The price of one share in Rwandan Francs</p>
            </div>
            <div className="form-group">
              <label className="form-label">Minimum Shares for Loan Eligibility</label>
              <input className="form-input" type="number" min="0" required
                value={config.minimumSharesForLoan}
                onChange={e => setConfig({...config, minimumSharesForLoan: e.target.value})} />
              <p style={{ fontSize: 12, color: '#9CA3AF', marginTop: 4 }}>Members must hold at least this many shares to apply for a loan</p>
            </div>
            <button type="submit" className="btn btn-primary" disabled={saving}>
              {saving ? 'Saving...' : 'Save Settings'}
            </button>
          </form>
        </div>
      </div>
    </>
  );
}
