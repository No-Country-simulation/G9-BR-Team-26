import React, { useState } from 'react';
import { Loader2 } from 'lucide-react';
import { useGoals } from '../../hooks/useGoals';
import { Card, CardContent, CardHeader, CardTitle } from '../../components/common/Card/Card';
import { Button } from '../../components/common/Button/Button';
import { Input } from '../../components/common/Input/Input';
import { handleApiError } from '../../services/api/axios';
import { formatDate } from '../../utils/date';

export const GoalsPage: React.FC = () => {
  const { goals, isLoading, error, refetch, createGoal, isCreating, createError } = useGoals();
  const [descricao, setDescricao] = useState(''); const [endividamentoAlvo, setEndividamentoAlvo] = useState(''); const [dataAlvo, setDataAlvo] = useState('');
  const submit = async (event: React.FormEvent) => { event.preventDefault(); await createGoal({ descricao, endividamentoAlvo: Number(endividamentoAlvo), dataAlvo }); setDescricao(''); setEndividamentoAlvo(''); setDataAlvo(''); };
  const currentError = error || createError;
  return <div className="space-y-6 max-w-5xl"><div><h1 className="text-2xl font-bold text-slate-900 dark:text-white">Metas financeiras</h1><p className="text-sm text-slate-500">Acompanhe metas de endividamento.</p></div>{currentError && <div className="rounded-xl bg-rose-50 border border-rose-300 p-4 text-sm text-rose-700">{handleApiError(currentError).message}{error && <button onClick={() => refetch()} className="ml-2 underline">Tentar novamente</button>}</div>}<Card><CardHeader><CardTitle>Nova meta</CardTitle></CardHeader><CardContent><form onSubmit={submit} className="grid grid-cols-1 sm:grid-cols-4 gap-4 items-end"><Input label="Descrição" required value={descricao} onChange={e => setDescricao(e.target.value)} /><Input label="Endividamento alvo (%)" type="number" min="0" max="100" required value={endividamentoAlvo} onChange={e => setEndividamentoAlvo(e.target.value)} /><Input label="Data alvo" type="date" required value={dataAlvo} onChange={e => setDataAlvo(e.target.value)} /><Button type="submit" isLoading={isCreating}>Criar meta</Button></form></CardContent></Card>{isLoading ? <div className="flex justify-center p-10"><Loader2 className="animate-spin text-blue-600" /></div> : <Card><CardContent className="p-0">{goals.length === 0 ? <p className="p-10 text-center text-slate-500">Nenhuma meta cadastrada.</p> : <div className="overflow-x-auto"><table className="w-full text-sm"><thead className="text-left text-slate-500 border-b"><tr><th className="p-4">Meta</th><th className="p-4">Alvo</th><th className="p-4">Atual</th><th className="p-4">Progresso</th><th className="p-4">Data alvo</th></tr></thead><tbody>{goals.map(goal => <tr key={goal.id} className="border-b border-slate-100 dark:border-slate-800"><td className="p-4 font-medium">{goal.descricao}</td><td className="p-4">{goal.endividamentoAlvo}%</td><td className="p-4">{goal.endividamentoAtual === null ? 'Sem análise' : `${goal.endividamentoAtual}%`}</td><td className="p-4">{goal.progressoPercentual === null ? '—' : `${Math.round(goal.progressoPercentual)}%`}</td><td className="p-4">{formatDate(goal.dataAlvo)}</td></tr>)}</tbody></table></div>}</CardContent></Card>}</div>;
};
