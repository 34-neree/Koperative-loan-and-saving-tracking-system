import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: { 'Content-Type': 'application/json' },
});

// Attach JWT token to every request
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Handle 401 responses
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Auth
export const authAPI = {
  login: (data) => api.post('/auth/login', data),
  register: (data) => api.post('/auth/register', data),
  me: () => api.get('/auth/me'),
};

// Members
export const membersAPI = {
  getAll: (search) => api.get('/members', { params: { search } }),
  create: (data) => api.post('/members', data),
  update: (id, data) => api.put(`/members/${id}`, data),
  getProfile: (id) => api.get(`/members/${id}/profile`),
  updateStatus: (id, status) => api.put(`/members/${id}/status`, { status }),
};

// Savings
export const savingsAPI = {
  record: (data) => api.post('/savings', data),
  getByMember: (memberId) => api.get(`/savings/member/${memberId}`),
  getBalance: (memberId) => api.get(`/savings/member/${memberId}/balance`),
};

// Shares
export const sharesAPI = {
  getConfig: () => api.get('/shares/config'),
  updateConfig: (data) => api.put('/shares/config', data),
  purchase: (data) => api.post('/shares', data),
  getByMember: (memberId) => api.get(`/shares/member/${memberId}`),
  checkEligibility: (memberId) => api.get(`/shares/eligibility/${memberId}`),
};

// Loans
export const loansAPI = {
  apply: (data) => api.post('/loans/apply', data),
  review: (id) => api.put(`/loans/${id}/review`),
  approve: (id, amount) => api.put(`/loans/${id}/approve`, { amountApproved: amount }),
  reject: (id) => api.put(`/loans/${id}/reject`),
  disburse: (id) => api.put(`/loans/${id}/disburse`),
  getByMember: (memberId) => api.get(`/loans/member/${memberId}`),
  getPending: () => api.get('/loans/pending'),
  getSchedule: (loanId) => api.get(`/loans/${loanId}/schedule`),
  repay: (loanId, data) => api.post(`/loans/${loanId}/repay`, data),
  getOverdue: () => api.get('/loans/overdue'),
};

// Meetings
export const meetingsAPI = {
  create: (data) => api.post('/meetings', data),
  getAll: () => api.get('/meetings'),
  submitAttendance: (id, data) => api.post(`/meetings/${id}/attendance`, data),
  getAttendance: (id) => api.get(`/meetings/${id}/attendance`),
};

// Fines
export const finesAPI = {
  getByMember: (memberId) => api.get(`/fines/member/${memberId}`),
  create: (data) => api.post('/fines', data),
  pay: (id) => api.put(`/fines/${id}/pay`),
  getUnpaid: () => api.get('/fines/unpaid'),
};

// Transactions
export const transactionsAPI = {
  create: (data) => api.post('/transactions', data),
  getAll: (params) => api.get('/transactions', { params }),
};

// Notifications
export const notificationsAPI = {
  send: (data) => api.post('/notifications/send', data),
  getByMember: (memberId) => api.get(`/notifications/member/${memberId}`),
};

// Dashboard
export const dashboardAPI = {
  getStats: () => api.get('/dashboard'),
  getMonthlyChart: () => api.get('/dashboard/monthly-chart'),
};

export default api;
