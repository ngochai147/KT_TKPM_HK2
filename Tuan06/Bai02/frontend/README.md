# Demo Management System - Frontend

React frontend application for the Demo Management System, built with TailwindCSS.

## Features

- **User Authentication**: Login and Registration
- **User Management**: View and manage users by status
- **Order Management**: Create, view, update, and cancel orders
- **Order Statistics**: View detailed order analytics
- **User Profile**: Create and update user profile information
- **Login History**: Track login sessions and security monitoring
- **Responsive Design**: Mobile-friendly interface with TailwindCSS

## Tech Stack

- React 18
- React Router DOM 6
- Axios for API calls
- TailwindCSS for styling

## Prerequisites

- Node.js (v14 or higher)
- npm or yarn
- Backend API running on `http://localhost:8080`

## Installation

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm start
```

The application will open at `http://localhost:3000`

## Available Scripts

- `npm start` - Runs the app in development mode
- `npm build` - Builds the app for production
- `npm test` - Runs the test suite
- `npm eject` - Ejects from Create React App (one-way operation)

## Project Structure

```
frontend/
├── public/
│   └── index.html
├── src/
│   ├── components/
│   │   ├── Auth/
│   │   │   ├── Login.js
│   │   │   └── Register.js
│   │   ├── User/
│   │   │   └── UserManagement.js
│   │   ├── Order/
│   │   │   ├── OrderList.js
│   │   │   ├── CreateOrder.js
│   │   │   └── OrderStatistics.js
│   │   ├── UserProfile/
│   │   │   └── ProfileManagement.js
│   │   ├── LoginHistory/
│   │   │   └── LoginHistoryView.js
│   │   └── Layout/
│   │       ├── Header.js
│   │       ├── Sidebar.js
│   │       └── Dashboard.js
│   ├── services/
│   │   └── api.js
│   ├── utils/
│   │   └── axios.js
│   ├── App.js
│   ├── index.js
│   └── index.css
├── package.json
├── tailwind.config.js
└── postcss.config.js
```

## API Endpoints

The frontend connects to the following API endpoints:

### User APIs (`/api/users`)
- POST `/register` - Register new user
- POST `/login` - Login
- GET `/{userId}` - Get user details
- GET `/email/{email}` - Search by email
- GET `/status/{status}` - Filter by status
- PUT `/{userId}/status` - Update status
- POST `/{userId}/change-password` - Change password
- DELETE `/{userId}` - Delete user

### Order APIs (`/api/orders`)
- POST `/` - Create order
- GET `/user/{userId}` - Get user orders
- GET `/user/{userId}/status/{status}` - Filter by status
- GET `/user/{userId}/recent` - Get recent orders
- GET `/code/{orderCode}` - Search by code
- GET `/{orderId}` - Get order details
- PUT `/{orderId}/status` - Update status
- DELETE `/{orderId}/cancel` - Cancel order
- GET `/user/{userId}/statistics` - Order statistics
- GET `/user/{userId}/spent` - Total spent

### Profile APIs (`/api/profiles`)
- POST `/` - Create profile
- GET `/{userId}` - Get profile
- PUT `/{userId}` - Update profile
- GET `/phone/{phoneNumber}` - Search by phone

### Login History APIs (`/api/login-history`)
- POST `/record` - Record login
- POST `/{historyId}/logout` - Record logout
- GET `/user/{userId}` - Get login history
- GET `/user/{userId}/recent` - Get recent logins
- GET `/user/{userId}/today` - Login count today
- GET `/user/{userId}/suspicious` - Check suspicious activity

## Configuration

Update the API base URL in `src/utils/axios.js` if your backend runs on a different port:

```javascript
const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/api',
  // ...
});
```

## Features Overview

### Authentication
- Secure login with email/password
- User registration with validation
- Automatic session management with localStorage

### Dashboard
- Quick statistics overview
- Recent orders display
- Quick action buttons

### Order Management
- Create new orders with validation
- View all orders with filtering
- Update order status
- Cancel orders
- Search by order code
- View order statistics and analytics

### User Profile
- Create and update profile information
- View profile details
- Phone number validation

### Login History
- Track all login sessions
- Security monitoring
- Suspicious activity detection
- Device and IP tracking

### User Management (Admin)
- Filter users by status
- Search users by email
- Update user status
- Delete users

## Security Features

- Protected routes with authentication
- Automatic logout on 401 responses
- Session tracking
- Suspicious activity detection

## Styling

The application uses TailwindCSS with a custom color scheme:

- Primary color: Blue (`#0ea5e9`)
- Success: Green
- Warning: Yellow
- Error: Red

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Troubleshooting

### CORS Issues
If you encounter CORS errors, ensure the backend has CORS enabled for `http://localhost:3000`

### API Connection
Verify the backend is running on `http://localhost:8080` and all endpoints are accessible

### Dependencies
If you have installation issues, try:
```bash
rm -rf node_modules package-lock.json
npm install
```
