import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Sun, Moon, Menu, Search } from 'lucide-react';
import { useAuth } from '../../../contexts/AuthContext';
import { useTheme } from '../../../contexts/ThemeContext';
import { ROUTES } from '../../../constants/routes';

interface HeaderProps { onOpenMobileMenu: () => void; title?: string; sidebarCollapsed?: boolean; onToggleSidebar?: () => void; }

export const Header: React.FC<HeaderProps> = ({ onOpenMobileMenu, sidebarCollapsed, onToggleSidebar }) => {
  const { user } = useAuth(); const { theme, toggleTheme } = useTheme(); const navigate = useNavigate(); const [search, setSearch] = useState('');
  const submit = (e: React.FormEvent) => { e.preventDefault(); if (search.trim()) navigate(`${ROUTES.TRANSACTIONS}?search=${encodeURIComponent(search.trim())}`); };
  return <header className="h-16 bg-white dark:bg-slate-900 border-b border-slate-200 dark:border-slate-800 px-4 sm:px-8 flex items-center justify-between sticky top-0 z-30"><div className="flex items-center gap-3 flex-1 max-w-lg"><button onClick={onOpenMobileMenu} className="lg:hidden p-2" aria-label="Abrir menu"><Menu className="w-5 h-5" /></button>{onToggleSidebar && <button onClick={onToggleSidebar} className="hidden lg:block p-2" title={sidebarCollapsed ? 'Expandir menu lateral' : 'Recolher menu lateral'}><Menu className="w-5 h-5" /></button>}<form onSubmit={submit} className="relative hidden sm:block w-full"><Search className="w-4 h-4 text-slate-400 absolute left-3 top-2.5"/><input value={search} onChange={e => setSearch(e.target.value)} placeholder="Pesquisar transações..." className="w-full rounded-full bg-slate-100 dark:bg-slate-800 pl-9 pr-4 py-2 text-xs"/></form></div><div className="flex items-center gap-3"><button onClick={toggleTheme} className="p-2" title="Alternar tema">{theme === 'light' ? <Moon className="w-4 h-4"/> : <Sun className="w-4 h-4"/>}</button><button onClick={() => navigate(ROUTES.SETTINGS)} className="flex items-center gap-2 text-xs font-semibold"><span className="w-7 h-7 rounded-full bg-blue-100 text-blue-700 flex items-center justify-center">{user?.name?.slice(0, 2).toUpperCase() || 'US'}</span><span className="hidden md:inline">{user?.name || 'Usuário'}</span></button></div></header>;
};
