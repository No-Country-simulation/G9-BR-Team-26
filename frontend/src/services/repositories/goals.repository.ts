import { api } from '../api/axios';

export interface FinancialGoal {
  id: number;
  descricao: string;
  endividamentoAlvo: number;
  endividamentoAtual: number | null;
  dataAlvo: string;
  criadoEm: string;
  concluida: boolean;
  progressoPercentual: number | null;
}

interface BackendGoal {
  id: number;
  descricao: string;
  endividamento_alvo: number;
  endividamento_atual: number | null;
  data_alvo: string;
  criado_em: string;
  concluida: boolean;
  progresso_percentual: number | null;
}

const mapGoal = (goal: BackendGoal): FinancialGoal => ({ id: goal.id, descricao: goal.descricao, endividamentoAlvo: goal.endividamento_alvo, endividamentoAtual: goal.endividamento_atual, dataAlvo: goal.data_alvo, criadoEm: goal.criado_em, concluida: goal.concluida, progressoPercentual: goal.progresso_percentual });

export const goalsRepository = {
  getAll: async (): Promise<FinancialGoal[]> => (await api.get<BackendGoal[]>('/metas')).data.map(mapGoal),
  create: async (data: { descricao: string; endividamentoAlvo: number; dataAlvo: string }): Promise<FinancialGoal> => mapGoal((await api.post<BackendGoal>('/metas', data)).data),
};
