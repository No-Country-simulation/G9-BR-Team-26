import React from 'react';
import { useNavigate } from 'react-router-dom';
import { ArrowLeft, Sparkles } from 'lucide-react';
import { Button } from '../../components/common/Button/Button';
import { ROUTES } from '../../constants/routes';

export const NotFoundPage: React.FC = () => {
  const navigate = useNavigate();

  return (
    <div className="min-h-[80vh] flex flex-col items-center justify-center text-center p-6 space-y-4">
      <div className="w-16 h-16 rounded-2xl bg-slate-100 dark:bg-slate-800 flex items-center justify-center text-blue-600 dark:text-blue-400">
        <Sparkles className="w-8 h-8" />
      </div>

      <h1 className="text-4xl font-extrabold text-slate-900 dark:text-slate-100 tracking-tight">404</h1>
      <h2 className="text-lg font-bold text-slate-800 dark:text-slate-200">Página Não Encontrada</h2>
      <p className="text-xs sm:text-sm text-slate-500 max-w-md">
        O endereço solicitado não existe ou foi movido para outra rota da plataforma SmartFinance.
      </p>

      <Button
        variant="primary"
        size="md"
        leftIcon={<ArrowLeft className="w-4 h-4" />}
        onClick={() => navigate(ROUTES.DASHBOARD)}
      >
        Voltar ao Dashboard
      </Button>
    </div>
  );
};
