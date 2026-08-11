import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import {
  LayoutDashboard,
  Receipt,
  PlusCircle,
  FileSpreadsheet,
  LineChart,
  History,
  User,
  LogOut,
  Bot,
  X,
  Menu
} from 'lucide-react';
import { ROUTES } from '../../../constants/routes';
import { useAuth } from '../../../contexts/AuthContext';
import { cn } from '../../../lib/utils';

interface SidebarProps {
  mobileOpen?: boolean;
  onCloseMobile?: () => void;
  onOpenNewTx?: () => void;
  onOpenImportCSV?: () => void;
  collapsed?: boolean;
  onToggleCollapse?: () => void;
}

export const Sidebar: React.FC<SidebarProps> = ({
  mobileOpen,
  onCloseMobile,
  onOpenNewTx,
  onOpenImportCSV,
  collapsed = false,
  onToggleCollapse
}) => {
  const { logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = async () => {
    await logout();
    navigate(ROUTES.LOGIN);
  };

  const isDashboardActive = location.pathname === ROUTES.DASHBOARD;
  const isTransactionsActive = location.pathname === ROUTES.TRANSACTIONS && !location.search.includes('tab=import') && !location.search.includes('new=true');
  const isNewTxActive = location.pathname === ROUTES.TRANSACTIONS && location.search.includes('new=true');
  const isImportCSVActive = location.pathname === ROUTES.TRANSACTIONS && location.search.includes('tab=import');
  const isAIActive = location.pathname === ROUTES.AI;
  const isAnalysisActive = location.pathname === ROUTES.ANALYSIS;
  const isHistoryActive = location.pathname === ROUTES.REPORTS;
  const isProfileActive = location.pathname === ROUTES.SETTINGS;

  const navItems = [
    { label: 'Dashboard', icon: LayoutDashboard, path: ROUTES.DASHBOARD, isActive: isDashboardActive },
    { label: 'Transações', icon: Receipt, path: ROUTES.TRANSACTIONS, isActive: isTransactionsActive },
    {
      label: 'Nova Transação',
      icon: PlusCircle,
      path: `${ROUTES.TRANSACTIONS}?new=true`,
      isActive: isNewTxActive,
      action: () => {
        if (onOpenNewTx) onOpenNewTx();
        else navigate(`${ROUTES.TRANSACTIONS}?new=true`);
      }
    },
    {
      label: 'Importar CSV',
      icon: FileSpreadsheet,
      path: `${ROUTES.TRANSACTIONS}?tab=import`,
      isActive: isImportCSVActive,
      action: () => {
        if (onOpenImportCSV) onOpenImportCSV();
        else navigate(`${ROUTES.TRANSACTIONS}?tab=import`);
      }
    },
    { label: 'Chatbot IA', icon: Bot, path: ROUTES.AI, isActive: isAIActive },
    { label: 'Análise Financeira', icon: LineChart, path: ROUTES.ANALYSIS, isActive: isAnalysisActive },
    { label: 'Histórico', icon: History, path: ROUTES.REPORTS, isActive: isHistoryActive },
    { label: 'Perfil', icon: User, path: ROUTES.SETTINGS, isActive: isProfileActive },
  ];

  return (
    <>
      {/* Mobile Backdrop */}
      {mobileOpen && (
        <div
          onClick={onCloseMobile}
          className="fixed inset-0 bg-slate-900/50 z-40 lg:hidden backdrop-blur-xs transition-opacity"
        />
      )}

      <aside
        className={cn(
          'fixed lg:sticky top-0 left-0 z-40 h-screen bg-[#1E52D2] dark:bg-slate-900 text-white flex flex-col justify-between transition-all duration-300 ease-in-out shrink-0 shadow-lg select-none',
          collapsed ? 'lg:w-20' : 'lg:w-64',
          'w-64',
          mobileOpen ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'
        )}
      >
        <div className="flex flex-col h-full justify-between">
          <div>
            {/* Brand Logo & Toggle Header */}
            <div className={cn(
              "h-20 flex items-center border-b border-white/10 dark:border-slate-800 transition-all duration-300",
              collapsed ? "px-3 justify-center" : "px-5 justify-between"
            )}>
              <div className="flex items-center gap-3 overflow-hidden">
                {/* Modern Geometric Hexagon/Cube Logo or Toggle Icon */}
                {collapsed && onToggleCollapse ? (
                  <button
                    onClick={onToggleCollapse}
                    className="w-10 h-10 rounded-xl bg-white/15 hover:bg-white/25 border border-white/20 flex items-center justify-center text-white transition-colors shrink-0"
                    title="Abrir / Expandir menu lateral"
                    aria-label="Abrir menu lateral"
                  >
                    <Menu className="w-5 h-5 text-white" />
                  </button>
                ) : (
                  <div className="w-10 h-10 rounded-xl bg-white/15 backdrop-blur-md border border-white/20 flex items-center justify-center text-white shadow-inner shrink-0">
                    <svg className="w-5 h-5 text-white" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.2" strokeLinecap="round" strokeLinejoin="round">
                      <polygon points="12 2 2 7 12 12 22 7 12 2" />
                      <polyline points="2 17 12 22 22 17" />
                      <polyline points="2 12 12 17 22 12" />
                    </svg>
                  </div>
                )}

                {!collapsed && (
                  <div className="transition-opacity duration-200 whitespace-nowrap">
                    <span className="font-extrabold text-lg text-white tracking-tight block leading-tight">
                      SmartFinance
                    </span>
                    <span className="text-[11px] font-medium text-blue-200/80 dark:text-slate-400 block">
                      Oracle X NoCountry
                    </span>
                  </div>
                )}
              </div>

              {/* Close button on Mobile */}
              {onCloseMobile && (
                <button
                  onClick={onCloseMobile}
                  className="lg:hidden p-1.5 rounded-lg text-blue-200 hover:text-white hover:bg-white/10 dark:hover:bg-slate-800"
                >
                  <X className="w-5 h-5" />
                </button>
              )}

              {/* Collapse button on Desktop when expanded */}
              {!collapsed && onToggleCollapse && (
                <button
                  onClick={onToggleCollapse}
                  className="hidden lg:flex p-2 rounded-xl text-blue-200 hover:text-white hover:bg-white/10 dark:hover:bg-slate-800 transition-colors"
                  title="Recolher menu lateral (apenas ícones)"
                  aria-label="Recolher menu"
                >
                  <Menu className="w-5 h-5" />
                </button>
              )}
            </div>

            {/* Sidebar Links */}
            <nav className={cn('space-y-1.5 mt-3 transition-all duration-300', collapsed ? 'px-2' : 'px-3.5')}>
              {navItems.map((item, idx) => {
                const Icon = item.icon;
                const active = item.isActive;

                const activeClasses = active
                  ? 'bg-white/20 dark:bg-slate-800 text-white font-semibold shadow-xs border border-white/20 dark:border-slate-700'
                  : 'text-blue-100/80 dark:text-slate-400 hover:text-white dark:hover:text-white hover:bg-white/10 dark:hover:bg-slate-800/60';

                return (
                  <button
                    key={idx}
                    title={collapsed ? item.label : undefined}
                    onClick={() => {
                      if (onCloseMobile) onCloseMobile();
                      if (item.action) {
                        item.action();
                      } else {
                        navigate(item.path);
                      }
                    }}
                    className={cn(
                      'w-full flex items-center rounded-xl text-xs font-medium transition-all duration-200 group',
                      collapsed ? 'justify-center p-3' : 'px-3.5 py-2.5 gap-3 text-left',
                      activeClasses
                    )}
                  >
                    <Icon className={cn(
                      'w-4 h-4 shrink-0 transition-transform group-hover:scale-110',
                      active ? 'text-white' : 'text-blue-200/80 dark:text-slate-400'
                    )} />
                    {!collapsed && (
                      <span className="truncate whitespace-nowrap">{item.label}</span>
                    )}
                  </button>
                );
              })}
            </nav>
          </div>

          {/* Bottom Actions */}
          <div className={cn('pb-4 space-y-2 transition-all duration-300', collapsed ? 'px-2' : 'px-3.5')}>
            <button
              onClick={handleLogout}
              title={collapsed ? 'Sair do sistema' : undefined}
              className={cn(
                'w-full flex items-center rounded-xl text-xs font-medium text-blue-100/80 dark:text-slate-400 hover:text-white hover:bg-white/10 dark:hover:bg-slate-800 transition-colors',
                collapsed ? 'justify-center p-3' : 'px-3.5 py-2.5 gap-3'
              )}
            >
              <LogOut className="w-4 h-4 text-blue-200/80 dark:text-slate-400 shrink-0" />
              {!collapsed && <span className="whitespace-nowrap">Sair do sistema</span>}
            </button>
          </div>
        </div>
      </aside>
    </>
  );
};
