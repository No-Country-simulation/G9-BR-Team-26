import { Transaction, TransactionCategory } from './transaction';

export interface MonthlyComparison {
  percentage: number;
  isPositive: boolean;
  label: string;
}

export interface MetricCardData {
  title: string;
  value: number;
  formattedValue: string;
  comparison: MonthlyComparison;
  iconName: string;
  type: 'balance' | 'income' | 'expense' | 'savings';
}

export interface CashFlowPoint {
  month: string;
  receitas: number;
  despesas: number;
  saldo: number;
}

export interface ExpenseCategoryPoint {
  category: TransactionCategory;
  amount: number;
  percentage: number;
  color: string;
}

export interface AIRecommendation {
  id: string;
  title: string;
  description: string;
  priority: 'ALTA' | 'MÉDIA' | 'BAIXA';
  category: string;
  estimatedImpact: string;
  actionLabel?: string;
  actionUrl?: string;
}

export interface DashboardSummary {
  totalTransactions: number;
  balance: number;
  income: number;
  expenses: number;
  savings: number;
  monthlyComparison: {
    balance: MonthlyComparison;
    income: MonthlyComparison;
    expenses: MonthlyComparison;
    savings: MonthlyComparison;
  };
  cashFlow: CashFlowPoint[];
  expenseCategories: ExpenseCategoryPoint[];
  recentTransactions: Transaction[];
  aiRecommendations: AIRecommendation[];
}
