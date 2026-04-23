import { useState, useEffect } from 'react';
import TopBar from '../components/TopBar';
import { membersAPI } from '../api';

export default function MembersPage() {
  const [members, setMembers] = useState([]);
  const [search, setSearch] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [editing, setEditing] = useState(null);
  const [loading, setLoading] = useState(true);
  const [form, setForm] = useState({
    firstName: '', lastName: '', nationalId: '', phone: '', email: '',
    cell: '', sector: '', district: '', nextOfKin: '', nextOfKinPhone: '',
  });

  const loadMembers = () => {
    setLoading(true);
    membersAPI.getAll(search)
      .then(res => setMembers(res.data))
      .catch(() => setMembers([]))
      .finally(() => setLoading(false));
  };

  useEffect(() => { loadMembers(); }, [search]);

  const openAdd = () => {
    setEditing(null);
    setForm({ firstName: '', lastName: '', nationalId: '', phone: '', email: '', cell: '', sector: '', district: '', nextOfKin: '', nextOfKinPhone: '' });
    setShowModal(true);
  };

  const openEdit = (m) => {
    setEditing(m);
    setForm({
      firstName: m.firstName || '', lastName: m.lastName || '', nationalId: m.nationalId || '',
      phone: m.phone || '', email: m.email || '', cell: m.cell || '', sector: m.sector || '',
      district: m.district || '', nextOfKin: m.nextOfKin || '', nextOfKinPhone: m.nextOfKinPhone || '',
    });
    setShowModal(true);
  };

  const handleSave = async (e) => {
    e.preventDefault();
    try {
      if (editing) {
        await membersAPI.update(editing.id, form);
      } else {
        await membersAPI.create(form);
      }
      setShowModal(false);
      loadMembers();
    } catch (err) {
      alert(err.response?.data?.message || 'Error saving member');
    }
  };

  const handleStatusChange = async (id, status) => {
    if (!confirm(`Change member status to ${status}?`)) return;
    try {
      await membersAPI.updateStatus(id, status);
      loadMembers();
    } catch (err) {
      alert('Error updating status');
    }
  };

  const statusClass = (s) => `badge badge-${(s || '').toLowerCase()}`;

  return (
    <>
      <TopBar title="Member Management" />
      <div className="page-content">
        <div className="data-table-wrap">
          <div className="data-table-header">
            <h2>Members ({members.length})</h2>
            <div style={{ display: 'flex', gap: 12 }}>
              <input
                className="form-input"
                placeholder="Search by name, ID, phone..."
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                style={{ width: 260 }}
              />
              <button className="btn btn-primary" onClick={openAdd}>+ Add Member</button>
            </div>
          </div>
          {loading ? (
            <p style={{ padding: 24 }}>Loading members...</p>
          ) : (
            <table className="data-table">
              <thead>
                <tr>
                  <th>Name</th><th>National ID</th><th>Phone</th><th>District</th>
                  <th>Sector</th><th>Status</th><th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {members.map((m) => (
                  <tr key={m.id}>
                    <td style={{ fontWeight: 600 }}>{m.firstName} {m.lastName}</td>
                    <td>{m.nationalId}</td>
                    <td>{m.phone}</td>
                    <td>{m.district}</td>
                    <td>{m.sector}</td>
                    <td><span className={statusClass(m.status)}>{m.status}</span></td>
                    <td>
                      <div style={{ display: 'flex', gap: 6 }}>
                        <button className="btn btn-outline btn-sm" onClick={() => openEdit(m)}>Edit</button>
                        {m.status === 'ACTIVE' && (
                          <button className="btn btn-sm" style={{ background: '#FFF3E0', color: '#E65100' }} onClick={() => handleStatusChange(m.id, 'SUSPENDED')}>Suspend</button>
                        )}
                        {m.status === 'SUSPENDED' && (
                          <>
                            <button className="btn btn-sm" style={{ background: '#E8F5E9', color: '#2E7D32' }} onClick={() => handleStatusChange(m.id, 'ACTIVE')}>Activate</button>
                            <button className="btn btn-sm btn-danger" onClick={() => handleStatusChange(m.id, 'EXITED')}>Exit</button>
                          </>
                        )}
                      </div>
                    </td>
                  </tr>
                ))}
                {members.length === 0 && (
                  <tr><td colSpan={7} style={{ textAlign: 'center', padding: 40, color: '#9CA3AF' }}>No members found</td></tr>
                )}
              </tbody>
            </table>
          )}
        </div>

        {showModal && (
          <div className="modal-overlay" onClick={() => setShowModal(false)}>
            <div className="modal" onClick={(e) => e.stopPropagation()}>
              <div className="modal-header">
                <h2>{editing ? 'Edit Member' : 'Add New Member'}</h2>
                <button className="modal-close" onClick={() => setShowModal(false)}>×</button>
              </div>
              <form onSubmit={handleSave}>
                <div className="form-row">
                  <div className="form-group">
                    <label className="form-label">First Name *</label>
                    <input className="form-input" required value={form.firstName} onChange={(e) => setForm({...form, firstName: e.target.value})} />
                  </div>
                  <div className="form-group">
                    <label className="form-label">Last Name *</label>
                    <input className="form-input" required value={form.lastName} onChange={(e) => setForm({...form, lastName: e.target.value})} />
                  </div>
                </div>
                <div className="form-row">
                  <div className="form-group">
                    <label className="form-label">National ID *</label>
                    <input className="form-input" required value={form.nationalId} onChange={(e) => setForm({...form, nationalId: e.target.value})} />
                  </div>
                  <div className="form-group">
                    <label className="form-label">Phone *</label>
                    <input className="form-input" required value={form.phone} onChange={(e) => setForm({...form, phone: e.target.value})} />
                  </div>
                </div>
                <div className="form-group">
                  <label className="form-label">Email</label>
                  <input className="form-input" type="email" value={form.email} onChange={(e) => setForm({...form, email: e.target.value})} />
                </div>
                <div className="form-row">
                  <div className="form-group">
                    <label className="form-label">Cell</label>
                    <input className="form-input" value={form.cell} onChange={(e) => setForm({...form, cell: e.target.value})} />
                  </div>
                  <div className="form-group">
                    <label className="form-label">Sector</label>
                    <input className="form-input" value={form.sector} onChange={(e) => setForm({...form, sector: e.target.value})} />
                  </div>
                </div>
                <div className="form-group">
                  <label className="form-label">District</label>
                  <input className="form-input" value={form.district} onChange={(e) => setForm({...form, district: e.target.value})} />
                </div>
                <div className="form-row">
                  <div className="form-group">
                    <label className="form-label">Next of Kin</label>
                    <input className="form-input" value={form.nextOfKin} onChange={(e) => setForm({...form, nextOfKin: e.target.value})} />
                  </div>
                  <div className="form-group">
                    <label className="form-label">Next of Kin Phone</label>
                    <input className="form-input" value={form.nextOfKinPhone} onChange={(e) => setForm({...form, nextOfKinPhone: e.target.value})} />
                  </div>
                </div>
                <div style={{ display: 'flex', gap: 12, justifyContent: 'flex-end', marginTop: 24 }}>
                  <button type="button" className="btn btn-outline" onClick={() => setShowModal(false)}>Cancel</button>
                  <button type="submit" className="btn btn-primary">{editing ? 'Update' : 'Add'} Member</button>
                </div>
              </form>
            </div>
          </div>
        )}
      </div>
    </>
  );
}
