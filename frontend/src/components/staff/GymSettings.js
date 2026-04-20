import React, { useState } from 'react';
import { gymApi } from '../../services/apiService';

function GymSettings() {
  const [settings, setSettings] = useState({
    crowdLimit: 50,
    sessionDurationMinutes: 60,
    minimumValidMinutes: 10,
    expiryAlertDays: 3,
    absentAlertDays: 7,
  });
  const [edited, setEdited] = useState(settings);
  const [isSaving, setIsSaving] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const gymId = localStorage.getItem('gymId') || '';

  React.useEffect(() => {
    const loadSettings = async () => {
      if (!gymId) {
        setError('Gym ID not found');
        return;
      }

      try {
        const result = await gymApi.getGymSettings(gymId);
        if (result?.data) {
          setSettings(result.data);
          setEdited(result.data);
        }
      } catch (err) {
        setError(err.message || 'Failed to load settings');
      }
    };

    loadSettings();
  }, [gymId]);

  const handleChange = (field, value) => {
    setEdited(prev => ({
      ...prev,
      [field]: Number(value)
    }));
  };

  const handleSave = async () => {
    if (!gymId) {
      setError('Gym ID not found');
      return;
    }

    setIsSaving(true);
    setError('');
    setSuccess('');

    try {
      const payload = {
        crowdLimit: edited.crowdLimit,
        sessionDurationMinutes: edited.sessionDurationMinutes,
        minimumValidMinutes: edited.minimumValidMinutes,
        expiryAlertDays: edited.expiryAlertDays,
        absentAlertDays: edited.absentAlertDays,
      };

      const result = await gymApi.updateGymSettings(gymId, payload);
      const updated = result?.data || payload;
      setSettings(updated);
      setEdited(updated);
      setSuccess('Settings saved successfully');
    } catch (err) {
      setError(err.message || 'Failed to save settings');
    } finally {
      setIsSaving(false);
    }
  };

  return (
    <div className="management-section">
      <div className="section-header">
        <h2>Gym Settings</h2>
        <button 
          className="btn-primary"
          onClick={handleSave}
          disabled={isSaving}
        >
          {isSaving ? 'Saving...' : 'Save Changes'}
        </button>
      </div>

      {error && <div className="error-message">{error}</div>}
      {success && <div className="badge">{success}</div>}

      <div className="settings-container">
        <div className="settings-grid">
          <div className="setting-card">
            <h3>Capacity Controls</h3>
            <div className="form-group">
              <label>Crowd Limit</label>
              <input
                type="number"
                value={edited.crowdLimit ?? settings.crowdLimit}
                onChange={(e) => handleChange('crowdLimit', e.target.value)}
              />
            </div>
            <div className="form-group">
              <label>Session Duration (Minutes)</label>
              <input
                type="number"
                value={edited.sessionDurationMinutes ?? settings.sessionDurationMinutes}
                onChange={(e) => handleChange('sessionDurationMinutes', e.target.value)}
              />
            </div>
            <div className="form-group">
              <label>Minimum Valid Minutes</label>
              <input
                type="number"
                value={edited.minimumValidMinutes ?? settings.minimumValidMinutes}
                onChange={(e) => handleChange('minimumValidMinutes', e.target.value)}
              />
            </div>
          </div>

          <div className="setting-card">
            <h3>Alert Rules</h3>
            <div className="form-group">
              <label>Expiry Alert Days</label>
              <input
                type="number"
                value={edited.expiryAlertDays ?? settings.expiryAlertDays}
                onChange={(e) => handleChange('expiryAlertDays', e.target.value)}
              />
            </div>
            <div className="form-group">
              <label>Absent Alert Days</label>
              <input
                type="number"
                value={edited.absentAlertDays ?? settings.absentAlertDays}
                onChange={(e) => handleChange('absentAlertDays', e.target.value)}
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default GymSettings;
