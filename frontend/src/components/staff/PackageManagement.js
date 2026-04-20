import React, { useState, useEffect, useCallback } from 'react';
import { packageApi } from '../../services/apiService';

function PackageManagement() {
  const [packages, setPackages] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    price: '',
    durationDays: '',
    sessions: '',
    type: 'MONTHLY',
    freezeAllowed: false,
    maxFreezeDays: '',
    features: ''
  });

  const gymId = localStorage.getItem('gymId') || '';

  const fetchPackages = useCallback(async () => {
    if (!gymId) {
      setError('Gym ID not found');
      return;
    }
    setLoading(true);
    try {
      const result = await packageApi.getGymPackages(gymId);
      setPackages(result.data || []);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, [gymId]);

  useEffect(() => {
    fetchPackages();
  }, [fetchPackages]);

  const handleInputChange = (e) => {
    const { name, value, checked, type } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      if (editingId) {
        await packageApi.updatePackage(gymId, editingId, formData);
      } else {
        await packageApi.createPackage(gymId, formData);
      }
      
      resetForm();
      setShowForm(false);
      await fetchPackages();
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (packageId) => {
    if (window.confirm('Are you sure you want to delete this package?')) {
      try {
        await packageApi.deletePackage(gymId, packageId);
        await fetchPackages();
      } catch (err) {
        setError(err.message);
      }
    }
  };

  const handleEdit = (pkg) => {
    setFormData({
      name: pkg.name,
      description: pkg.description || '',
      price: pkg.price,
      durationDays: pkg.durationDays,
      sessions: pkg.sessions,
      type: pkg.type,
      freezeAllowed: pkg.freezeAllowed || false,
      maxFreezeDays: pkg.maxFreezeDays || '',
      features: pkg.features || ''
    });
    setEditingId(pkg.id);
    setShowForm(true);
  };

  const resetForm = () => {
    setFormData({
      name: '',
      description: '',
      price: '',
      durationDays: '',
      sessions: '',
      type: 'MONTHLY',
      freezeAllowed: false,
      maxFreezeDays: '',
      features: ''
    });
    setEditingId(null);
  };

  return (
    <div className="management-section">
      <div className="section-header">
        <h2>Manage Packages</h2>
        <button 
          className="btn-primary"
          onClick={() => {
            if (showForm && !editingId) {
              setShowForm(false);
            } else {
              resetForm();
              setShowForm(!showForm);
            }
          }}
        >
          {showForm ? '✕ Cancel' : '+ New Package'}
        </button>
      </div>

      {error && <div className="error-message">{error}</div>}

      {showForm && (
        <form className="management-form" onSubmit={handleSubmit}>
          <div className="form-row">
            <div className="form-group">
              <label>Package Name</label>
              <input
                type="text"
                name="name"
                value={formData.name}
                onChange={handleInputChange}
                required
                placeholder="e.g., Gold Package"
              />
            </div>
            <div className="form-group">
              <label>Package Type</label>
              <select name="type" value={formData.type} onChange={handleInputChange}>
                <option value="MONTHLY">Monthly</option>
                <option value="QUARTERLY">Quarterly</option>
                <option value="YEARLY">Yearly</option>
                <option value="CUSTOM">Custom</option>
              </select>
            </div>
          </div>

          <div className="form-group">
            <label>Description</label>
            <textarea
              name="description"
              value={formData.description}
              onChange={handleInputChange}
              placeholder="Package description"
            />
          </div>

          <div className="form-row">
            <div className="form-group">
              <label>Price (₹)</label>
              <input
                type="number"
                name="price"
                value={formData.price}
                onChange={handleInputChange}
                required
                step="0.01"
              />
            </div>
            <div className="form-group">
              <label>Duration (Days)</label>
              <input
                type="number"
                name="durationDays"
                value={formData.durationDays}
                onChange={handleInputChange}
                required
              />
            </div>
            <div className="form-group">
              <label>Sessions Included</label>
              <input
                type="number"
                name="sessions"
                value={formData.sessions}
                onChange={handleInputChange}
                required
              />
            </div>
          </div>

          <div className="form-row">
            <div className="form-group checkbox">
              <input
                type="checkbox"
                name="freezeAllowed"
                checked={formData.freezeAllowed}
                onChange={handleInputChange}
              />
              <label>Allow Freeze</label>
            </div>
            {formData.freezeAllowed && (
              <div className="form-group">
                <label>Max Freeze Days</label>
                <input
                  type="number"
                  name="maxFreezeDays"
                  value={formData.maxFreezeDays}
                  onChange={handleInputChange}
                />
              </div>
            )}
          </div>

          <div className="form-group">
            <label>Features (comma-separated)</label>
            <textarea
              name="features"
              value={formData.features}
              onChange={handleInputChange}
              placeholder="e.g., Gym Access, Personal Training, Group Classes"
            />
          </div>

          <button type="submit" disabled={loading} className="btn-primary">
            {loading ? 'Saving...' : editingId ? 'Update Package' : 'Create Package'}
          </button>
        </form>
      )}

      <div className="list-section">
        {loading && <p>Loading packages...</p>}
        {packages.length === 0 && !loading && <p>No packages found</p>}

        <div className="packages-grid">
          {packages.map(pkg => (
            <div key={pkg.id} className="package-card">
              <h3>{pkg.name}</h3>
              <p className="package-type">{pkg.type}</p>
              <p className="package-price">₹{pkg.price} <span>/month</span></p>
              <ul className="package-details">
                <li>{pkg.durationDays} days validity</li>
                <li>{pkg.sessions} sessions</li>
                {pkg.freezeAllowed && <li>Freeze allowed ({pkg.maxFreezeDays} days max)</li>}
              </ul>
              <p className="package-description">{pkg.description}</p>
              <div className="card-actions">
                <button className="btn-sm btn-edit" onClick={() => handleEdit(pkg)}>Edit</button>
                <button className="btn-sm btn-delete" onClick={() => handleDelete(pkg.id)}>Delete</button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default PackageManagement;
