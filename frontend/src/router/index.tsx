import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { ROUTES } from '../constants/routes';
import { useAuth } from '../contexts/AuthContext';
import { DashboardLayout } from '../components/layout/DashboardLayout/DashboardLayout';
import { LoginPage } from '../pages/Login/LoginPage';
import { DashboardPage } from '../pages/Dashboard/DashboardPage';
import { TransactionsPage } from '../pages/Transactions/TransactionsPage';
import { FinancialAnalysisPage } from '../pages/FinancialAnalysis/FinancialAnalysisPage';
import { AIPage } from '../pages/AI/AIPage';
import { ReportsPage } from '../pages/Reports/ReportsPage';
import { SettingsPage } from '../pages/Settings/SettingsPage';
import { NotFoundPage } from '../pages/NotFound/NotFoundPage';
import { Loader2 } from 'lucide-react';

const ProtectedRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { isAuthenticated, isLoading } = useAuth();

  if (isLoading) {
    return (
      <div className="min-h-screen bg-slate-900 flex flex-col items-center justify-center text-slate-100 gap-3">
        <Loader2 className="w-8 h-8 animate-spin text-blue-500" />
        <p className="text-xs font-semibold">Carregando credenciais...</p>
      </div>
    );
  }

  if (!isAuthenticated) {
    return <Navigate to={ROUTES.LOGIN} replace />;
  }

  return <>{children}</>;
};

export const AppRouter: React.FC = () => {
  return (
    <BrowserRouter>
      <Routes>
        {/* Public Route */}
        <Route path={ROUTES.LOGIN} element={<LoginPage />} />

        {/* Protected Dashboard Shell Routes */}
        <Route
          element={
            <ProtectedRoute>
              <DashboardLayout />
            </ProtectedRoute>
          }
        >
          <Route path={ROUTES.DASHBOARD} element={<DashboardPage />} />
          <Route path={ROUTES.TRANSACTIONS} element={<TransactionsPage />} />
          <Route path={ROUTES.ANALYSIS} element={<FinancialAnalysisPage />} />
          <Route path={ROUTES.AI} element={<AIPage />} />
          <Route path={ROUTES.REPORTS} element={<ReportsPage />} />
          <Route path={ROUTES.SETTINGS} element={<SettingsPage />} />
        </Route>

        {/* Root Redirect */}
        <Route path="/" element={<Navigate to={ROUTES.DASHBOARD} replace />} />

        {/* 404 Catch All */}
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
    </BrowserRouter>
  );
};
