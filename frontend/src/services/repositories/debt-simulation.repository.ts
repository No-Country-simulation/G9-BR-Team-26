import { api } from '../api/axios';

export interface DebtSimulation { valorDivida: number; valorMensal: number; taxaJurosMensal: number | null; meses: number; valorTotalPago: number; totalJurosPagos: number; }

export const debtSimulationRepository = {
  simulate: async (data: { valorDivida: number; valorMensal: number; taxaJurosMensal?: number }): Promise<DebtSimulation> => (await api.post<DebtSimulation>('/simulacao/quitacao', data)).data,
};
