import React, { useState, useEffect } from 'react';
import './App.css';
import Login from './pages/Login';
import Signup from './pages/Signup';
import Dashboard from './pages/Dashboard';
import AdminDashboard from './pages/admin/AdminDashboard';
import StaffDashboard from './pages/staff/StaffDashboard';

function App() {
  const [currentPage, setCurrentPage] = useState('login');
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [userData, setUserData] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('role');
    const userId = localStorage.getItem('userId');
    const tenantId = localStorage.getItem('tenantId');
    const gymId = localStorage.getItem('gymId');

    if (token) {
      setIsAuthenticated(true);
      setCurrentPage('dashboard');
      setUserData({ role, userId, tenantId, gymId, name: role || 'User' });
    }
  }, []);

  const handleLoginSuccess = (payload) => {
    localStorage.setItem('token', payload.token);
    localStorage.setItem('role', payload.role || 'USER');
    localStorage.setItem('userId', payload.userId || '');
    localStorage.setItem('tenantId', payload.tenantId || '');
    localStorage.setItem('gymId', payload.gymId || '');

    setUserData(payload);
    setIsAuthenticated(true);
    setCurrentPage('dashboard');
  };

  const handleSignupSuccess = () => {
    setCurrentPage('login');
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('userId');
    localStorage.removeItem('tenantId');
    localStorage.removeItem('gymId');
    setUserData(null);
    setIsAuthenticated(false);
    setCurrentPage('login');
  };

  const renderDashboard = () => {
    const role = localStorage.getItem('role');

    if (role === 'ADMIN' || role === 'MANAGEMENT') {
      return <AdminDashboard onLogout={handleLogout} userData={userData} />;
    }

    if (role === 'STAFF') {
      return <StaffDashboard onLogout={handleLogout} userData={userData} />;
    }

    return <Dashboard onLogout={handleLogout} />;
  };

  const navigateTo = (page) => {
    setCurrentPage(page);
  };

  return (
    <div className="App">
      {isAuthenticated ? (
        renderDashboard()
      ) : (
        <>
          {currentPage === 'login' && (
            <Login 
              onLoginSuccess={handleLoginSuccess}
              onNavigateToSignup={() => navigateTo('signup')}
            />
          )}
          {currentPage === 'signup' && (
            <Signup 
              onSignupSuccess={handleSignupSuccess}
              onNavigateToLogin={() => navigateTo('login')}
            />
          )}
        </>
      )}
    </div>
  );
}

export default App;
