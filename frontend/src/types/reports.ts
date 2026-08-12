import { TransactionCategory } from './transaction';

export interface MonthlyReportSummary {
  period: string; // e.g. "Agosto 2026"
  totalIncome: number;
  totalExpenses: number;
  netSavings: number;
  savingsRate: number;
  topExpenseCategory: TransactionCategory;
  largestExpense: {
    description: string;
    amount: number;
    date: string;
  };
  categoryBreakdown: Array<{
    category: TransactionCategory;
    totalAmount: number;
    transactionCount: number;
    percentageOfTotal: number;
  }>;
}

export interface YearlyReportSummary {
  year: number;
  annualIncome: number;
  annualExpenses: number;
  annualSavings: number;
  averageMonthlyExpense: number;
  highestSpendingMonth: {
    month: string;
    amount: number;
  };
  lowestSpendingMonth: {
    month: string;
    amount: number;
  };
  monthlyEvolution: Array<{
    month: string;
    income: number;
    expenses: number;
    savings: number;
  }>;
}

export interface ReportFilter {
  periodType: 'MONTHLY' | 'YEARLY' | 'CUSTOM';
  selectedYear: number;
  selectedMonth?: number;
  startDate?: string;
  endDate?: string;
  category?: string;
}

export type ExportFormat = 'PDF' | 'EXCEL' | 'CSV';
