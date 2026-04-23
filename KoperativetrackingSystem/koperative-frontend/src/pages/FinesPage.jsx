import { useState, useEffect } from 'react';
import TopBar from '../components/TopBar';
import { finesAPI, membersAPI } from '../api';

export default function FinesPage() {
  const [fines, setFines] = useState([]);
  const [members, setMembers] = useState([]);
  const [showCreate, setShowCreate] = useState(false);
  const [loading, setLoading] = useState(true);
  const [form, setForm] = useState({ memberId:'', reason:'', amount:'' });

  useEffect(() => {
    Promise.all([
      finesAPI.getUnpaid().then(r=>setFines(r.data)).catch(()=>setFines([])),
      membersAPI.getAll().then(r=>setMembers(r.data)).catch(()=>{})
    ]).finally(()=>setLoading(false));
  }, []);

  const reload = () => finesAPI.getUnpaid().then(r=>setFines(r.data));

  const handleCreate = async(e) => {
    e.preventDefault();
    try { await finesAPI.create(form); setShowCreate(false); reload(); }
    catch(err) { alert(err.response?.data?.message||'Error'); }
  };

  const handlePay = async(id) => {
    if(!confirm('Mark this fine as paid?')) return;
    try { await finesAPI.pay(id); reload(); }
    catch(err) { alert(err.response?.data?.message||'Error'); }
  };

  const getMemberName = (id) => { const m=members.find(x=>x.id==id); return m?`${m.firstName} ${m.lastName}`:`#${id}`; };
  const totalUnpaid = fines.filter(f=>f.status==='UNPAID').reduce((s,f)=>s+(f.amount||0),0);

  return (
    <>
      <TopBar title="Fines & Penalties" />
      <div className="page-content">
        <div className="stat-cards">
          <div className="stat-card"><div className="stat-icon red">⚠️</div><div className="stat-info"><h3>Unpaid Fines</h3><div className="stat-value">{fines.filter(f=>f.status==='UNPAID').length}</div></div></div>
          <div className="stat-card"><div className="stat-icon gold">💰</div><div className="stat-info"><h3>Total Unpaid Amount</h3><div className="stat-value">{totalUnpaid.toLocaleString()} RWF</div></div></div>
        </div>

        <div style={{display:'flex',justifyContent:'flex-end',marginBottom:16}}>
          <button className="btn btn-primary" onClick={()=>{setForm({memberId:'',reason:'',amount:''});setShowCreate(true);}}>+ Issue Fine</button>
        </div>

        <div className="data-table-wrap">
          <div className="data-table-header"><h2>Fines</h2></div>
          <table className="data-table">
            <thead><tr><th>Member</th><th>Reason</th><th>Amount</th><th>Issued</th><th>Status</th><th>Action</th></tr></thead>
            <tbody>
              {fines.map(f=>(
                <tr key={f.id}>
                  <td style={{fontWeight:600}}>{getMemberName(f.memberId)}</td>
                  <td>{f.reason}</td>
                  <td style={{fontWeight:700}}>{Number(f.amount||0).toLocaleString()} RWF</td>
                  <td>{f.issuedDate}</td>
                  <td><span className={`badge badge-${(f.status||'').toLowerCase()}`}>{f.status}</span></td>
                  <td>{f.status==='UNPAID'&&<button className="btn btn-sm" style={{background:'#E8F5E9',color:'#2E7D32'}} onClick={()=>handlePay(f.id)}>✓ Pay</button>}</td>
                </tr>
              ))}
              {fines.length===0&&<tr><td colSpan={6} style={{textAlign:'center',padding:40,color:'#9CA3AF'}}>No fines</td></tr>}
            </tbody>
          </table>
        </div>

        {showCreate&&(<div className="modal-overlay" onClick={()=>setShowCreate(false)}><div className="modal" onClick={e=>e.stopPropagation()}>
          <div className="modal-header"><h2>Issue Fine</h2><button className="modal-close" onClick={()=>setShowCreate(false)}>×</button></div>
          <form onSubmit={handleCreate}>
            <div className="form-group"><label className="form-label">Member</label>
              <select className="form-select" required value={form.memberId} onChange={e=>setForm({...form,memberId:e.target.value})}>
                <option value="">—</option>{members.map(m=><option key={m.id} value={m.id}>{m.firstName} {m.lastName}</option>)}
              </select></div>
            <div className="form-group"><label className="form-label">Reason</label>
              <input className="form-input" required value={form.reason} onChange={e=>setForm({...form,reason:e.target.value})}/></div>
            <div className="form-group"><label className="form-label">Amount (RWF)</label>
              <input className="form-input" type="number" min="1" required value={form.amount} onChange={e=>setForm({...form,amount:e.target.value})}/></div>
            <div style={{display:'flex',gap:12,justifyContent:'flex-end',marginTop:16}}>
              <button type="button" className="btn btn-outline" onClick={()=>setShowCreate(false)}>Cancel</button>
              <button type="submit" className="btn btn-primary">Issue</button></div>
          </form></div></div>)}
      </div>
    </>
  );
}
