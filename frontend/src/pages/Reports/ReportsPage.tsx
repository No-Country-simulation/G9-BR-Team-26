import React, { useState } from 'react';
import { Loader2 } from 'lucide-react';
import { useAnalysis } from '../../hooks/useAnalysis';
import { Card, CardContent } from '../../components/common/Card/Card';
import { Modal } from '../../components/common/Modal/Modal';
import { Button } from '../../components/common/Button/Button';
import { formatCurrency } from '../../utils/currency';
import { formatDate } from '../../utils/date';
import { SingleAnalysisData } from '../../types/analysis';
import { handleApiError } from '../../services/api/axios';

export const ReportsPage: React.FC = () => {
  const { history, isLoading, isError, error, refetch } = useAnalysis();
  const [selected, setSelected] = useState<SingleAnalysisData | null>(null);
  if (isLoading) return <div className="flex justify-center p-12"><Loader2 className="animate-spin text-blue-600" /></div>;
  if (isError) return <div className="rounded-xl bg-rose-50 border border-rose-300 p-4 text-rose-700">{handleApiError(error).message}<button onClick={() => refetch()} className="ml-2 underline">Tentar novamente</button></div>;
  return <div className="space-y-6"><div><h1 className="text-2xl font-bold text-slate-900 dark:text-white">Histórico de análises</h1><p className="text-sm text-slate-500">Dados obtidos de /analise-financeira/historico.</p></div>
    <Card><CardContent className="p-0">{history.length === 0 ? <div className="p-12 text-center text-slate-500">Nenhuma análise registrada ainda.</div> : <div className="overflow-x-auto"><table className="w-full text-sm"><thead className="text-left text-slate-500 border-b"><tr><th className="p-4">Data</th><th className="p-4">Renda</th><th className="p-4">Endividamento</th><th className="p-4">Perfil</th><th className="p-4">Probabilidade</th><th className="p-4" /></tr></thead><tbody>{history.map(item => <tr key={item.id} className="border-b border-slate-100 dark:border-slate-800"><td className="p-4">{formatDate(item.criadoEm)}</td><td className="p-4">{formatCurrency(item.rendaMensal)}</td><td className="p-4">{item.nivelEndividamento}%</td><td className="p-4">{item.perfilFinanceiro}</td><td className="p-4">{Math.round(item.probabilidade * 100)}%</td><td className="p-4"><button onClick={() => setSelected(item)} className="text-blue-600 font-semibold">Ver detalhe</button></td></tr>)}</tbody></table></div>}</CardContent></Card>
    {selected && <Modal isOpen onClose={() => setSelected(null)} title={`Análise #${selected.id}`} description={formatDate(selected.criadoEm)}><div className="space-y-3 text-sm"><p>Frequência de poupança: <strong>{selected.frequenciaPoupanca}</strong></p><p>Renda mensal: <strong>{formatCurrency(selected.rendaMensal)}</strong></p><p>Endividamento: <strong>{selected.nivelEndividamento}%</strong></p><div><p className="font-semibold mb-2">Recomendações</p>{selected.recomendacoes.length ? <ul className="list-disc pl-5 space-y-1">{selected.recomendacoes.map((r, i) => <li key={i}>{r}</li>)}</ul> : <p className="text-slate-500">Sem recomendações retornadas.</p>}</div><Button variant="outline" size="sm" onClick={() => setSelected(null)}>Fechar</Button></div></Modal>}
  </div>;
};
