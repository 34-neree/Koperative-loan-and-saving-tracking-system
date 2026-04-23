import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { authAPI } from '../api';

export default function RegisterPage() {
  const [form, setForm] = useState({
    firstName: '', lastName: '', nationalId: '', phone: '', email: '',
    district: '', sector: '', cell: '', username: '', password: '', confirmPassword: ''
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { setUserFromResponse } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (form.password !== form.confirmPassword) {
      setError('Passwords do not match');
      return;
    }
    if (form.password.length < 6) {
      setError('Password must be at least 6 characters');
      return;
    }

    setLoading(true);
    try {
      const { confirmPassword, ...data } = form;
      const res = await authAPI.register(data);
      setUserFromResponse(res.data);
      navigate('/');
    } catch (err) {
      setError(err.response?.data?.message || 'Registration failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const set = (field) => (e) => setForm({ ...form, [field]: e.target.value });

  return (
    <div className="login-page">
      <div className="login-card" style={{ width: 520, maxHeight: '90vh', overflowY: 'auto' }}>
        <div className="brand-section">
          <div className="brand-logo">K</div>
          <h1>Create Account</h1>
          <p>Join the Koperative Tracking System</p>
        </div>

        {error && <div className="login-error">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-row">
            <div className="form-group">
              <label className="form-label">First Name *</label>
              <input className="form-input" required value={form.firstName} onChange={set('firstName')} placeholder="e.g. Jean" />
            </div>
            <div className="form-group">
              <label className="form-label">Last Name *</label>
              <input className="form-input" required value={form.lastName} onChange={set('lastName')} placeholder="e.g. Mugabo" />
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label className="form-label">National ID *</label>
              <input className="form-input" required value={form.nationalId} onChange={set('nationalId')} placeholder="16 digits" />
            </div>
            <div className="form-group">
              <label className="form-label">Phone *</label>
              <input className="form-input" required value={form.phone} onChange={set('phone')} placeholder="+250 7XX XXX XXX" />
            </div>
          </div>

          <div className="form-group">
            <label className="form-label">Email</label>
            <input className="form-input" type="email" value={form.email} onChange={set('email')} placeholder="Optional" />
          </div>

          <div className="form-row">
            <div className="form-group">
              <label className="form-label">District</label>
              <input className="form-input" value={form.district} onChange={set('district')} placeholder="e.g. Kigali" />
            </div>
            <div className="form-group">
              <label className="form-label">Sector</label>
              <input className="form-input" value={form.sector} onChange={set('sector')} placeholder="e.g. Nyarugenge" />
            </div>
          </div>

          <div className="form-group">
            <label className="form-label">Cell</label>
            <input className="form-input" value={form.cell} onChange={set('cell')} />
          </div>

          <hr style={{ border: 'none', borderTop: '1px solid #E5E7EB', margin: '20px 0' }} />

          <div className="form-group">
            <label className="form-label">Username *</label>
            <input className="form-input" required minLength={3} value={form.username} onChange={set('username')} placeholder="Choose a username" />
          </div>

          <div className="form-row">
            <div className="form-group">
              <label className="form-label">Password *</label>
              <input className="form-input" type="password" required minLength={6} value={form.password} onChange={set('password')} placeholder="Min 6 characters" />
            </div>
            <div className="form-group">
              <label className="form-label">Confirm Password *</label>
              <input className="form-input" type="password" required value={form.confirmPassword} onChange={set('confirmPassword')} placeholder="Re-enter password" />
            </div>
          </div>

          <button type="submit" className="btn btn-primary login-btn" disabled={loading} style={{ marginTop: 8 }}>
            {loading ? 'Creating Account...' : 'Create Account'}
          </button>
        </form>

        <p style={{ textAlign: 'center', marginTop: 20, fontSize: 14, color: '#6B7280' }}>
          Already have an account?{' '}
          <Link to="/login" style={{ color: '#1B5E20', fontWeight: 600, textDecoration: 'none' }}>
            Sign In
          </Link>
        </p>
      </div>
    </div>
  );
}
