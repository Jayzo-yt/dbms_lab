# Gym Management Platform - Frontend

A modern React-based frontend for the Gym Management Platform backend.

## 🎯 Features

✅ User Authentication (Login/Signup)
✅ JWT Token Management
✅ Dashboard with Sidebar Navigation
✅ Responsive UI Design
✅ Multiple sections: Home, Members, Packages, Payments, Entry Logs

## 📋 Prerequisites

- Node.js (v18 or higher)
- npm or yarn
- Backend running on `http://localhost:8080`

## 🚀 Installation & Setup

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

## 🏃 Running the Application

Start the development server:

```bash
npm start
```

The app will automatically open at `http://localhost:3000`

## 🔨 Build for Production

```bash
npm run build
```

This creates an optimized production build in the `build/` folder.

## 📁 Project Structure

```
frontend/
├── src/
│   ├── pages/
│   │   ├── Login.js       # Login page component
│   │   ├── Signup.js      # Signup page component
│   │   └── Dashboard.js   # Main dashboard component
│   ├── styles/
│   │   ├── Auth.css       # Login/Signup styling
│   │   └── Dashboard.css  # Dashboard styling
│   ├── App.js             # Main app component
│   ├── App.css            # Global app styling
│   └── index.js           # React entry point
├── public/
│   └── index.html         # HTML template
└── package.json           # Dependencies
```

## 🔌 API Endpoints Used

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/user/auth/login` | User login |
| POST | `/user/auth/signup` | User registration |

## 🔐 Authentication Flow

1. User enters email and password on login page
2. Frontend sends credentials to backend
3. Backend returns JWT token
4. Token is stored in browser's localStorage
5. Token is sent with subsequent API requests
6. User is redirected to dashboard on successful login

## 📱 Available Pages

### Login Page
- Email and password input
- Link to signup page
- Error message display
- Loading state management

### Signup Page
- First name and last name inputs
- Email input with validation
- Password with confirmation
- Success/error feedback

### Dashboard
- Navigation sidebar with sections
- Home dashboard with statistics cards
- Members section (placeholder)
- Packages section (placeholder)
- Payments section (placeholder)
- Entry logs section (placeholder)
- Logout button

## 🛠️ Available Scripts

```bash
npm start      # Run development server on port 3000
npm test       # Run tests in watch mode
npm run build  # Build for production
npm run eject  # Eject from Create React App (irreversible)
```

## 🎨 Styling

The app uses CSS modules with:
- Modern gradient backgrounds
- Responsive grid layouts
- Hover effects and transitions
- Custom scrollbar styling
- Mobile-friendly design

## 🐛 Troubleshooting

### CORS Errors
- Ensure backend has CORS enabled for `http://localhost:3000`
- Check backend's `SecurityConfig.java` for CORS configuration

### Connection Refused
- Verify backend is running on `http://localhost:8080`
- Check network connectivity

### Login/Signup Fails
- Open browser Developer Tools (F12)
- Check Console tab for error messages
- Verify backend is running and database is accessible

### Port 3000 Already in Use
```bash
npm start -- --port 3001
```

## 🚀 Future Enhancements

- [ ] Member management CRUD operations
- [ ] Package management features
- [ ] Payment tracking and invoicing
- [ ] Entry log viewer with filters
- [ ] User profile management
- [ ] Pagination for lists
- [ ] Search and advanced filtering
- [ ] Data export (CSV/PDF)
- [ ] Real-time notifications
- [ ] Charts and analytics

## 📦 Dependencies

Main packages:
- `react` - UI library
- `react-dom` - React DOM rendering
- `react-scripts` - Create React App build tools

## 🤝 Contributing

1. Create a new branch for features
2. Make changes and test thoroughly
3. Commit with clear messages
4. Submit pull request

## 📝 Notes

- The backend must be running before using the frontend
- JWT tokens expire after 24 hours (configurable in backend)
- localStorage stores the JWT token - clear it to logout completely
- API base URL is hardcoded to `http://localhost:8080`

## 📞 Support

For issues or questions, check:
1. Browser console for error messages
2. Backend logs for API errors
3. Network tab in Developer Tools
