import { useState, useEffect } from 'react';
import TopBar from '../components/TopBar';
import { savingsAPI, membersAPI } from '../api';
import { useAuth } from '../context/AuthContext';

export default function SavingsPage() {
  const { user, role } = useAuth();
  const isAdmin = role === 'ADMIN' || role === 'TREASURER';
  const [members, setMembers] = useState([]);
  const [selectedMember, setSelectedMember] = useState('');
  const [savings, setSavings] = useState([]);
  const [balance, setBalance] = useState(0);
  const [showRecord, setShowRecord] = useState(false);
  const [form, setForm] = useState({ memberId:'', amount:'', transactionType:'DEPOSIT', notes:'' });

  useEffect(() => {
    if (isAdmin) {
      membersAPI.getAll().then(r => setMembers(r.data)).catch(() => {});
    } else if (user?.memberId) {
      // Auto-load for MEMBER
      setSelectedMember(String(user.memberId));
      loadMemberSavings(user.memberId);
    }
  }, []);

  const loadMemberSavings = async(mid) => {
    setSelectedMember(mid);
    if(!mid) { setSavings([]); setBalance(0); return; }
    try {
      const [sRes, bRes] = await Promise.all([savingsAPI.getByMember(mid), savingsAPI.getBalance(mid)]);
      setSavings(sRes.data); setBalance(bRes.data.balance);
    } catch { setSavings([]); setBalance(0); }
  };

  const handleRecord = async(e) => {
    e.preventDefault();
    try {
      const memberId = isAdmin ? form.memberId : user.memberId;
      await savingsAPI.record({ ...form, memberId });
      setShowRecord(false);
      loadMemberSavings(memberId);
    } catch(err) { alert(err.response?.data?.message||'Error'); }
  };

  return (
    <>
      <TopBar title="Savings" />
      <div className="page-content">
        <div style={{display:'flex',gap:12,marginBottom:24,alignItems:'flex-end',flexWrap:'wrap'}}>
          {isAdmin ? (
            <div className="form-group" style={{margin:0,flex:1,maxWidth:340}}>
              <label className="form-label">Select Member</label>
              <select className="form-select" value={selectedMember} onChange={e=>loadMemberSavings(e.target.value)}>
                <option value="">— Choose —</option>
                {members.map(m=><option key={m.id} value={m.id}>{m.firstName} {m.lastName}</option>)}
              </select>
            </div>
          ) : (
            <div className="stat-card" style={{margin:0,flex:1,maxWidth:340}}>
              <div className="stat-info"><h3>My Savings Account</h3></div>
            </div>
          )}
          {selectedMember && (
            <div className="stat-card" style={{margin:0,background:'linear-gradient(135deg,#E8F5E9,#C8E6C9)',border:'none'}}>
              <div className="stat-info">
                <h3 style={{color:'#2E7D32'}}>Balance</h3>
                <div className="stat-value" style={{color:'#1B5E20'}}>{Number(balance).toLocaleString()} RWF</div>
              </div>
            </div>
          )}
          <button className="btn btn-primary" onClick={()=>{
            setForm({ memberId: isAdmin ? '' : String(user.memberId), amount:'', transactionType:'DEPOSIT', notes:'' });
            setShowRecord(true);
          }}>+ Record Transaction</button>
        </div>

        <div className="data-table-wrap">
          <div className="data-table-header"><h2>Transaction History</h2></div>
          <table className="data-table">
            <thead><tr><th>Date</th><th>Type</th><th>Amount (RWF)</th><th>Recorded By</th><th>Notes</th></tr></thead>
            <tbody>
              {savings.map(s=>(
                <tr key={s.id}>
                  <td>{new Date(s.transactionDate).toLocaleDateString()}</td>
                  <td><span className={`badge badge-${s.transactionType==='DEPOSIT'?'active':'rejected'}`}>{s.transactionType}</span></td>
                  <td style={{fontWeight:600,color:s.transactionType==='DEPOSIT'?'#2E7D32':'#C62828'}}>
                    {s.transactionType==='DEPOSIT'?'+':'-'}{Number(s.amount).toLocaleString()}
                  </td>
                  <td>{s.recordedBy||'—'}</td>
                  <td>{s.notes||'—'}</td>
                </tr>
              ))}
              {savings.length===0&&<tr><td colSpan={5} style={{textAlign:'center',padding:40,color:'#9CA3AF'}}>{selectedMember?'No transactions yet':'Select a member'}</td></tr>}
            </tbody>
          </table>
        </div>

        {showRecord&&(<div className="modal-overlay" onClick={()=>setShowRecord(false)}><div className="modal" onClick={e=>e.stopPropagation()}>
          <div className="modal-header"><h2>Record Savings</h2><button className="modal-close" onClick={()=>setShowRecord(false)}>×</button></div>
          <form onSubmit={handleRecord}>
            {isAdmin && <div className="form-group"><label className="form-label">Member *</label>
              <select className="form-select" required value={form.memberId} onChange={e=>setForm({...form,memberId:e.target.value})}>
                <option value="">—</option>{members.map(m=><option key={m.id} value={m.id}>{m.firstName} {m.lastName}</option>)}
              </select></div>}
            <div className="form-row">
              <div className="form-group"><label className="form-label">Type</label>
                <select className="form-select" value={form.transactionType} onChange={e=>setForm({...form,transactionType:e.target.value})}>
                  <option value="DEPOSIT">Deposit</option><option value="WITHDRAWAL">Withdrawal</option></select></div>
              <div className="form-group"><label className="form-label">Amount (RWF) *</label>
                <input className="form-input" type="number" min="1" required value={form.amount} onChange={e=>setForm({...form,amount:e.target.value})}/></div>
            </div>
            <div className="form-group"><label className="form-label">Notes</label>
              <textarea className="form-textarea" rows={2} value={form.notes} onChange={e=>setForm({...form,notes:e.target.value})}/></div>
            <div style={{display:'flex',gap:12,justifyContent:'flex-end',marginTop:16}}>
              <button type="button" className="btn btn-outline" onClick={()=>setShowRecord(false)}>Cancel</button>
              <button type="submit" className="btn btn-primary">Save</button></div>
          </form></div></div>)}
      </div>
    </>
  );
}
