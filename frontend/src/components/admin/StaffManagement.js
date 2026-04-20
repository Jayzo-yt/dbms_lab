import React, { useState, useEffect, useCallback } from 'react';
import { authApi, gymApi, staffApi } from '../../services/apiService';

function StaffManagement() {
  const gymId = localStorage.getItem('gymId') || '';

  const [staffs, setStaffs] = useState([]);
  const [gyms, setGyms] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [formData, setFormData] = useState({
    name: '',
    age: '',
    email: '',
    password: '',
    phone: '',
    gymId: gymId,
    role: 'MANAGER'
  });

  const fetchGyms = useCallback(async () => {
    try {
      const result = await gymApi.getAllGyms();
      const gymList = result || [];
      setGyms(gymList);

      if (!gymId && !formData.gymId && gymList.length > 0) {
        setFormData((prev) => ({
          ...prev,
          gymId: gymList[0].id,
        }));
      }
    } catch (err) {
      setError(err.message);
    }
  }, [gymId, formData.gymId]);

  const fetchStaffs = useCallback(async () => {
    const selectedGymId = formData.gymId || gymId;
    if (!selectedGymId) {
      return;
    }

    setLoading(true);
    try {
      const result = await staffApi.getGymStaff(selectedGymId);
      setStaffs(result || []);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, [formData.gymId, gymId]);

  useEffect(() => {
    fetchStaffs();
  }, [fetchStaffs]);

  useEffect(() => {
    fetchGyms();
  }, [fetchGyms]);

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
    setSuccess('');

    try {
      const createdUser = await authApi.createUserByAdmin({
        name: formData.name,
        age: formData.age ? Number(formData.age) : null,
        email: formData.email,
        password: formData.password,
        phone: formData.phone,
        role: 'STAFF',
        gymId: formData.gymId,
      });

      const staffUserId = createdUser?.userId;
      if (!staffUserId) {
        throw new Error('Failed to create staff account');
      }

      await staffApi.assignStaff(formData.gymId, {
        staffId: staffUserId,
        gymId: formData.gymId,
        role: formData.role,
      });

      setFormData({
        name: '',
        age: '',
        email: '',
        password: '',
        phone: '',
        gymId: gymId,
        role: 'MANAGER'
      });
      setShowForm(false);
      setSuccess('Staff created and assigned successfully');
      await fetchStaffs();
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="management-section">
      <div className="section-header">
        <h2>Manage Staff Assignments</h2>
        <button 
          className="btn-primary"
          onClick={() => setShowForm(!showForm)}
        >
          {showForm ? '✕ Cancel' : '+ Assign Staff'}
        </button>
      </div>

      {error && <div className="error-message">{error}</div>}
      {success && <div className="success-message">{success}</div>}

      {showForm && (
        <form className="management-form" onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Staff Name</label>
            <input
              type="text"
              name="name"
              value={formData.name}
              onChange={handleInputChange}
              required
              placeholder="Staff full name"
            />
          </div>

          <div className="form-group">
            <label>Age</label>
            <input
              type="number"
              name="age"
              value={formData.age}
              onChange={handleInputChange}
              min="18"
              placeholder="Age"
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
              placeholder="staff@example.com"
            />
          </div>

          <div className="form-group">
            <label>Password</label>
            <input
              type="password"
              name="password"
              value={formData.password}
              onChange={handleInputChange}
              required
              minLength={8}
              placeholder="Minimum 8 characters"
            />
          </div>

          <div className="form-group">
            <label>Phone</label>
            <input
              type="text"
              name="phone"
              value={formData.phone}
              onChange={handleInputChange}
              placeholder="Phone number"
            />
          </div>

          <div className="form-group">
            <label>Assign Gym</label>
            <select
              name="gymId"
              value={formData.gymId}
              onChange={handleInputChange}
              required
              disabled={Boolean(gymId)}
            >
              <option value="">Select a gym</option>
              {gyms.map((gym) => (
                <option key={gym.id} value={gym.id}>
                  {gym.gymName}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Staff Role</label>
            <select
              name="role"
              value={formData.role}
              onChange={handleInputChange}
            >
              <option value="MANAGER">Manager</option>
              <option value="TRAINER">Trainer</option>
              <option value="RECEPTIONIST">Receptionist</option>
              <option value="NUTRITION_EXPERT">Nutrition Expert</option>
              <option value="BILLING_STAFF">Billing Staff</option>
            </select>
          </div>

          <button type="submit" disabled={loading} className="btn-primary">
            {loading ? 'Creating...' : 'Create Staff'}
          </button>
        </form>
      )}

      <div className="list-section">
        {loading && <p>Loading staff assignments...</p>}
        {staffs.length === 0 && !loading && <p>No staff assignments found</p>}

        <div className="table-container">
          <table className="data-table">
            <thead>
              <tr>
                <th>Staff Name</th>
                <th>Email</th>
                <th>Gym ID</th>
                <th>Role</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {staffs.map(staff => (
                <tr key={staff.id}>
                  <td>{staff.staffName}</td>
                  <td>{staff.staffEmail}</td>
                  <td>{staff.gymId}</td>
                  <td>{staff.role}</td>
                  <td><span className="badge">{staff.active ? 'Active' : 'Inactive'}</span></td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

export default StaffManagement;
