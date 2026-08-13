import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { transactionRepository } from '../services/repositories/transaction.repository';
import { TransactionFilter, CreateTransactionDTO, UpdateTransactionDTO } from '../types/transaction';

export function useTransactions(filter?: TransactionFilter) {
  const queryClient = useQueryClient();

  const transactionsQuery = useQuery({
    queryKey: ['transactions', filter],
    queryFn: () => transactionRepository.getTransactions(filter),
  });

  const createMutation = useMutation({
    mutationFn: (dto: CreateTransactionDTO) => transactionRepository.createTransaction(dto),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['transactions'] });
      queryClient.invalidateQueries({ queryKey: ['dashboardSummary'] });
      queryClient.invalidateQueries({ queryKey: ['financialHealth'] });
    },
  });

  const updateMutation = useMutation({
    mutationFn: (dto: UpdateTransactionDTO) => transactionRepository.updateTransaction(dto),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['transactions'] });
      queryClient.invalidateQueries({ queryKey: ['dashboardSummary'] });
      queryClient.invalidateQueries({ queryKey: ['financialHealth'] });
    },
  });

  const deleteMutation = useMutation({
    mutationFn: (id: string) => transactionRepository.deleteTransaction(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['transactions'] });
      queryClient.invalidateQueries({ queryKey: ['dashboardSummary'] });
      queryClient.invalidateQueries({ queryKey: ['financialHealth'] });
    },
  });

  const importCSVMutation = useMutation({
    mutationFn: (csvText: string) => transactionRepository.importCSV(csvText),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['transactions'] });
      queryClient.invalidateQueries({ queryKey: ['dashboardSummary'] });
    },
  });

  return {
    transactions: transactionsQuery.data || [],
    isLoading: transactionsQuery.isLoading,
    isError: transactionsQuery.isError,
    error: transactionsQuery.error,
    refetch: transactionsQuery.refetch,
    createTransaction: createMutation.mutateAsync,
    isCreating: createMutation.isPending,
    updateTransaction: updateMutation.mutateAsync,
    isUpdating: updateMutation.isPending,
    deleteTransaction: deleteMutation.mutateAsync,
    isDeleting: deleteMutation.isPending,
    importCSV: importCSVMutation.mutateAsync,
    isImporting: importCSVMutation.isPending,
  };
}
