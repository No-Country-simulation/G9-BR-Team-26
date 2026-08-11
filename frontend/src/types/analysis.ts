export interface SingleAnalysisData {
  id: number;
  criadoEm: string;
  rendaMensal: number;
  nivelEndividamento: number;
  frequenciaPoupanca: string;
  perfilFinanceiro: string;
  probabilidade: number;
  recomendacoes: string[];
}

export interface HealthScore {
  score: number;
  status: 'EXCELENTE' | 'BOM' | 'EM_OBSERVACAO' | 'RITMO_CRITICO';
  statusColor: string;
  summary: string;
}

export interface RiskFactor {
  id: string;
  factor: string;
  severity: 'ALTA' | 'MÉDIA' | 'BAIXA';
  recommendation: string;
}

export interface CategoryAnalysis {
  category: string;
  budgeted: number;
  spent: number;
  percentageUsed: number;
  status: 'DENTRO' | 'ALERTA' | 'EXCEDIDO';
  aiForecast: number;
}

export interface AIInsight {
  id: string;
  type: 'ALERT' | 'OPPORTUNITY' | 'FORECAST' | 'PATTERN';
  title: string;
  description: string;
  impactValue?: number;
  confidenceScore: number;
  createdAt: string;
}

export interface FinancialHealth {
  healthScore: HealthScore;
  financialProfile: string;
  goalProbability: number;
  savingsRate: number;
  fixedVsVariableRatio: {
    fixedPercentage: number;
    variablePercentage: number;
  };
  debtToIncomeRatio: number;
  categoriesAnalysis: CategoryAnalysis[];
  riskFactors: RiskFactor[];
  insights: AIInsight[];
}
