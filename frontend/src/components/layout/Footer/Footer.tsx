import React from 'react';
import { Shield } from 'lucide-react';

export const Footer: React.FC = () => {
  return (
    <footer className="mt-auto border-t border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-900 py-4 px-6 text-xs text-slate-500 dark:text-slate-400 transition-colors">
      <div className="flex flex-col sm:flex-row items-center justify-between gap-3 max-w-7xl mx-auto">
        <div className="flex flex-wrap items-center gap-2">
          <span className="font-bold text-slate-800 dark:text-slate-200">FinanceAI - Seu sistema inteligente de finanças</span>
          <span className="text-slate-400 dark:text-slate-600">•</span>
          <span className="text-slate-600 dark:text-slate-400 font-medium">
            Com apoio <strong className="text-slate-800 dark:text-slate-200">Oracle</strong> X <strong className="text-slate-800 dark:text-slate-200">NoCountry</strong>
          </span>
        </div>

        <div className="flex items-center gap-3 text-[11px] text-slate-500 dark:text-slate-400">
          <span className="text-slate-300 dark:text-slate-700">•</span>
          <span>© 2026 Todos os direitos reservados</span>
        </div>
      </div>
    </footer>
  );
};
