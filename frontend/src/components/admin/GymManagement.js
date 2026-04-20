import React, { useState, useEffect } from 'react';
import { gymApi } from '../../services/apiService';

function GymManagement() {
  const [gyms, setGyms] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [formData, setFormData] = useState({
    gymName: '',
    gymCode: '',
    email: '',
    phoneNumber: '',
    address: '',
    gstNumber: ''
  });

  useEffect(() => {
    fetchGyms();
  }, []);

  const fetchGyms = async () => {
    setLoading(true);
    try {
      const result = await gymApi.getAllGyms();
      setGyms(result || []);
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
      await gymApi.createGym(formData);
      setFormData({
        gymName: '',
        gymCode: '',
        email: '',
        phoneNumber: '',
        address: '',
        gstNumber: ''
      });
      setShowForm(false);
      await fetchGyms();
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="management-section">
      <div className="section-header">
        <h2>Manage Gyms</h2>
        <button 
          className="btn-primary"
          onClick={() => setShowForm(!showForm)}
        >
          {showForm ? '✕ Cancel' : '+ New Gym'}
        </button>
      </div>

      {error && <div className="error-message">{error}</div>}

      {showForm && (
        <form className="management-form" onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Gym Name</label>
            <input
              type="text"
              name="gymName"
              value={formData.gymName}
              onChange={handleInputChange}
              required
              placeholder="Gym Name"
            />
          </div>

          <div className="form-group">
            <label>Gym Code</label>
            <input
              type="text"
              name="gymCode"
              value={formData.gymCode}
              onChange={handleInputChange}
              required
              placeholder="GYM001"
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
              placeholder="gym@email.com"
            />
          </div>

          <div className="form-group">
            <label>Phone</label>
            <input
              type="tel"
              name="phoneNumber"
              value={formData.phoneNumber}
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
              required
              placeholder="Gym Location"
            />
          </div>

          <div className="form-group">
            <label>GST Number</label>
            <input
              type="text"
              name="gstNumber"
              value={formData.gstNumber}
              onChange={handleInputChange}
              placeholder="GST Number"
            />
          </div>

          <button type="submit" disabled={loading} className="btn-primary">
            {loading ? 'Creating...' : 'Create Gym'}
          </button>
        </form>
      )}

      <div className="list-section">
        {loading && <p>Loading gyms...</p>}
        {gyms.length === 0 && !loading && <p>No gyms found</p>}

        <div className="list-grid">
          {gyms.map(gym => (
            <div key={gym.id} className="list-item-card">
              <h3>{gym.gymName}</h3>
              <p><strong>Code:</strong> {gym.gymCode}</p>
              <p><strong>Email:</strong> {gym.email}</p>
              <p><strong>Phone:</strong> {gym.phoneNumber}</p>
              <p><strong>Address:</strong> {gym.address}</p>
              <p><strong>Status:</strong> <span className="badge">{gym.status}</span></p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default GymManagement;
