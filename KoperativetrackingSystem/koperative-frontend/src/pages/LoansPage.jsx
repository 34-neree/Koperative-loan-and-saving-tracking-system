import { useState, useEffect } from 'react';
import TopBar from '../components/TopBar';
import { loansAPI, membersAPI } from '../api';
import { useAuth } from '../context/AuthContext';

export default function LoansPage() {
  const { user, role } = useAuth();
  const isAdmin = role === 'ADMIN' || role === 'TREASURER';
  const [loans, setLoans] = useState([]);
  const [members, setMembers] = useState([]);
  const [showApply, setShowApply] = useState(false);
  const [loading, setLoading] = useState(true);
  const [form, setForm] = useState({ amountRequested:'', interestRate:10, termMonths:12, purpose:'', guarantor1Id:'', guarantor2Id:'' });

  useEffect(() => {
    const loadData = async () => {
      try {
        if (isAdmin) {
          const [loansRes, membersRes] = await Promise.all([
            loansAPI.getPending(),
            membersAPI.getAll()
          ]);
          setLoans(loansRes.data);
          setMembers(membersRes.data);
        } else {
          // MEMBER role — show only their own loans
          if (user?.memberId) {
            const res = await loansAPI.getByMember(user.memberId);
            setLoans(res.data);
          }
          const membersRes = await membersAPI.getAll();
          setMembers(membersRes.data);
        }
      } catch {}
      setLoading(false);
    };
    loadData();
  }, []);

  const reload = async () => {
    try {
      if (isAdmin) {
        const r = await loansAPI.getPending();
        setLoans(r.data);
      } else if (user?.memberId) {
        const r = await loansAPI.getByMember(user.memberId);
        setLoans(r.data);
      }
    } catch {}
  };

  const handleApply = async(e) => {
    e.preventDefault();
    try {
      const data = { ...form, memberId: user.memberId };
      await loansAPI.apply(data);
      setShowApply(false);
      reload();
    } catch(err) { alert(err.response?.data?.message||'Error applying for loan'); }
  };

  const action = async(id, fn, label) => {
    if(!confirm(`${label} this loan?`)) return;
    try { await fn(id); reload(); } catch(err) { alert(err.response?.data?.message||'Error'); }
  };

  const getMemberName = (id) => { const m=members.find(x=>x.id==id); return m?`${m.firstName} ${m.lastName}`:`#${id}`; };
  const statusClass = (s) => `badge badge-${(s||'').toLowerCase()}`;

  return (
    <>
      <TopBar title="Loan Management" />
      <div className="page-content">
        <div style={{display:'flex',justifyContent:'space-between',alignItems:'center',marginBottom:24}}>
          <h2 style={{fontSize:18,fontWeight:700}}>{isAdmin ? 'All Loans' : 'My Loans'} ({loans.length})</h2>
          <button className="btn btn-primary" onClick={()=>{setForm({amountRequested:'',interestRate:10,termMonths:12,purpose:'',guarantor1Id:'',guarantor2Id:''});setShowApply(true);}}>
            + Apply for Loan
          </button>
        </div>

        <div className="data-table-wrap">
          <table className="data-table">
            <thead><tr>
              {isAdmin && <th>Member</th>}
              <th>Requested</th><th>Approved</th><th>Interest</th><th>Term</th><th>Purpose</th><th>Status</th>
              {isAdmin && <th>Actions</th>}
            </tr></thead>
            <tbody>
              {loans.map(l=>(
                <tr key={l.id}>
                  {isAdmin && <td style={{fontWeight:600}}>{getMemberName(l.memberId || l.member?.id)}</td>}
                  <td>{Number(l.amountRequested||0).toLocaleString()} RWF</td>
                  <td>{l.amountApproved?Number(l.amountApproved).toLocaleString()+' RWF':'—'}</td>
                  <td>{l.interestRate}%</td>
                  <td>{l.termMonths || 12} months</td>
                  <td style={{maxWidth:150,overflow:'hidden',textOverflow:'ellipsis',whiteSpace:'nowrap'}}>{l.purpose||'—'}</td>
                  <td><span className={statusClass(l.status)}>{l.status}</span></td>
                  {isAdmin && <td>
                    <div style={{display:'flex',gap:4,flexWrap:'wrap'}}>
                      {l.status==='APPLIED'&&<button className="btn btn-sm btn-outline" onClick={()=>action(l.id,loansAPI.review,'Review')}>Review</button>}
                      {l.status==='UNDER_REVIEW'&&<>
                        <button className="btn btn-sm" style={{background:'#E8F5E9',color:'#2E7D32'}} onClick={()=>{const a=prompt('Approved amount:',l.amountRequested);if(a)loansAPI.approve(l.id,a).then(reload).catch(e=>alert(e.response?.data?.message||'Error'));}}>Approve</button>
                        <button className="btn btn-sm btn-danger" onClick={()=>action(l.id,loansAPI.reject,'Reject')}>Reject</button>
                      </>}
                      {l.status==='APPROVED'&&<button className="btn btn-sm btn-primary" onClick={()=>action(l.id,loansAPI.disburse,'Disburse')}>Disburse</button>}
                    </div>
                  </td>}
                </tr>
              ))}
              {loans.length===0&&<tr><td colSpan={isAdmin?8:6} style={{textAlign:'center',padding:40,color:'#9CA3AF'}}>No loans found</td></tr>}
            </tbody>
          </table>
        </div>

        {showApply&&(<div className="modal-overlay" onClick={()=>setShowApply(false)}><div className="modal" onClick={e=>e.stopPropagation()}>
          <div className="modal-header"><h2>Apply for Loan</h2><button className="modal-close" onClick={()=>setShowApply(false)}>×</button></div>
          <p style={{color:'#6B7280',fontSize:14,marginBottom:20}}>Applying as: <strong>{user?.fullName || user?.username}</strong></p>
          <form onSubmit={handleApply}>
            <div className="form-row">
              <div className="form-group"><label className="form-label">Amount (RWF) *</label>
                <input className="form-input" type="number" min="1" required value={form.amountRequested} onChange={e=>setForm({...form,amountRequested:e.target.value})}/></div>
              <div className="form-group"><label className="form-label">Interest Rate (%)</label>
                <input className="form-input" type="number" min="0" step="0.5" value={form.interestRate} onChange={e=>setForm({...form,interestRate:e.target.value})}/></div>
            </div>
            <div className="form-group"><label className="form-label">Term (months)</label>
              <input className="form-input" type="number" min="1" max="60" value={form.termMonths} onChange={e=>setForm({...form,termMonths:e.target.value})}/></div>
            <div className="form-group"><label className="form-label">Purpose</label>
              <textarea className="form-textarea" rows={2} value={form.purpose} onChange={e=>setForm({...form,purpose:e.target.value})}/></div>
            <div className="form-row">
              <div className="form-group"><label className="form-label">Guarantor 1</label>
                <select className="form-select" value={form.guarantor1Id} onChange={e=>setForm({...form,guarantor1Id:e.target.value})}>
                  <option value="">— Optional —</option>{members.filter(m=>m.id!=user?.memberId).map(m=><option key={m.id} value={m.id}>{m.firstName} {m.lastName}</option>)}
                </select></div>
              <div className="form-group"><label className="form-label">Guarantor 2</label>
                <select className="form-select" value={form.guarantor2Id} onChange={e=>setForm({...form,guarantor2Id:e.target.value})}>
                  <option value="">— Optional —</option>{members.filter(m=>m.id!=user?.memberId).map(m=><option key={m.id} value={m.id}>{m.firstName} {m.lastName}</option>)}
                </select></div>
            </div>
            <div style={{display:'flex',gap:12,justifyContent:'flex-end',marginTop:16}}>
              <button type="button" className="btn btn-outline" onClick={()=>setShowApply(false)}>Cancel</button>
              <button type="submit" className="btn btn-primary">Submit Application</button></div>
          </form></div></div>)}
      </div>
    </>
  );
}
