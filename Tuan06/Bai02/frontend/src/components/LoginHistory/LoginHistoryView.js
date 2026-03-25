import React, { useState, useEffect } from 'react';
import { loginHistoryAPI } from '../../services/api';

const LoginHistoryView = () => {
  const userId = localStorage.getItem('userId');
  const [loginHistory, setLoginHistory] = useState([]);
  const [suspiciousData, setSuspiciousData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [limit, setLimit] = useState(20);

  useEffect(() => {
    loadLoginHistory();
    checkSuspiciousActivity();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [limit, userId]);

  const loadLoginHistory = async () => {
    if (!userId) return;

    setLoading(true);
    try {
      const response = await loginHistoryAPI.getRecentLogins(userId, limit);
      setLoginHistory(response.data.data || []);
    } catch (error) {
      console.error('Error loading login history:', error);
    } finally {
      setLoading(false);
    }
  };

  const checkSuspiciousActivity = async () => {
    if (!userId) return;

    try {
      const response = await loginHistoryAPI.checkSuspiciousActivity(userId, 7);
      setSuspiciousData(response.data);
    } catch (error) {
      console.error('Error checking suspicious activity:', error);
    }
  };

  const getRiskColor = (riskLevel) => {
    switch (riskLevel) {
      case 'HIGH':
        return 'bg-red-100 text-red-800 border-red-300';
      case 'MEDIUM':
        return 'bg-yellow-100 text-yellow-800 border-yellow-300';
      case 'LOW':
        return 'bg-green-100 text-green-800 border-green-300';
      default:
        return 'bg-gray-100 text-gray-800 border-gray-300';
    }
  };

  return (
    <div className="space-y-6">
      <h2 className="text-2xl font-bold text-gray-900">Login History</h2>

      {/* Security Alert */}
      {suspiciousData && (
        <div className={`p-4 rounded-lg border-2 ${getRiskColor(suspiciousData.riskLevel)}`}>
          <div className="flex items-center justify-between">
            <div>
              <h3 className="text-lg font-semibold">Security Status: {suspiciousData.riskLevel}</h3>
              <p className="text-sm mt-1">
                {suspiciousData.uniqueIPsCount} unique IP address(es) detected in the last {suspiciousData.daysChecked} days
              </p>
            </div>
            {suspiciousData.riskLevel === 'HIGH' && (
              <div className="text-sm font-medium">
                ⚠️ Unusual activity detected
              </div>
            )}
          </div>
        </div>
      )}

      {/* Filter Section */}
      <div className="bg-white p-6 rounded-lg shadow-md">
        <div className="flex items-center gap-4">
          <label htmlFor="limit" className="text-sm font-medium text-gray-700">
            Show last:
          </label>
          <select
            id="limit"
            value={limit}
            onChange={(e) => setLimit(parseInt(e.target.value))}
            className="px-4 py-2 border border-gray-300 rounded-md focus:ring-primary-500 focus:border-primary-500"
          >
            <option value="10">10 logins</option>
            <option value="20">20 logins</option>
            <option value="50">50 logins</option>
            <option value="100">100 logins</option>
          </select>
          <button
            onClick={loadLoginHistory}
            className="px-4 py-2 bg-primary-600 text-white rounded-md hover:bg-primary-700"
          >
            Refresh
          </button>
        </div>
      </div>

      {/* Login History Table */}
      <div className="bg-white rounded-lg shadow-md overflow-hidden">
        {loading ? (
          <div className="text-center py-8">Loading...</div>
        ) : (
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Login Time
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Logout Time
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    IP Address
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Device
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    User Agent
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Status
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {loginHistory.map((history) => (
                  <tr key={history.historyId} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {new Date(history.loginTime).toLocaleString()}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {history.logoutTime ? new Date(history.logoutTime).toLocaleString() : '-'}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {history.ipAddress}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      <span className="capitalize">{history.device}</span>
                    </td>
                    <td className="px-6 py-4 text-sm text-gray-500 max-w-xs truncate">
                      {history.userAgent}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                        history.logoutTime
                          ? 'bg-gray-100 text-gray-800'
                          : 'bg-green-100 text-green-800'
                      }`}>
                        {history.logoutTime ? 'Ended' : 'Active'}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
            {loginHistory.length === 0 && (
              <div className="text-center py-8 text-gray-500">No login history found</div>
            )}
          </div>
        )}
      </div>

      {/* Session Duration Info */}
      {loginHistory.length > 0 && (
        <div className="bg-white p-6 rounded-lg shadow-md">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">Session Insights</h3>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div className="border-l-4 border-blue-500 pl-4">
              <p className="text-sm text-gray-600">Total Sessions</p>
              <p className="text-2xl font-bold text-gray-900">{loginHistory.length}</p>
            </div>
            <div className="border-l-4 border-green-500 pl-4">
              <p className="text-sm text-gray-600">Active Sessions</p>
              <p className="text-2xl font-bold text-gray-900">
                {loginHistory.filter(h => !h.logoutTime).length}
              </p>
            </div>
            <div className="border-l-4 border-purple-500 pl-4">
              <p className="text-sm text-gray-600">Unique Devices</p>
              <p className="text-2xl font-bold text-gray-900">
                {new Set(loginHistory.map(h => h.device)).size}
              </p>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default LoginHistoryView;
