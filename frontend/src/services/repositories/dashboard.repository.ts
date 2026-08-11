import { transactionRepository } from './transaction.repository';
import { DashboardSummary } from '../../types/dashboard';

export const dashboardRepository = {
  getSummary: async (): Promise<DashboardSummary> => {
    const transactions = await transactionRepository.getTransactions();
    const categories = new Map<string, number>();
    transactions.forEach(transaction => categories.set(transaction.category, (categories.get(transaction.category) ?? 0) + transaction.amount));
    const expenseCategories = [...categories.entries()].map(([category, amount], index) => ({ category: category as DashboardSummary['expenseCategories'][number]['category'], amount, percentage: transactions.length ? Math.round((amount / transactions.reduce((sum, item) => sum + item.amount, 0)) * 100) : 0, color: ['#2563EB', '#10B981', '#F59E0B', '#8B5CF6', '#64748B'][index % 5] }));
    const total = transactions.reduce((sum, item) => sum + item.amount, 0);
    return { totalTransactions: transactions.length, balance: 0, income: 0, expenses: total, savings: 0, monthlyComparison: { balance: { percentage: 0, isPositive: false, label: '' }, income: { percentage: 0, isPositive: false, label: '' }, expenses: { percentage: 0, isPositive: false, label: '' }, savings: { percentage: 0, isPositive: false, label: '' } }, cashFlow: [], expenseCategories, recentTransactions: transactions.slice(0, 5), aiRecommendations: [] };
  },
};
