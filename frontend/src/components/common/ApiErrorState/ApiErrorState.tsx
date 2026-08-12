import React from 'react';
import { AlertTriangle, Home, RefreshCw } from 'lucide-react';
import { Link } from 'react-router-dom';
import { ROUTES } from '../../../constants/routes';

interface ApiErrorStateProps { onRetry?: () => void | Promise<unknown>; fullPage?: boolean; }
export const ApiErrorState: React.FC<ApiErrorStateProps> = ({ onRetry, fullPage = false }) => (
  <div className={`${fullPage ? 'min-h-screen' : 'min-h-[16rem] rounded-2xl'} flex flex-col items-center justify-center gap-4 bg-slate-50 p-8 text-center dark:bg-slate-900`}>
    <AlertTriangle className="h-10 w-10 text-amber-500" aria-hidden="true" />
    <div><h1 className="text-lg font-bold text-slate-900 dark:text-white">Ops, algo não funcionou.</h1><p className="mt-1 text-sm text-slate-500">Tente novamente mais tarde.</p></div>
    <div className="flex flex-wrap justify-center gap-3">
      {onRetry && <button onClick={() => void onRetry()} className="inline-flex items-center gap-2 rounded-lg bg-blue-600 px-4 py-2 text-sm font-semibold text-white hover:bg-blue-500"><RefreshCw className="h-4 w-4" />Tentar novamente</button>}
      <Link to={ROUTES.DASHBOARD} className="inline-flex items-center gap-2 rounded-lg border border-slate-300 px-4 py-2 text-sm font-semibold text-slate-700 hover:bg-white dark:border-slate-700 dark:text-slate-200"><Home className="h-4 w-4" />Voltar ao Dashboard</Link>
    </div>
  </div>
);
