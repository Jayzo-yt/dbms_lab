const API_BASE = 'http://localhost:8080';

const AUTH_ENDPOINTS = {
  DEFAULT: '/auth',
  ADMIN: '/auth/admin',
  STAFF: '/auth/staff',
};

const unwrapData = (response) => {
  if (response && typeof response === 'object' && 'data' in response) {
    return response.data;
  }
  return response;
};

// Helper function to make API requests with proper error handling.
const makeRequest = async (endpoint, options = {}) => {
  const url = `${API_BASE}${endpoint}`;
  const { skipAuth = false, ...requestOptions } = options;

  const defaultOptions = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      ...(requestOptions.headers || {}),
    },
    credentials: 'include',
    ...requestOptions,
  };

  const token = localStorage.getItem('token');
  if (!skipAuth && token && !defaultOptions.headers.Authorization) {
    defaultOptions.headers.Authorization = `Bearer ${token}`;
  }

  try {
    console.log(`[API] ${defaultOptions.method} ${url}`, {
      body: defaultOptions.body ? JSON.parse(defaultOptions.body) : null,
    });

    const response = await fetch(url, defaultOptions);
    const text = await response.text();
    const data = text ? JSON.parse(text) : null;

    console.log('[API] Response:', { status: response.status, data });

    if (!response.ok) {
      const error = new Error(data?.message || `API Error: ${response.status}`);
      error.status = response.status;
      error.response = data;
      throw error;
    }

    return data;
  } catch (error) {
    console.error('[API] Error:', error);
    throw error;
  }
};

export const authApi = {
  login: (email, password) =>
    makeRequest(`${AUTH_ENDPOINTS.DEFAULT}/login`, {
      method: 'POST',
      skipAuth: true,
      body: JSON.stringify({ email, password }),
    }),

  adminSignup: (name, email, password, phone = '') =>
    makeRequest('/auth/admin/signup', {
      method: 'POST',
      skipAuth: true,
      body: JSON.stringify({ name, email, password, phone }),
    }),

  changePassword: (currentPassword, newPassword) =>
    makeRequest('/auth/change-password', {
      method: 'POST',
      body: JSON.stringify({ currentPassword, newPassword }),
    }),

  createUserByAdmin: async (payload) =>
    unwrapData(await makeRequest('/auth/admin/create-user', {
      method: 'POST',
      body: JSON.stringify(payload),
    })),
};

export const tenantApi = {
  createTenant: async (payload) =>
    unwrapData(await makeRequest('/admin/tenant', {
      method: 'POST',
      body: JSON.stringify(payload),
    })),

  getMyTenants: async () => unwrapData(await makeRequest('/admin/tenant')),
};

export const gymApi = {
  createGym: async (payload) =>
    unwrapData(await makeRequest('/admin/gym', {
      method: 'POST',
      body: JSON.stringify(payload),
    })),

  getAllGyms: async () => unwrapData(await makeRequest('/admin/gyms')),

  updateGymSettings: (gymId, payload) =>
    makeRequest(`/admin/gym/${gymId}/settings`, {
      method: 'PUT',
      body: JSON.stringify(payload),
    }),

  getGymSettings: (gymId) => makeRequest(`/admin/gym/${gymId}/settings`),
};

export const staffApi = {
  assignStaff: async (gymId, payload) =>
    unwrapData(await makeRequest(`/admin/gym/${gymId}/staff`, {
      method: 'POST',
      body: JSON.stringify(payload),
    })),

  getGymStaff: async (gymId) => unwrapData(await makeRequest(`/admin/gym/${gymId}/staff`)),

  updateStaff: (gymId, staffId, payload) =>
    makeRequest(`/admin/gym/${gymId}/staff/${staffId}`, {
      method: 'PUT',
      body: JSON.stringify(payload),
    }),

  removeStaff: (gymId, staffId) =>
    makeRequest(`/admin/gym/${gymId}/staff/${staffId}`, {
      method: 'DELETE',
    }),
};

export const memberApi = {
  createMember: (gymId, payload) =>
    makeRequest(`/staff/gym/${gymId}/members`, {
      method: 'POST',
      body: JSON.stringify(payload),
    }),

  getGymMembers: (gymId) => makeRequest(`/staff/gym/${gymId}/members`),

  updateMember: (gymId, memberId, payload) =>
    makeRequest(`/staff/gym/${gymId}/members/${memberId}`, {
      method: 'PUT',
      body: JSON.stringify(payload),
    }),

  deleteMember: (gymId, memberId) =>
    makeRequest(`/staff/gym/${gymId}/members/${memberId}`, {
      method: 'DELETE',
    }),
};

export const packageApi = {
  createPackage: (gymId, payload) =>
    makeRequest(`/staff/gym/${gymId}/packages`, {
      method: 'POST',
      body: JSON.stringify(payload),
    }),

  getGymPackages: (gymId) => makeRequest(`/staff/gym/${gymId}/packages`),

  updatePackage: (gymId, packageId, payload) =>
    makeRequest(`/staff/gym/${gymId}/packages/${packageId}`, {
      method: 'PUT',
      body: JSON.stringify(payload),
    }),

  deletePackage: (gymId, packageId) =>
    makeRequest(`/staff/gym/${gymId}/packages/${packageId}`, {
      method: 'DELETE',
    }),
};

// Health check - verify backend is running
export const healthCheck = async () => {
  try {
    const response = await fetch(`${API_BASE}/health`, {
      method: 'GET',
      credentials: 'include',
    });
    return response.ok;
  } catch (error) {
    console.error('Health check failed:', error);
    return false;
  }
};

// eslint-disable-next-line import/no-anonymous-default-export
export default {
  authApi,
  tenantApi,
  gymApi,
  staffApi,
  memberApi,
  packageApi,
  healthCheck,
};
