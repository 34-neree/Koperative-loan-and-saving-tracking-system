import { useState, useEffect } from 'react';
import TopBar from '../components/TopBar';
import { notificationsAPI, membersAPI } from '../api';

export default function NotificationsPage() {
  const [members, setMembers] = useState([]);
  const [history, setHistory] = useState([]);
  const [selectedMember, setSelectedMember] = useState('');
  const [showSend, setShowSend] = useState(false);
  const [form, setForm] = useState({ memberId:'', phone:'', message:'', channel:'SMS' });

  useEffect(() => { membersAPI.getAll().then(r=>setMembers(r.data)).catch(()=>{}); }, []);

  const loadHistory = (mid) => {
    setSelectedMember(mid);
    if(!mid) { setHistory([]); return; }
    notificationsAPI.getByMember(mid).then(r=>setHistory(r.data)).catch(()=>setHistory([]));
  };

  const handleSend = async(e) => {
    e.preventDefault();
    try { await notificationsAPI.send(form); setShowSend(false); if(selectedMember) loadHistory(selectedMember); alert('Notification sent!'); }
    catch(err) { alert(err.response?.data?.message||'Error'); }
  };

  const statusClass = (s) => `badge badge-${(s||'').toLowerCase()}`;

  return (
    <>
      <TopBar title="SMS Notifications" />
      <div className="page-content">
        <div style={{display:'flex',gap:12,marginBottom:24,alignItems:'flex-end'}}>
          <div className="form-group" style={{margin:0,flex:1,maxWidth:340}}>
            <label className="form-label">Select Member</label>
            <select className="form-select" value={selectedMember} onChange={e=>loadHistory(e.target.value)}>
              <option value="">— Choose —</option>
              {members.map(m=><option key={m.id} value={m.id}>{m.firstName} {m.lastName}</option>)}
            </select>
          </div>
          <button className="btn btn-primary" onClick={()=>{setForm({memberId:'',phone:'',message:'',channel:'SMS'});setShowSend(true);}}>+ Send Notification</button>
        </div>

        <div className="data-table-wrap">
          <div className="data-table-header"><h2>Notification History</h2></div>
          <table className="data-table">
            <thead><tr><th>Date</th><th>Phone</th><th>Message</th><th>Channel</th><th>Status</th></tr></thead>
            <tbody>
              {history.map(n=>(
                <tr key={n.id}>
                  <td>{n.sentAt}</td>
                  <td>{n.phone}</td>
                  <td style={{maxWidth:300,overflow:'hidden',textOverflow:'ellipsis',whiteSpace:'nowrap'}}>{n.message}</td>
                  <td><span className="badge badge-info">{n.channel}</span></td>
                  <td><span className={statusClass(n.status)}>{n.status}</span></td>
                </tr>
              ))}
              {history.length===0&&<tr><td colSpan={5} style={{textAlign:'center',padding:40,color:'#9CA3AF'}}>{selectedMember?'No notifications':'Select a member'}</td></tr>}
            </tbody>
          </table>
        </div>

        {showSend&&(<div className="modal-overlay" onClick={()=>setShowSend(false)}><div className="modal" onClick={e=>e.stopPropagation()}>
          <div className="modal-header"><h2>Send Notification</h2><button className="modal-close" onClick={()=>setShowSend(false)}>×</button></div>
          <form onSubmit={handleSend}>
            <div className="form-group"><label className="form-label">Member</label>
              <select className="form-select" required value={form.memberId} onChange={e=>{
                setForm({...form,memberId:e.target.value});
                const m=members.find(x=>x.id==e.target.value);
                if(m) setForm(f=>({...f,phone:m.phone||''}));
              }}>
                <option value="">—</option>{members.map(m=><option key={m.id} value={m.id}>{m.firstName} {m.lastName}</option>)}
              </select></div>
            <div className="form-row">
              <div className="form-group"><label className="form-label">Phone</label>
                <input className="form-input" required value={form.phone} onChange={e=>setForm({...form,phone:e.target.value})}/></div>
              <div className="form-group"><label className="form-label">Channel</label>
                <select className="form-select" value={form.channel} onChange={e=>setForm({...form,channel:e.target.value})}>
                  <option value="SMS">SMS</option><option value="SYSTEM">System</option></select></div>
            </div>
            <div className="form-group"><label className="form-label">Message</label>
              <textarea className="form-textarea" rows={4} required value={form.message} onChange={e=>setForm({...form,message:e.target.value})}/></div>
            <div style={{display:'flex',gap:12,justifyContent:'flex-end',marginTop:16}}>
              <button type="button" className="btn btn-outline" onClick={()=>setShowSend(false)}>Cancel</button>
              <button type="submit" className="btn btn-primary">Send</button></div>
          </form></div></div>)}
      </div>
    </>
  );
}
