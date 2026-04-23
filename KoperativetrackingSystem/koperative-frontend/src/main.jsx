import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import Layout from './components/Layout';
import ProtectedRoute from './components/ProtectedRoute';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import DashboardPage from './pages/dashboard/DashboardPage';
import MembersPage from './pages/MembersPage';
import SharesPage from './pages/SharesPage';
import SavingsPage from './pages/SavingsPage';
import LoansPage from './pages/LoansPage';
import RepaymentsPage from './pages/RepaymentsPage';
import MeetingsPage from './pages/MeetingsPage';
import FinesPage from './pages/FinesPage';
import TransactionsPage from './pages/TransactionsPage';
import NotificationsPage from './pages/NotificationsPage';
import SettingsPage from './pages/SettingsPage';
import './index.css';

ReactDOM.createRoot(document.getElementById('app')).render(
  <React.StrictMode>
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route element={<ProtectedRoute><Layout /></ProtectedRoute>}>
            <Route index element={<DashboardPage />} />
            <Route path="members" element={<ProtectedRoute roles={['ADMIN','SECRETARY']}><MembersPage /></ProtectedRoute>} />
            <Route path="shares" element={<ProtectedRoute roles={['ADMIN','TREASURER','MEMBER']}><SharesPage /></ProtectedRoute>} />
            <Route path="savings" element={<ProtectedRoute roles={['ADMIN','TREASURER','MEMBER']}><SavingsPage /></ProtectedRoute>} />
            <Route path="loans" element={<ProtectedRoute roles={['ADMIN','TREASURER','MEMBER']}><LoansPage /></ProtectedRoute>} />
            <Route path="repayments" element={<ProtectedRoute roles={['ADMIN','TREASURER']}><RepaymentsPage /></ProtectedRoute>} />
            <Route path="meetings" element={<ProtectedRoute roles={['ADMIN','SECRETARY']}><MeetingsPage /></ProtectedRoute>} />
            <Route path="fines" element={<ProtectedRoute roles={['ADMIN','TREASURER']}><FinesPage /></ProtectedRoute>} />
            <Route path="transactions" element={<ProtectedRoute roles={['ADMIN','TREASURER']}><TransactionsPage /></ProtectedRoute>} />
            <Route path="notifications" element={<ProtectedRoute roles={['ADMIN']}><NotificationsPage /></ProtectedRoute>} />
            <Route path="settings" element={<ProtectedRoute roles={['ADMIN']}><SettingsPage /></ProtectedRoute>} />
          </Route>
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  </React.StrictMode>
);
