import { NavLink, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const navItems = [
  { section: 'Overview', items: [
    { path: '/', label: 'Dashboard', icon: '📊', roles: ['ADMIN', 'TREASURER', 'SECRETARY', 'MEMBER'] },
  ]},
  { section: 'Management', items: [
    { path: '/members', label: 'Members', icon: '👥', roles: ['ADMIN', 'SECRETARY'] },
    { path: '/shares', label: 'Share Capital', icon: '📈', roles: ['ADMIN', 'TREASURER', 'MEMBER'] },
    { path: '/savings', label: 'Savings', icon: '💰', roles: ['ADMIN', 'TREASURER', 'MEMBER'] },
  ]},
  { section: 'Loans', items: [
    { path: '/loans', label: 'Loan Management', icon: '🏦', roles: ['ADMIN', 'TREASURER', 'MEMBER'] },
    { path: '/repayments', label: 'Repayments', icon: '📋', roles: ['ADMIN', 'TREASURER'] },
  ]},
  { section: 'Operations', items: [
    { path: '/meetings', label: 'Meetings', icon: '📅', roles: ['ADMIN', 'SECRETARY'] },
    { path: '/fines', label: 'Fines', icon: '⚠️', roles: ['ADMIN', 'TREASURER'] },
    { path: '/transactions', label: 'Income & Expenses', icon: '💳', roles: ['ADMIN', 'TREASURER'] },
  ]},
  { section: 'System', items: [
    { path: '/notifications', label: 'Notifications', icon: '🔔', roles: ['ADMIN'] },
    { path: '/settings', label: 'Settings', icon: '⚙️', roles: ['ADMIN'] },
  ]},
];

export default function Sidebar() {
  const { role } = useAuth();

  return (
    <aside className="sidebar">
      <div className="sidebar-brand">
        <div className="brand-icon">K</div>
        <div>
          <h2>Koperative</h2>
          <span>Tracking System</span>
        </div>
      </div>
      <nav className="sidebar-nav">
        {navItems.map((section) => {
          const visibleItems = section.items.filter(item => item.roles.includes(role));
          if (visibleItems.length === 0) return null;
          return (
            <div className="nav-section" key={section.section}>
              <div className="nav-section-title">{section.section}</div>
              {visibleItems.map((item) => (
                <NavLink
                  key={item.path}
                  to={item.path}
                  end={item.path === '/'}
                  className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}
                >
                  <span className="nav-icon">{item.icon}</span>
                  {item.label}
                </NavLink>
              ))}
            </div>
          );
        })}
      </nav>
    </aside>
  );
}
