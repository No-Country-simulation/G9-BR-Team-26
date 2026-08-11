import React from 'react';
import { Loader2 } from 'lucide-react';
import { useDashboard } from '../../hooks/useDashboard';
import { CategoryBarChart } from '../../components/charts/BarChart/CategoryBarChart';
import { Card, CardContent, CardHeader, CardTitle } from '../../components/common/Card/Card';
import { formatCurrency } from '../../utils/currency';
import { formatRelativeDate } from '../../utils/date';
import { handleApiError } from '../../services/api/axios';

export const DashboardPage: React.FC = () => {
  const { summary, isLoading, isError, error, refetch } = useDashboard();
  if (isLoading) return <div className="flex justify-center p-12"><Loader2 className="animate-spin text-blue-600" /></div>;
  if (isError) return <div className="rounded-xl bg-rose-50 border border-rose-300 p-4 text-rose-700">{handleApiError(error).message}<button onClick={() => refetch()} className="ml-2 underline">Tentar novamente</button></div>;
  if (!summary) return null;
  const categoryData = summary.expenseCategories.map(item => ({ category: item.category, spent: item.amount, color: item.color }));
  const topCategory = summary.expenseCategories.sort((a, b) => b.amount - a.amount)[0];
  return <div className="space-y-6"><div><h1 className="text-2xl font-bold text-slate-900 dark:text-white">Visão geral</h1><p className="text-sm text-slate-500">Resumo calculado localmente a partir das suas transações.</p></div>
    <div className="grid grid-cols-1 sm:grid-cols-3 gap-4"><Card><CardContent className="p-5"><p className="text-xs text-slate-500">Transações registradas</p><p className="text-2xl font-bold">{summary.totalTransactions}</p></CardContent></Card><Card><CardContent className="p-5"><p className="text-xs text-slate-500">Valor total registrado</p><p className="text-2xl font-bold">{formatCurrency(summary.expenses)}</p></CardContent></Card><Card><CardContent className="p-5"><p className="text-xs text-slate-500">Categoria principal</p><p className="text-2xl font-bold">{topCategory?.category ?? '—'}</p></CardContent></Card></div>
    <div className="grid grid-cols-1 lg:grid-cols-3 gap-6"><Card className="lg:col-span-2"><CardHeader><CardTitle>Transações por categoria</CardTitle></CardHeader><CardContent>{categoryData.length ? <CategoryBarChart data={categoryData} /> : <p className="py-20 text-center text-sm text-slate-500">Cadastre transações para visualizar a distribuição por categoria.</p>}</CardContent></Card><Card><CardHeader><CardTitle>Transações recentes</CardTitle></CardHeader><CardContent>{summary.recentTransactions.length ? <ul className="space-y-4">{summary.recentTransactions.map(item => <li key={item.id} className="flex justify-between gap-3 text-sm"><div><p className="font-medium">{item.description}</p><p className="text-xs text-slate-500">{item.category} · {formatRelativeDate(item.date)}</p></div><strong>{formatCurrency(item.amount)}</strong></li>)}</ul> : <p className="text-sm text-slate-500">Ainda não há transações.</p>}</CardContent></Card></div>
  </div>;
};
