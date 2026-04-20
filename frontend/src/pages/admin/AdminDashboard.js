import React, { useState } from 'react';
import '../../styles/AdminDashboard.css';
import TenantManagement from '../../components/admin/TenantManagement';
import GymManagement from '../../components/admin/GymManagement';
import StaffManagement from '../../components/admin/StaffManagement';

function AdminDashboard({ onLogout, userData }) {
  const [activeTab, setActiveTab] = useState('home');

  return (
    <div className="admin-dashboard-container">
      <nav className="admin-navbar">
        <div className="navbar-brand">
          <h1>🏋️ Admin Dashboard</h1>
        </div>
        <div className="navbar-info">
          <span>{userData?.name}</span>
          <button className="btn-logout" onClick={onLogout}>Logout</button>
        </div>
      </nav>

      <div className="admin-content">
        <aside className="admin-sidebar">
          <button 
            className={`sidebar-btn ${activeTab === 'home' ? 'active' : ''}`}
            onClick={() => setActiveTab('home')}
          >
            🏠 Home
          </button>
          <button 
            className={`sidebar-btn ${activeTab === 'tenants' ? 'active' : ''}`}
            onClick={() => setActiveTab('tenants')}
          >
            🏢 Tenants
          </button>
          <button 
            className={`sidebar-btn ${activeTab === 'gyms' ? 'active' : ''}`}
            onClick={() => setActiveTab('gyms')}
          >
            🏋️ Gyms
          </button>
          <button 
            className={`sidebar-btn ${activeTab === 'staff' ? 'active' : ''}`}
            onClick={() => setActiveTab('staff')}
          >
            👨‍💼 Staff
          </button>
        </aside>

        <main className="admin-main-content">
          {activeTab === 'home' && (
            <div className="tab-content">
              <h2>Welcome, Admin!</h2>
              <div className="dashboard-stats">
                <div className="stat-card">
                  <h3>📊 Manage Tenants</h3>
                  <p>Create and manage gym owner tenants</p>
                </div>
                <div className="stat-card">
                  <h3>🏋️ Manage Gyms</h3>
                  <p>Oversee all gyms across tenants</p>
                </div>
                <div className="stat-card">
                  <h3>👨‍💼 Manage Staff</h3>
                  <p>Assign and manage gym staff</p>
                </div>
              </div>
            </div>
          )}

          {activeTab === 'tenants' && <TenantManagement />}
          {activeTab === 'gyms' && <GymManagement />}
          {activeTab === 'staff' && <StaffManagement />}
        </main>
      </div>
    </div>
  );
}

export default AdminDashboard;
