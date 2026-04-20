import React, { useState, useEffect } from 'react';
import { tenantApi } from '../../services/apiService';

function TenantManagement() {
  const [tenants, setTenants] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [formData, setFormData] = useState({
    tenantCode: '',
    name: '',
    email: '',
    phone: '',
    address: ''
  });

  useEffect(() => {
    fetchTenants();
  }, []);

  const fetchTenants = async () => {
    setLoading(true);
    try {
      const result = await tenantApi.getMyTenants();
      setTenants(result || []);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      await tenantApi.createTenant(formData);
      setFormData({
        tenantCode: '',
        name: '',
        email: '',
        phone: '',
        address: ''
      });
      setShowForm(false);
      await fetchTenants();
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="management-section">
      <div className="section-header">
        <h2>Manage Tenants</h2>
        <button 
          className="btn-primary"
          onClick={() => setShowForm(!showForm)}
        >
          {showForm ? '✕ Cancel' : '+ New Tenant'}
        </button>
      </div>

      {error && <div className="error-message">{error}</div>}

      {showForm && (
        <form className="management-form" onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Tenant Code</label>
            <input
              type="text"
              name="tenantCode"
              value={formData.tenantCode}
              onChange={handleInputChange}
              required
              placeholder="e.g., TENANT001"
            />
          </div>

          <div className="form-group">
            <label>Tenant Name</label>
            <input
              type="text"
              name="name"
              value={formData.name}
              onChange={handleInputChange}
              required
              placeholder="Gym Owner Name"
            />
          </div>

          <div className="form-group">
            <label>Email</label>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleInputChange}
              required
              placeholder="owner@gym.com"
            />
          </div>

          <div className="form-group">
            <label>Phone</label>
            <input
              type="tel"
              name="phone"
              value={formData.phone}
              onChange={handleInputChange}
              placeholder="+1234567890"
            />
          </div>

          <div className="form-group">
            <label>Address</label>
            <textarea
              name="address"
              value={formData.address}
              onChange={handleInputChange}
              placeholder="Gym Location Address"
            />
          </div>

          <button type="submit" disabled={loading} className="btn-primary">
            {loading ? 'Creating...' : 'Create Tenant'}
          </button>
        </form>
      )}

      <div className="list-section">
        {loading && <p>Loading tenants...</p>}
        {tenants.length === 0 && !loading && <p>No tenants found</p>}

        <div className="list-grid">
          {tenants.map(tenant => (
            <div key={tenant.id} className="list-item-card">
              <h3>{tenant.name}</h3>
              <p><strong>Code:</strong> {tenant.tenantCode}</p>
              <p><strong>Email:</strong> {tenant.email}</p>
              <p><strong>Phone:</strong> {tenant.phone}</p>
              <p><strong>Status:</strong> <span className="badge">{tenant.status}</span></p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default TenantManagement;
