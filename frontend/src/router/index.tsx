import React, { useEffect } from 'react';
import { Routes, Route, Navigate, useLocation, useNavigate } from 'react-router-dom';
import { ROUTES } from '../constants/routes';
import { useAuth } from '../contexts/AuthContext';
import { UNAUTHORIZED_EVENT } from '../services/api/session';
import { DashboardLayout } from '../components/layout/DashboardLayout/DashboardLayout';
import { LoginPage } from '../pages/Login/LoginPage';
import { DashboardPage } from '../pages/Dashboard/DashboardPage';
import { TransactionsPage } from '../pages/Transactions/TransactionsPage';
import { FinancialAnalysisPage } from '../pages/FinancialAnalysis/FinancialAnalysisPage';
import { AIPage } from '../pages/AI/AIPage';
import { ReportsPage } from '../pages/Reports/ReportsPage';
import { SettingsPage } from '../pages/Settings/SettingsPage';
import { GoalsPage } from '../pages/Goals/GoalsPage';
import { DebtSimulationPage } from '../pages/DebtSimulation/DebtSimulationPage';
import { NotFoundPage } from '../pages/NotFound/NotFoundPage';
import { ApiErrorPage } from '../pages/ApiError/ApiErrorPage';
import { Loader2 } from 'lucide-react';

const ProtectedRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { isAuthenticated, isLoading } = useAuth();
  const location = useLocation();
  if (isLoading) return <div className="min-h-screen bg-slate-900 flex flex-col items-center justify-center text-slate-100 gap-3"><Loader2 className="w-8 h-8 animate-spin text-blue-500" /><p className="text-xs font-semibold">Carregando credenciais...</p></div>;
  if (!isAuthenticated) return <Navigate to={`${ROUTES.LOGIN}?redirect=${encodeURIComponent(location.pathname + location.search)}`} replace />;
  return <>{children}</>;
};

/** Bridges the HTTP interceptor and router, so a mid-session 401 always navigates. */
const SessionRedirector: React.FC = () => {
  const navigate = useNavigate(); const location = useLocation();
  useEffect(() => {
    const redirectToLogin = () => {
      if (location.pathname === ROUTES.LOGIN) return;
      navigate(`${ROUTES.LOGIN}?redirect=${encodeURIComponent(location.pathname + location.search)}`, { replace: true });
    };
    window.addEventListener(UNAUTHORIZED_EVENT, redirectToLogin);
    return () => window.removeEventListener(UNAUTHORIZED_EVENT, redirectToLogin);
  }, [location.pathname, location.search, navigate]);
  return null;
};

export const AppRouter: React.FC = () => <><SessionRedirector /><Routes>
  <Route path={ROUTES.LOGIN} element={<LoginPage />} />
  <Route path="/signup" element={<LoginPage />} />
  <Route path="/erro" element={<ApiErrorPage />} />
  <Route element={<ProtectedRoute><DashboardLayout /></ProtectedRoute>}>
    <Route path={ROUTES.DASHBOARD} element={<DashboardPage />} /><Route path={ROUTES.TRANSACTIONS} element={<TransactionsPage />} />
    <Route path={ROUTES.GOALS} element={<GoalsPage />} /><Route path={ROUTES.DEBT_SIMULATION} element={<DebtSimulationPage />} />
    <Route path={ROUTES.ANALYSIS} element={<FinancialAnalysisPage />} /><Route path={ROUTES.AI} element={<AIPage />} />
    <Route path={ROUTES.REPORTS} element={<ReportsPage />} /><Route path={ROUTES.SETTINGS} element={<SettingsPage />} />
  </Route>
  <Route path="/" element={<Navigate to={ROUTES.DASHBOARD} replace />} /><Route path="*" element={<NotFoundPage />} />
</Routes></>;
