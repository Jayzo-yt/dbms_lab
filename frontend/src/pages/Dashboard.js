import React, { useState } from 'react';
import '../styles/Dashboard.css';

function Dashboard({ onLogout }) {
  const [activeTab, setActiveTab] = useState('home');

  return (
    <div className="dashboard-container">
      <nav className="navbar">
        <div className="navbar-brand">
          <h1>💪 Gym Management Platform</h1>
        </div>
        <button className="btn-logout" onClick={onLogout}>
          Logout
        </button>
      </nav>

      <div className="dashboard-content">
        <aside className="sidebar">
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
            className={`sidebar-btn ${activeTab === 'payments' ? 'active' : ''}`}
            onClick={() => setActiveTab('payments')}
          >
            💳 Payments
          </button>
          <button 
            className={`sidebar-btn ${activeTab === 'entries' ? 'active' : ''}`}
            onClick={() => setActiveTab('entries')}
          >
            📝 Entry Logs
          </button>
        </aside>

        <main className="main-content">
          {activeTab === 'home' && (
            <div className="tab-content">
              <h2>Welcome to Gym Management Platform</h2>
              <div className="dashboard-grid">
                <div className="card">
                  <h3>📊 Dashboard</h3>
                  <p>Manage your gym operations efficiently</p>
                </div>
                <div className="card">
                  <h3>👥 Members</h3>
                  <p>Track and manage all gym members</p>
                </div>
                <div className="card">
                  <h3>📦 Packages</h3>
                  <p>Create and manage membership packages</p>
                </div>
                <div className="card">
                  <h3>💳 Payments</h3>
                  <p>Monitor member payments and billing</p>
                </div>
              </div>
            </div>
          )}

          {activeTab === 'members' && (
            <div className="tab-content">
              <h2>Members</h2>
              <p>Member management feature will be available soon.</p>
              <div className="placeholder">
                <button className="btn-primary">Add New Member</button>
              </div>
            </div>
          )}

          {activeTab === 'packages' && (
            <div className="tab-content">
              <h2>Membership Packages</h2>
              <p>Manage your gym's membership packages.</p>
              <div className="placeholder">
                <button className="btn-primary">Create Package</button>
              </div>
            </div>
          )}

          {activeTab === 'payments' && (
            <div className="tab-content">
              <h2>Payments</h2>
              <p>View and manage member payments.</p>
              <div className="placeholder">
                <button className="btn-primary">Record Payment</button>
              </div>
            </div>
          )}

          {activeTab === 'entries' && (
            <div className="tab-content">
              <h2>Entry Logs</h2>
              <p>Track member gym entry and exit logs.</p>
              <div className="placeholder">
                <button className="btn-primary">View Logs</button>
              </div>
            </div>
          )}
        </main>
      </div>
    </div>
  );
}

export default Dashboard;