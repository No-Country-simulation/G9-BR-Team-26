import React, { useState } from 'react';
import { Loader2, RefreshCw } from 'lucide-react';
import { useAnalysis } from '../../hooks/useAnalysis';
import { Card, CardContent, CardHeader, CardTitle } from '../../components/common/Card/Card';
import { Button } from '../../components/common/Button/Button';
import { Input } from '../../components/common/Input/Input';
import { formatCurrency } from '../../utils/currency';
import { handleApiError } from '../../services/api/axios';

export const FinancialAnalysisPage: React.FC = () => {
  const { latestAnalysis, isLoading, isError, error, refetch, createAnalysis, isCreating, createError } = useAnalysis();
  const [rendaMensal, setRendaMensal] = useState('');
  const [nivelEndividamento, setNivelEndividamento] = useState('');
  const [frequenciaPoupanca, setFrequenciaPoupanca] = useState('Media');
  const submit = async (event: React.FormEvent) => {
    event.preventDefault();
    await createAnalysis({ rendaMensal: Number(rendaMensal), nivelEndividamento: Number(nivelEndividamento), frequenciaPoupanca });
  };
  const errorMessage = isError ? handleApiError(error).message : createError ? handleApiError(createError).message : '';

  return <div className="space-y-6 max-w-5xl mx-auto">
    <div><h1 className="text-2xl font-bold text-slate-900 dark:text-white">Análise Financeira</h1><p className="text-sm text-slate-500 mt-1">Gere um diagnóstico financeiro com base na análise inteligente do FinanceAI.</p></div>
    {errorMessage && <div className="rounded-xl border border-rose-300 bg-rose-50 p-4 text-sm text-rose-700">{errorMessage} {handleApiError(error || createError).retryable && <button onClick={() => refetch()} className="underline font-semibold ml-2">Tentar novamente</button>}</div>}
    <Card><CardHeader><CardTitle>Nova análise</CardTitle></CardHeader><CardContent>
      <form onSubmit={submit} className="grid grid-cols-1 sm:grid-cols-4 gap-4 items-end">
        <Input label="Renda mensal" type="number" min="0.01" step="0.01" required value={rendaMensal} onChange={e => setRendaMensal(e.target.value)} />
        <Input label="Endividamento (%)" type="number" min="0" max="100" required value={nivelEndividamento} onChange={e => setNivelEndividamento(e.target.value)} />
        <label className="text-xs font-semibold text-slate-700 dark:text-slate-300">Frequência de poupança<select value={frequenciaPoupanca} onChange={e => setFrequenciaPoupanca(e.target.value)} className="block mt-1.5 w-full rounded-lg border p-2.5 bg-white dark:bg-slate-900"><option>Baixa</option><option>Media</option><option>Alta</option></select></label>
        <Button type="submit" variant="primary" isLoading={isCreating}>Gerar análise</Button>
      </form>
    </CardContent></Card>
    {isLoading ? <div className="flex justify-center p-12"><Loader2 className="animate-spin text-blue-600" /></div> : !latestAnalysis ? <Card><CardContent className="p-10 text-center text-slate-500">Nenhuma análise foi gerada ainda.</CardContent></Card> : <>
      <div className="grid grid-cols-1 sm:grid-cols-4 gap-4">
        <Card><CardContent className="p-5"><p className="text-xs text-slate-500">Renda mensal</p><p className="text-xl font-bold">{formatCurrency(latestAnalysis.rendaMensal)}</p></CardContent></Card>
        <Card><CardContent className="p-5"><p className="text-xs text-slate-500">Endividamento</p><p className="text-xl font-bold">{latestAnalysis.nivelEndividamento}%</p></CardContent></Card>
        <Card><CardContent className="p-5"><p className="text-xs text-slate-500">Perfil</p><p className="text-xl font-bold">{latestAnalysis.perfilFinanceiro}</p></CardContent></Card>
        <Card><CardContent className="p-5"><p className="text-xs text-slate-500">Probabilidade</p><p className="text-xl font-bold">{Math.round(latestAnalysis.probabilidade * 100)}%</p></CardContent></Card>
      </div>
      <Card><CardHeader><CardTitle>Recomendações</CardTitle></CardHeader><CardContent>{latestAnalysis.recomendacoes.length ? <ul className="space-y-3">{latestAnalysis.recomendacoes.map((item, index) => <li key={index} className="rounded-lg bg-slate-50 dark:bg-slate-800 p-3 text-sm">{item}</li>)}</ul> : <p className="text-sm text-slate-500">A API não retornou recomendações para esta análise.</p>}</CardContent></Card>
    </>}
  </div>;
};
