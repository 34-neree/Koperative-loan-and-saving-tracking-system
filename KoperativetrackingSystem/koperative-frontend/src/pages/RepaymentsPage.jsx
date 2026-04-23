import { useState, useEffect } from 'react';
import TopBar from '../components/TopBar';
import { loansAPI } from '../api';

export default function RepaymentsPage() {
  const [overdue, setOverdue] = useState([]);
  const [schedule, setSchedule] = useState([]);
  const [loanId, setLoanId] = useState('');
  const [showPay, setShowPay] = useState(null);
  const [payForm, setPayForm] = useState({ amountPaid:'', paidDate:new Date().toISOString().split('T')[0] });

  useEffect(() => { loansAPI.getOverdue().then(r=>setOverdue(r.data)).catch(()=>{}); }, []);

  const loadSchedule = async() => {
    if(!loanId) return;
    try { const r=await loansAPI.getSchedule(loanId); setSchedule(r.data); }
    catch { setSchedule([]); alert('Loan not found'); }
  };

  const handlePay = async(e) => {
    e.preventDefault();
    try { await loansAPI.repay(showPay.loanId||loanId, payForm); setShowPay(null); if(loanId) loadSchedule(); loansAPI.getOverdue().then(r=>setOverdue(r.data)); }
    catch(err) { alert(err.response?.data?.message||'Error'); }
  };

  const statusClass = (s) => `badge badge-${(s||'').toLowerCase()}`;

  return (
    <>
      <TopBar title="Loan Repayments" />
      <div className="page-content">
        {/* Overdue Alert */}
        {overdue.length>0&&(
          <div style={{background:'#FFF3E0',border:'1px solid #FFE0B2',borderRadius:12,padding:'16px 20px',marginBottom:24}}>
            <h3 style={{fontSize:14,fontWeight:700,color:'#E65100',marginBottom:8}}>⚠️ {overdue.length} Overdue Repayment(s)</h3>
            {overdue.slice(0,5).map(o=>(
              <div key={o.id} style={{fontSize:13,color:'#BF360C',marginBottom:4}}>
                Loan #{o.loanId} — {Number(o.amountDue||0).toLocaleString()} RWF due on {o.dueDate}
                <button className="btn btn-sm" style={{marginLeft:8,background:'#E65100',color:'white',padding:'2px 8px',fontSize:11}} onClick={()=>{setShowPay(o);setPayForm({amountPaid:o.amountDue,paidDate:new Date().toISOString().split('T')[0]});}}>Pay Now</button>
              </div>
            ))}
          </div>
        )}

        {/* Schedule Lookup */}
        <div style={{display:'flex',gap:12,marginBottom:24,alignItems:'flex-end'}}>
          <div className="form-group" style={{margin:0}}>
            <label className="form-label">Loan ID</label>
            <input className="form-input" style={{width:200}} placeholder="Enter loan ID" value={loanId} onChange={e=>setLoanId(e.target.value)}/>
          </div>
          <button className="btn btn-primary" onClick={loadSchedule}>View Schedule</button>
        </div>

        <div className="data-table-wrap">
          <div className="data-table-header"><h2>Repayment Schedule</h2></div>
          <table className="data-table">
            <thead><tr><th>Due Date</th><th>Amount Due</th><th>Amount Paid</th><th>Paid Date</th><th>Status</th><th>Action</th></tr></thead>
            <tbody>
              {schedule.map(s=>(
                <tr key={s.id}>
                  <td>{s.dueDate}</td>
                  <td style={{fontWeight:600}}>{Number(s.amountDue||0).toLocaleString()} RWF</td>
                  <td>{s.amountPaid?Number(s.amountPaid).toLocaleString()+' RWF':'—'}</td>
                  <td>{s.paidDate||'—'}</td>
                  <td><span className={statusClass(s.status)}>{s.status}</span></td>
                  <td>{(s.status==='PENDING'||s.status==='OVERDUE'||s.status==='PARTIAL')&&(
                    <button className="btn btn-sm btn-primary" onClick={()=>{setShowPay(s);setPayForm({amountPaid:s.amountDue-(s.amountPaid||0),paidDate:new Date().toISOString().split('T')[0]});}}>Pay</button>
                  )}</td>
                </tr>
              ))}
              {schedule.length===0&&<tr><td colSpan={6} style={{textAlign:'center',padding:40,color:'#9CA3AF'}}>Enter a Loan ID to view schedule</td></tr>}
            </tbody>
          </table>
        </div>

        {showPay&&(<div className="modal-overlay" onClick={()=>setShowPay(null)}><div className="modal" onClick={e=>e.stopPropagation()}>
          <div className="modal-header"><h2>Record Payment</h2><button className="modal-close" onClick={()=>setShowPay(null)}>×</button></div>
          <form onSubmit={handlePay}>
            <div className="form-row">
              <div className="form-group"><label className="form-label">Amount (RWF)</label>
                <input className="form-input" type="number" min="1" required value={payForm.amountPaid} onChange={e=>setPayForm({...payForm,amountPaid:e.target.value})}/></div>
              <div className="form-group"><label className="form-label">Payment Date</label>
                <input className="form-input" type="date" required value={payForm.paidDate} onChange={e=>setPayForm({...payForm,paidDate:e.target.value})}/></div>
            </div>
            <div style={{display:'flex',gap:12,justifyContent:'flex-end',marginTop:16}}>
              <button type="button" className="btn btn-outline" onClick={()=>setShowPay(null)}>Cancel</button>
              <button type="submit" className="btn btn-primary">Record Payment</button></div>
          </form></div></div>)}
      </div>
    </>
  );
}
