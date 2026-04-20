import React, { useState } from 'react';
import '../../styles/StaffDashboard.css';
import MemberManagement from '../../components/staff/MemberManagement';
import PackageManagement from '../../components/staff/PackageManagement';
import GymSettings from '../../components/staff/GymSettings';

function StaffDashboard({ onLogout, userData }) {
  const [activeTab, setActiveTab] = useState('home');

  return (
    <div className="staff-dashboard-container">
      <nav className="staff-navbar">
        <div className="navbar-brand">
          <h1>💪 Staff Dashboard</h1>
        </div>
        <div className="navbar-info">
          <span>{userData?.name}</span>
          <button className="btn-logout" onClick={onLogout}>Logout</button>
        </div>
      </nav>

      <div className="staff-content">
        <aside className="staff-sidebar">
          <button 
            className={`sidebar-btn ${activeTab === 'home' ? 'active' : ''}`}
            onClick={() => setActiveTab('home')}
          >
            🏠 Home
          </button>
          <button 
            className={`sidebar-btn ${activeTab === 'members' ? 'active' : ''}`}
            onClick={() => setActiveTab('members')}
          >
            👥 Members
          </button>
          <button 
            className={`sidebar-btn ${activeTab === 'packages' ? 'active' : ''}`}
            onClick={() => setActiveTab('packages')}
          >
            📦 Packages
          </button>
          <button 
            className={`sidebar-btn ${activeTab === 'settings' ? 'active' : ''}`}
            onClick={() => setActiveTab('settings')}
          >
            ⚙️ Gym Settings
          </button>
        </aside>

        <main className="staff-main-content">
          {activeTab === 'home' && (
            <div className="tab-content">
              <h2>Welcome, {userData?.name}!</h2>
              <div className="dashboard-cards">
                <div className="card">
                  <h3>👥 Manage Members</h3>
                  <p>Add, update, and manage gym members</p>
                </div>
                <div className="card">
                  <h3>📦 Manage Packages</h3>
                  <p>Create and manage membership packages</p>
                </div>
                <div className="card">
                  <h3>⚙️ Gym Settings</h3>
                  <p>Configure gym information and settings</p>
                </div>
              </div>
            </div>
          )}

          {activeTab === 'members' && <MemberManagement />}
          {activeTab === 'packages' && <PackageManagement />}
          {activeTab === 'settings' && <GymSettings />}
        </main>
      </div>
    </div>
  );
}

export default StaffDashboard;
