import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './components/Auth/Login';
import Register from './components/Auth/Register';
import Dashboard from './components/Layout/Dashboard';
import Header from './components/Layout/Header';
import Sidebar from './components/Layout/Sidebar';
import UserManagement from './components/User/UserManagement';
import OrderList from './components/Order/OrderList';
import CreateOrder from './components/Order/CreateOrder';
import OrderStatistics from './components/Order/OrderStatistics';
import ProfileManagement from './components/UserProfile/ProfileManagement';
import LoginHistoryView from './components/LoginHistory/LoginHistoryView';

// Protected Route Component
const ProtectedRoute = ({ children }) => {
  const userId = localStorage.getItem('userId');
  return userId ? children : <Navigate to="/login" />;
};

// Main Layout Component
const MainLayout = ({ children }) => {
  return (
    <div className="flex h-screen bg-gray-100">
      <Sidebar />
      <div className="flex-1 flex flex-col overflow-hidden">
        <Header />
        <main className="flex-1 overflow-x-hidden overflow-y-auto bg-gray-100">
          <div className="container mx-auto px-6 py-8">
            {children}
          </div>
        </main>
      </div>
    </div>
  );
};

function App() {
  return (
    <Router>
      <Routes>
        {/* Public Routes */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        {/* Protected Routes */}
        <Route
          path="/dashboard"
          element={
            <ProtectedRoute>
              <MainLayout>
                <Dashboard />
              </MainLayout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/users"
          element={
            <ProtectedRoute>
              <MainLayout>
                <UserManagement />
              </MainLayout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/orders"
          element={
            <ProtectedRoute>
              <MainLayout>
                <OrderList />
              </MainLayout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/orders/create"
          element={
            <ProtectedRoute>
              <MainLayout>
                <CreateOrder />
              </MainLayout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/orders/statistics"
          element={
            <ProtectedRoute>
              <MainLayout>
                <OrderStatistics />
              </MainLayout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/profile"
          element={
            <ProtectedRoute>
              <MainLayout>
                <ProfileManagement />
              </MainLayout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/login-history"
          element={
            <ProtectedRoute>
              <MainLayout>
                <LoginHistoryView />
              </MainLayout>
            </ProtectedRoute>
          }
        />

        {/* Default Route */}
        <Route path="/" element={<Navigate to="/login" />} />
        <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
    </Router>
  );
}

export default App;
