import { useAuth } from '../context/AuthContext';

export default function TopBar({ title }) {
  const { user, logout } = useAuth();

  return (
    <header className="topbar">
      <h1>{title}</h1>
      <div className="topbar-right">
        <div className="user-badge">
          <div className="user-avatar">
            {user?.fullName?.charAt(0) || 'U'}
          </div>
          <span>{user?.fullName || user?.username}</span>
          <span style={{ opacity: 0.6, fontSize: 11 }}>({user?.role})</span>
        </div>
        <button className="btn-logout" onClick={logout}>Logout</button>
      </div>
    </header>
  );
}
