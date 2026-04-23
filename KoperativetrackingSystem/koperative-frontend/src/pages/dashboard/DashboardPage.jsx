import { useState, useEffect } from 'react';
import TopBar from '../../components/TopBar';
import { dashboardAPI } from '../../api';
import { useAuth } from '../../context/AuthContext';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, PieChart, Pie, Cell, Legend } from 'recharts';

const COLORS = ['#1B5E20', '#C62828', '#F9A825', '#1565C0'];

export default function DashboardPage() {
  const { user } = useAuth();
  const [stats, setStats] = useState(null);
  const [chartData, setChartData] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const months = ['JAN','FEB','MAR','APR','MAY','JUN','JUL','AUG','SEP','OCT','NOV','DEC'];
    const now = new Date();

    // Generate default 6-month labels
    const defaultChart = [];
    for (let i = 5; i >= 0; i--) {
      const d = new Date(now.getFullYear(), now.getMonth() - i, 1);
      defaultChart.push({ month: months[d.getMonth()], savings: 0, loans: 0 });
    }

    Promise.all([
      dashboardAPI.getStats().then(r => r.data).catch(() => ({
        totalMembers: 0, totalSavings: 0, totalLoansDisbursed: 0,
        pendingLoanApprovals: 0, overdueRepayments: 0, totalUnpaidFines: 0,
        totalIncome: 0, totalExpenses: 0, netBalance: 0
      })),
      dashboardAPI.getMonthlyChart().then(r => r.data).catch(() => [])
    ]).then(([statsData, chart]) => {
      setStats(statsData);
      if (chart && chart.length > 0) {
        setChartData(chart.map(c => ({
          month: c.month, savings: Number(c.savings) || 0, loans: Number(c.loans) || 0
        })));
      } else {
        setChartData(defaultChart);
      }
    }).finally(() => setLoading(false));
  }, []);

  const pieData = stats ? [
    { name: 'Income', value: Number(stats.totalIncome) || 0 },
    { name: 'Expenses', value: Number(stats.totalExpenses) || 0 },
    { name: 'Unpaid Fines', value: Number(stats.totalUnpaidFines) || 0 },
    { name: 'Net Balance', value: Math.max(Number(stats.netBalance) || 0, 0) },
  ].filter(d => d.value > 0) : [];

  // Fallback pie data when no financial transactions exist
  const displayPie = pieData.length > 0 ? pieData : [
    { name: 'No Data', value: 1 }
  ];
  const pieColors = pieData.length > 0 ? COLORS : ['#E5E7EB'];

  const fmt = (n) => {
    const num = Number(n) || 0;
    if (num >= 1000000) return (num / 1000000).toFixed(1) + 'M';
    if (num >= 1000) return (num / 1000).toFixed(0) + 'K';
    return num.toLocaleString();
  };

  const CustomTooltip = ({ active, payload, label }) => {
    if (active && payload && payload.length) {
      return (
        <div style={{background:'white',padding:'12px 16px',borderRadius:10,boxShadow:'0 4px 20px rgba(0,0,0,0.12)',border:'1px solid #E5E7EB'}}>
          <p style={{fontWeight:700,marginBottom:4,color:'#1A1A2E'}}>{label}</p>
          {payload.map((p, i) => (
            <p key={i} style={{color:p.color,fontSize:13,margin:'2px 0'}}>
              {p.name}: <strong>{Number(p.value).toLocaleString()} RWF</strong>
            </p>
          ))}
        </div>
      );
    }
    return null;
  };

  return (
    <>
      <TopBar title="Dashboard" />
      <div className="page-content">
        {loading ? (
          <div className="loading-spinner"><div className="spinner"></div><p>Loading dashboard...</p></div>
        ) : (
          <>
            <div className="welcome-banner">
              <div>
                <h2>Welcome back, {user?.fullName || user?.username} 👋</h2>
                <p>Here's your cooperative overview for today</p>
              </div>
            </div>

            <div className="stat-cards">
              <div className="stat-card">
                <div className="stat-icon green">👥</div>
                <div className="stat-info">
                  <h3>Total Members</h3>
                  <div className="stat-value">{stats?.totalMembers || 0}</div>
                </div>
              </div>
              <div className="stat-card">
                <div className="stat-icon gold">💰</div>
                <div className="stat-info">
                  <h3>Total Savings</h3>
                  <div className="stat-value">{fmt(stats?.totalSavings)} RWF</div>
                </div>
              </div>
              <div className="stat-card">
                <div className="stat-icon blue">🏦</div>
                <div className="stat-info">
                  <h3>Loans Disbursed</h3>
                  <div className="stat-value">{fmt(stats?.totalLoansDisbursed)} RWF</div>
                </div>
              </div>
              <div className="stat-card">
                <div className="stat-icon" style={{background:'#EDE7F6',color:'#5E35B1'}}>📋</div>
                <div className="stat-info">
                  <h3>Pending Approvals</h3>
                  <div className="stat-value">{stats?.pendingLoanApprovals || 0}</div>
                </div>
              </div>
              <div className="stat-card">
                <div className="stat-icon red">⚠️</div>
                <div className="stat-info">
                  <h3>Overdue</h3>
                  <div className="stat-value">{stats?.overdueRepayments || 0}</div>
                </div>
              </div>
              <div className="stat-card">
                <div className="stat-icon green">📈</div>
                <div className="stat-info">
                  <h3>Net Balance</h3>
                  <div className="stat-value">{fmt(stats?.netBalance)} RWF</div>
                </div>
              </div>
            </div>

            <div className="charts-grid">
              <div className="chart-card">
                <h3>Monthly Savings vs Loan Disbursements</h3>
                <ResponsiveContainer width="100%" height={280}>
                  <BarChart data={chartData} barGap={4} barSize={28}>
                    <defs>
                      <linearGradient id="greenGrad" x1="0" y1="0" x2="0" y2="1">
                        <stop offset="0%" stopColor="#43A047" /><stop offset="100%" stopColor="#1B5E20" />
                      </linearGradient>
                      <linearGradient id="goldGrad" x1="0" y1="0" x2="0" y2="1">
                        <stop offset="0%" stopColor="#FDD835" /><stop offset="100%" stopColor="#F9A825" />
                      </linearGradient>
                    </defs>
                    <CartesianGrid strokeDasharray="3 3" stroke="#F0F0F0" vertical={false} />
                    <XAxis dataKey="month" fontSize={12} tick={{fill:'#6B7280'}} axisLine={{stroke:'#E5E7EB'}} tickLine={false} />
                    <YAxis fontSize={12} tickFormatter={(v) => fmt(v)} tick={{fill:'#9CA3AF'}} axisLine={false} tickLine={false} />
                    <Tooltip content={<CustomTooltip />} />
                    <Legend iconType="circle" iconSize={8} wrapperStyle={{fontSize:13,paddingTop:8}} />
                    <Bar dataKey="savings" fill="url(#greenGrad)" radius={[6, 6, 0, 0]} name="Savings" />
                    <Bar dataKey="loans" fill="url(#goldGrad)" radius={[6, 6, 0, 0]} name="Loans" />
                  </BarChart>
                </ResponsiveContainer>
              </div>
              <div className="chart-card">
                <h3>Financial Overview</h3>
                <ResponsiveContainer width="100%" height={280}>
                  <PieChart>
                    <Pie data={displayPie} dataKey="value" nameKey="name" cx="50%" cy="50%" outerRadius={95} innerRadius={55} paddingAngle={pieData.length > 0 ? 3 : 0}
                      label={pieData.length > 0 ? ({name,percent})=>`${name} ${(percent*100).toFixed(0)}%` : false}
                      strokeWidth={2}>
                      {displayPie.map((_, i) => (
                        <Cell key={i} fill={pieColors[i % pieColors.length]} />
                      ))}
                    </Pie>
                    <Tooltip formatter={(v) => v.toLocaleString() + ' RWF'} contentStyle={{borderRadius:10,border:'1px solid #E5E7EB',boxShadow:'0 4px 20px rgba(0,0,0,0.1)'}} />
                    {pieData.length > 0 && <Legend iconType="circle" iconSize={8} wrapperStyle={{fontSize:13}} />}
                    {pieData.length === 0 && (
                      <text x="50%" y="50%" textAnchor="middle" dominantBaseline="middle" style={{fontSize:13,fill:'#9CA3AF'}}>
                        No data yet
                      </text>
                    )}
                  </PieChart>
                </ResponsiveContainer>
              </div>
            </div>
          </>
        )}
      </div>
    </>
  );
}
