import { useState, useEffect } from 'react';
import TopBar from '../components/TopBar';
import { transactionsAPI } from '../api';

export default function TransactionsPage() {
  const [transactions, setTransactions] = useState([]);
  const [filter, setFilter] = useState({ type:'', month:'', year:'' });
  const [showCreate, setShowCreate] = useState(false);
  const [loading, setLoading] = useState(true);
  const [form, setForm] = useState({ type:'INCOME', category:'', amount:'', description:'', receiptNumber:'' });

  const load = () => {
    setLoading(true);
    transactionsAPI.getAll(filter).then(r=>setTransactions(r.data)).catch(()=>setTransactions([])).finally(()=>setLoading(false));
  };

  useEffect(()=>{ load(); }, [filter.type]);

  const handleCreate = async(e) => {
    e.preventDefault();
    try { await transactionsAPI.create(form); setShowCreate(false); load(); }
    catch(err) { alert(err.response?.data?.message||'Error'); }
  };

  const income = transactions.filter(t=>t.type==='INCOME').reduce((s,t)=>s+(t.amount||0),0);
  const expense = transactions.filter(t=>t.type==='EXPENSE').reduce((s,t)=>s+(t.amount||0),0);

  const categories = ['Interest Income','Membership Fees','Share Income','Rent','Salaries','Stationery','Other'];

  return (
    <>
      <TopBar title="Income & Expenses" />
      <div className="page-content">
        <div className="stat-cards">
          <div className="stat-card"><div className="stat-icon green">📥</div><div className="stat-info"><h3>Total Income</h3><div className="stat-value">{income.toLocaleString()} RWF</div></div></div>
          <div className="stat-card"><div className="stat-icon red">📤</div><div className="stat-info"><h3>Total Expenses</h3><div className="stat-value">{expense.toLocaleString()} RWF</div></div></div>
          <div className="stat-card"><div className="stat-icon blue">💎</div><div className="stat-info"><h3>Net Balance</h3><div className="stat-value" style={{color:income-expense>=0?'#2E7D32':'#C62828'}}>{(income-expense).toLocaleString()} RWF</div></div></div>
        </div>

        <div style={{display:'flex',gap:12,marginBottom:16,alignItems:'flex-end'}}>
          <div className="form-group" style={{margin:0}}>
            <label className="form-label">Type</label>
            <select className="form-select" style={{width:160}} value={filter.type} onChange={e=>setFilter({...filter,type:e.target.value})}>
              <option value="">All</option><option value="INCOME">Income</option><option value="EXPENSE">Expense</option>
            </select>
          </div>
          <button className="btn btn-outline" onClick={load}>🔄 Refresh</button>
          <div style={{flex:1}}/>
          <button className="btn btn-primary" onClick={()=>{setForm({type:'INCOME',category:'',amount:'',description:'',receiptNumber:''});setShowCreate(true);}}>+ Record Transaction</button>
        </div>

        <div className="data-table-wrap">
          <table className="data-table">
            <thead><tr><th>Date</th><th>Type</th><th>Category</th><th>Amount</th><th>Description</th><th>Receipt #</th></tr></thead>
            <tbody>
              {transactions.map(t=>(
                <tr key={t.id}>
                  <td>{t.transactionDate}</td>
                  <td><span className={`badge ${t.type==='INCOME'?'badge-active':'badge-overdue'}`}>{t.type}</span></td>
                  <td>{t.category||'—'}</td>
                  <td style={{fontWeight:700,color:t.type==='INCOME'?'#2E7D32':'#C62828'}}>{Number(t.amount||0).toLocaleString()} RWF</td>
                  <td>{t.description||'—'}</td>
                  <td>{t.receiptNumber||'—'}</td>
                </tr>
              ))}
              {transactions.length===0&&<tr><td colSpan={6} style={{textAlign:'center',padding:40,color:'#9CA3AF'}}>No transactions</td></tr>}
            </tbody>
          </table>
        </div>

        {showCreate&&(<div className="modal-overlay" onClick={()=>setShowCreate(false)}><div className="modal" onClick={e=>e.stopPropagation()}>
          <div className="modal-header"><h2>Record Transaction</h2><button className="modal-close" onClick={()=>setShowCreate(false)}>×</button></div>
          <form onSubmit={handleCreate}>
            <div className="form-row">
              <div className="form-group"><label className="form-label">Type</label>
                <select className="form-select" value={form.type} onChange={e=>setForm({...form,type:e.target.value})}>
                  <option value="INCOME">Income</option><option value="EXPENSE">Expense</option></select></div>
              <div className="form-group"><label className="form-label">Category</label>
                <select className="form-select" value={form.category} onChange={e=>setForm({...form,category:e.target.value})}>
                  <option value="">—</option>{categories.map(c=><option key={c} value={c}>{c}</option>)}</select></div>
            </div>
            <div className="form-row">
              <div className="form-group"><label className="form-label">Amount (RWF)</label>
                <input className="form-input" type="number" min="1" required value={form.amount} onChange={e=>setForm({...form,amount:e.target.value})}/></div>
              <div className="form-group"><label className="form-label">Receipt #</label>
                <input className="form-input" value={form.receiptNumber} onChange={e=>setForm({...form,receiptNumber:e.target.value})}/></div>
            </div>
            <div className="form-group"><label className="form-label">Description</label>
              <textarea className="form-textarea" rows={2} value={form.description} onChange={e=>setForm({...form,description:e.target.value})}/></div>
            <div style={{display:'flex',gap:12,justifyContent:'flex-end',marginTop:16}}>
              <button type="button" className="btn btn-outline" onClick={()=>setShowCreate(false)}>Cancel</button>
              <button type="submit" className="btn btn-primary">Record</button></div>
          </form></div></div>)}
      </div>
    </>
  );
}
