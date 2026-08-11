import React, { useState } from 'react';
import { useDebtSimulation } from '../../hooks/useDebtSimulation';
import { Card, CardContent, CardHeader, CardTitle } from '../../components/common/Card/Card';
import { Button } from '../../components/common/Button/Button';
import { Input } from '../../components/common/Input/Input';
import { handleApiError } from '../../services/api/axios';
import { formatCurrency } from '../../utils/currency';

export const DebtSimulationPage: React.FC = () => {
  const { simulate, result, isSimulating, error } = useDebtSimulation();
  const [valorDivida, setValorDivida] = useState(''); const [valorMensal, setValorMensal] = useState(''); const [taxaJurosMensal, setTaxaJurosMensal] = useState('');
  const submit = async (event: React.FormEvent) => { event.preventDefault(); await simulate({ valorDivida: Number(valorDivida), valorMensal: Number(valorMensal), ...(taxaJurosMensal ? { taxaJurosMensal: Number(taxaJurosMensal) / 100 } : {}) }); };
  return <div className="space-y-6 max-w-3xl"><div><h1 className="text-2xl font-bold text-slate-900 dark:text-white">Simular quitação</h1><p className="text-sm text-slate-500">Estime o prazo de pagamento da sua dívida.</p></div>{error && <div className="rounded-xl bg-rose-50 border border-rose-300 p-4 text-sm text-rose-700">{handleApiError(error).message}</div>}<Card><CardHeader><CardTitle>Dados da simulação</CardTitle></CardHeader><CardContent><form onSubmit={submit} className="grid grid-cols-1 sm:grid-cols-3 gap-4 items-end"><Input label="Valor da dívida" type="number" min="0.01" step="0.01" required value={valorDivida} onChange={e => setValorDivida(e.target.value)} /><Input label="Pagamento mensal" type="number" min="0.01" step="0.01" required value={valorMensal} onChange={e => setValorMensal(e.target.value)} /><Input label="Juros mensais (%) — opcional" type="number" min="0" step="0.01" value={taxaJurosMensal} onChange={e => setTaxaJurosMensal(e.target.value)} /><Button type="submit" isLoading={isSimulating}>Simular</Button></form></CardContent></Card>{result && <Card><CardHeader><CardTitle>Resultado</CardTitle></CardHeader><CardContent className="grid grid-cols-1 sm:grid-cols-3 gap-4"><div><p className="text-xs text-slate-500">Prazo</p><p className="text-xl font-bold">{result.meses} meses</p></div><div><p className="text-xs text-slate-500">Total pago</p><p className="text-xl font-bold">{formatCurrency(result.valorTotalPago)}</p></div><div><p className="text-xs text-slate-500">Juros pagos</p><p className="text-xl font-bold">{formatCurrency(result.totalJurosPagos)}</p></div></CardContent></Card>}</div>;
};
