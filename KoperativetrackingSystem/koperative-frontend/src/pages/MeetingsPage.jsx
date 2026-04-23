import { useState, useEffect } from 'react';
import TopBar from '../components/TopBar';
import { meetingsAPI, membersAPI } from '../api';

export default function MeetingsPage() {
  const [meetings, setMeetings] = useState([]);
  const [members, setMembers] = useState([]);
  const [showCreate, setShowCreate] = useState(false);
  const [showAttendance, setShowAttendance] = useState(null);
  const [attendance, setAttendance] = useState({});
  const [form, setForm] = useState({ title:'', meetingDate:'', location:'', agenda:'' });

  useEffect(() => {
    meetingsAPI.getAll().then(r=>setMeetings(r.data)).catch(()=>{});
    membersAPI.getAll().then(r=>setMembers(r.data)).catch(()=>{});
  }, []);

  const reload = () => meetingsAPI.getAll().then(r=>setMeetings(r.data));

  const handleCreate = async(e) => {
    e.preventDefault();
    try { await meetingsAPI.create(form); setShowCreate(false); reload(); }
    catch(err) { alert(err.response?.data?.message||'Error'); }
  };

  const openAttendance = async(meeting) => {
    setShowAttendance(meeting);
    const att = {};
    members.forEach(m => att[m.id] = true);
    try {
      const r = await meetingsAPI.getAttendance(meeting.id);
      if(r.data && r.data.length > 0) {
        r.data.forEach(a => { att[a.memberId] = a.attended; });
      }
    } catch {}
    setAttendance(att);
  };

  const submitAttendance = async() => {
    const list = Object.entries(attendance).map(([memberId, attended]) => ({ memberId: Number(memberId), attended }));
    try { await meetingsAPI.submitAttendance(showAttendance.id, list); setShowAttendance(null); alert('Attendance saved!'); }
    catch(err) { alert(err.response?.data?.message||'Error'); }
  };

  return (
    <>
      <TopBar title="Meetings & Attendance" />
      <div className="page-content">
        <div style={{display:'flex',justifyContent:'space-between',marginBottom:24}}>
          <h2 style={{fontSize:18,fontWeight:700}}>Meetings ({meetings.length})</h2>
          <button className="btn btn-primary" onClick={()=>{setForm({title:'',meetingDate:'',location:'',agenda:''});setShowCreate(true);}}>+ Create Meeting</button>
        </div>

        <div className="data-table-wrap">
          <table className="data-table">
            <thead><tr><th>Title</th><th>Date</th><th>Location</th><th>Agenda</th><th>Actions</th></tr></thead>
            <tbody>
              {meetings.map(m=>(
                <tr key={m.id}>
                  <td style={{fontWeight:600}}>{m.title}</td>
                  <td>{m.meetingDate}</td>
                  <td>{m.location||'—'}</td>
                  <td style={{maxWidth:200,overflow:'hidden',textOverflow:'ellipsis',whiteSpace:'nowrap'}}>{m.agenda||'—'}</td>
                  <td><button className="btn btn-sm btn-outline" onClick={()=>openAttendance(m)}>📋 Attendance</button></td>
                </tr>
              ))}
              {meetings.length===0&&<tr><td colSpan={5} style={{textAlign:'center',padding:40,color:'#9CA3AF'}}>No meetings yet</td></tr>}
            </tbody>
          </table>
        </div>

        {showCreate&&(<div className="modal-overlay" onClick={()=>setShowCreate(false)}><div className="modal" onClick={e=>e.stopPropagation()}>
          <div className="modal-header"><h2>Create Meeting</h2><button className="modal-close" onClick={()=>setShowCreate(false)}>×</button></div>
          <form onSubmit={handleCreate}>
            <div className="form-group"><label className="form-label">Title</label>
              <input className="form-input" required value={form.title} onChange={e=>setForm({...form,title:e.target.value})}/></div>
            <div className="form-row">
              <div className="form-group"><label className="form-label">Date</label>
                <input className="form-input" type="date" required value={form.meetingDate} onChange={e=>setForm({...form,meetingDate:e.target.value})}/></div>
              <div className="form-group"><label className="form-label">Location</label>
                <input className="form-input" value={form.location} onChange={e=>setForm({...form,location:e.target.value})}/></div>
            </div>
            <div className="form-group"><label className="form-label">Agenda</label>
              <textarea className="form-textarea" rows={3} value={form.agenda} onChange={e=>setForm({...form,agenda:e.target.value})}/></div>
            <div style={{display:'flex',gap:12,justifyContent:'flex-end',marginTop:16}}>
              <button type="button" className="btn btn-outline" onClick={()=>setShowCreate(false)}>Cancel</button>
              <button type="submit" className="btn btn-primary">Create</button></div>
          </form></div></div>)}

        {showAttendance&&(<div className="modal-overlay" onClick={()=>setShowAttendance(null)}><div className="modal" onClick={e=>e.stopPropagation()} style={{maxWidth:500}}>
          <div className="modal-header"><h2>Attendance — {showAttendance.title}</h2><button className="modal-close" onClick={()=>setShowAttendance(null)}>×</button></div>
          <div style={{maxHeight:400,overflowY:'auto'}}>
            {members.map(m=>(
              <div key={m.id} style={{display:'flex',alignItems:'center',justifyContent:'space-between',padding:'10px 0',borderBottom:'1px solid #F3F4F6'}}>
                <span style={{fontWeight:500}}>{m.firstName} {m.lastName}</span>
                <label style={{display:'flex',alignItems:'center',gap:8,cursor:'pointer'}}>
                  <input type="checkbox" checked={attendance[m.id]||false} onChange={e=>setAttendance({...attendance,[m.id]:e.target.checked})}/>
                  <span style={{fontSize:13,color:attendance[m.id]?'#2E7D32':'#C62828'}}>{attendance[m.id]?'Present':'Absent'}</span>
                </label>
              </div>
            ))}
          </div>
          <div style={{display:'flex',gap:12,justifyContent:'flex-end',marginTop:20}}>
            <button className="btn btn-outline" onClick={()=>setShowAttendance(null)}>Cancel</button>
            <button className="btn btn-primary" onClick={submitAttendance}>Save Attendance</button></div>
        </div></div>)}
      </div>
    </>
  );
}
