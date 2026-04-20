# Gym Management Platform Frontend

A simple HTML/JavaScript frontend for the Gym Management Platform backend.

## Features

- User login
- User signup
- Basic dashboard after login

## How to Run

1. Ensure the backend is running on `http://localhost:8080`.
2. Open `frontend/index.html` in a web browser.

## API Endpoints Used

- POST `/user/auth/login` - Login
- POST `/user/auth/signup` - Signup

The frontend stores the JWT token in localStorage for authentication.