import { useState, useEffect } from 'react';
import TopBar from '../components/TopBar';
import { sharesAPI, membersAPI } from '../api';
import { useAuth } from '../context/AuthContext';

export default function SharesPage() {
  const { user, role } = useAuth();
  const isAdmin = role === 'ADMIN' || role === 'TREASURER';
  const [config, setConfig] = useState({ sharePrice: 0, minimumSharesForLoan: 0 });
  const [members, setMembers] = useState([]);
  const [selectedMember, setSelectedMember] = useState('');
  const [shares, setShares] = useState([]);
  const [showBuy, setShowBuy] = useState(false);
  const [buyForm, setBuyForm] = useState({ memberId:'', numberOfShares:1 });

  useEffect(() => {
    sharesAPI.getConfig().then(r => setConfig(r.data)).catch(() => {});
    if (isAdmin) {
      membersAPI.getAll().then(r => setMembers(r.data)).catch(() => {});
    } else if (user?.memberId) {
      setSelectedMember(String(user.memberId));
      loadShares(user.memberId);
    }
  }, []);

  const loadShares = (mid) => {
    setSelectedMember(mid);
    if (!mid) { setShares([]); return; }
    sharesAPI.getByMember(mid).then(r => setShares(r.data)).catch(() => setShares([]));
  };

  const totalShares = shares.reduce((a, s) => a + (s.numberOfShares || 0), 0);
  const totalValue = shares.reduce((a, s) => a + Number(s.totalValue || 0), 0);

  const handleBuy = async (e) => {
    e.preventDefault();
    try {
      const memberId = isAdmin ? buyForm.memberId : user.memberId;
      await sharesAPI.purchase({ ...buyForm, memberId });
      setShowBuy(false);
      loadShares(memberId);
    } catch (err) { alert(err.response?.data?.message || 'Error'); }
  };

  return (
    <>
      <TopBar title="Share Capital" />
      <div className="page-content">
        <div className="stat-cards" style={{ marginBottom: 24 }}>
          <div className="stat-card">
            <div className="stat-icon gold">💎</div>
            <div className="stat-info">
              <h3>Share Price</h3>
              <div className="stat-value">{Number(config.sharePrice).toLocaleString()} RWF</div>
            </div>
          </div>
          <div className="stat-card">
            <div className="stat-icon blue">📊</div>
            <div className="stat-info">
              <h3>Min. for Loan</h3>
              <div className="stat-value">{config.minimumSharesForLoan} shares</div>
            </div>
          </div>
          {selectedMember && (
            <>
              <div className="stat-card">
                <div className="stat-icon green">📈</div>
                <div className="stat-info">
                  <h3>My Shares</h3>
                  <div className="stat-value">{totalShares}</div>
                </div>
              </div>
              <div className="stat-card" style={{background:'linear-gradient(135deg,#E8F5E9,#C8E6C9)',border:'none'}}>
                <div className="stat-icon" style={{background:'#fff',color:'#1B5E20'}}>💰</div>
                <div className="stat-info">
                  <h3 style={{color:'#2E7D32'}}>Total Value</h3>
                  <div className="stat-value" style={{color:'#1B5E20'}}>{totalValue.toLocaleString()} RWF</div>
                </div>
              </div>
            </>
          )}
        </div>

        <div style={{display:'flex',gap:12,marginBottom:24,alignItems:'flex-end'}}>
          {isAdmin && (
            <div className="form-group" style={{margin:0,flex:1,maxWidth:340}}>
              <label className="form-label">Select Member</label>
              <select className="form-select" value={selectedMember} onChange={e => loadShares(e.target.value)}>
                <option value="">— Choose —</option>
                {members.map(m => <option key={m.id} value={m.id}>{m.firstName} {m.lastName}</option>)}
              </select>
            </div>
          )}
          <button className="btn btn-primary" onClick={() => {
            setBuyForm({ memberId: isAdmin ? '' : String(user.memberId), numberOfShares: 1 });
            setShowBuy(true);
          }}>+ Buy Shares</button>
        </div>

        <div className="data-table-wrap">
          <div className="data-table-header"><h2>Share History</h2></div>
          <table className="data-table">
            <thead><tr><th>Date</th><th>Shares</th><th>Price/Share</th><th>Total Value</th><th>Status</th></tr></thead>
            <tbody>
              {shares.map(s => (
                <tr key={s.id}>
                  <td>{s.purchaseDate}</td>
                  <td style={{fontWeight:600}}>{s.numberOfShares}</td>
                  <td>{Number(s.sharePriceAtPurchase).toLocaleString()} RWF</td>
                  <td style={{fontWeight:600,color:'#1B5E20'}}>{Number(s.totalValue).toLocaleString()} RWF</td>
                  <td><span className="badge badge-active">{s.status}</span></td>
                </tr>
              ))}
              {shares.length === 0 && <tr><td colSpan={5} style={{textAlign:'center',padding:40,color:'#9CA3AF'}}>{selectedMember ? 'No shares yet' : 'Select a member'}</td></tr>}
            </tbody>
          </table>
        </div>

        {showBuy && (<div className="modal-overlay" onClick={() => setShowBuy(false)}><div className="modal" onClick={e => e.stopPropagation()}>
          <div className="modal-header"><h2>Buy Shares</h2><button className="modal-close" onClick={() => setShowBuy(false)}>×</button></div>
          <form onSubmit={handleBuy}>
            {isAdmin && <div className="form-group"><label className="form-label">Member *</label>
              <select className="form-select" required value={buyForm.memberId} onChange={e => setBuyForm({...buyForm, memberId: e.target.value})}>
                <option value="">—</option>{members.map(m => <option key={m.id} value={m.id}>{m.firstName} {m.lastName}</option>)}
              </select></div>}
            <div className="form-group">
              <label className="form-label">Number of Shares *</label>
              <input className="form-input" type="number" min="1" required value={buyForm.numberOfShares}
                onChange={e => setBuyForm({...buyForm, numberOfShares: e.target.value})} />
              <p style={{fontSize:13,color:'#6B7280',marginTop:6}}>
                Cost: <strong>{(Number(buyForm.numberOfShares || 0) * Number(config.sharePrice || 0)).toLocaleString()} RWF</strong>
              </p>
            </div>
            <div style={{display:'flex',gap:12,justifyContent:'flex-end',marginTop:16}}>
              <button type="button" className="btn btn-outline" onClick={() => setShowBuy(false)}>Cancel</button>
              <button type="submit" className="btn btn-primary">Purchase</button></div>
          </form></div></div>)}
      </div>
    </>
  );
}
