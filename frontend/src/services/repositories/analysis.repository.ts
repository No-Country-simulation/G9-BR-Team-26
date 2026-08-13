import { api } from '../api/axios';
import { SingleAnalysisData } from '../../types/analysis';

interface BackendAnalysis {
  id?: number;
  criado_em?: string;
  renda_mensal?: number;
  nivel_endividamento?: number;
  frequencia_poupanca?: string;
  perfil_financeiro: string;
  probabilidade: number;
  score?: number;
  recomendacoes: string[];
}

const mapAnalysis = (analysis: BackendAnalysis): SingleAnalysisData => ({
  id: analysis.id ?? 0,
  criadoEm: analysis.criado_em ?? new Date().toISOString(),
  rendaMensal: Number(analysis.renda_mensal ?? 0),
  nivelEndividamento: Number(analysis.nivel_endividamento ?? 0),
  frequenciaPoupanca: analysis.frequencia_poupanca ?? '',
  perfilFinanceiro: analysis.perfil_financeiro,
  probabilidade: Number(analysis.probabilidade ?? 0),
  recomendacoes: analysis.recomendacoes ?? [],
});

export const analysisRepository = {
  createAnalysis: async (data: { rendaMensal: number; nivelEndividamento: number; frequenciaPoupanca: string }): Promise<SingleAnalysisData> => {
    const response = await api.post<BackendAnalysis>('/analise-financeira', data);
    return mapAnalysis({ ...response.data, renda_mensal: data.rendaMensal, nivel_endividamento: data.nivelEndividamento, frequencia_poupanca: data.frequenciaPoupanca });
  },
  getHistory: async (): Promise<SingleAnalysisData[]> => {
    const response = await api.get<BackendAnalysis[]>('/analise-financeira/historico');
    return (response.data ?? []).map(mapAnalysis);
  },
  getById: async (id: number): Promise<SingleAnalysisData> => {
    const response = await api.get<BackendAnalysis>(`/analise-financeira/${id}`);
    return mapAnalysis(response.data);
  },
};
