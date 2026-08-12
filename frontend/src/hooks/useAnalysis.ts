import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { analysisRepository } from '../services/repositories/analysis.repository';

export function useAnalysis() {
  const queryClient = useQueryClient();
  const historyQuery = useQuery({ queryKey: ['analysisHistory'], queryFn: analysisRepository.getHistory });
  const createMutation = useMutation({
    mutationFn: analysisRepository.createAnalysis,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['analysisHistory'] }),
  });
  return {
    history: historyQuery.data ?? [],
    latestAnalysis: historyQuery.data?.[0],
    isLoading: historyQuery.isLoading,
    isError: historyQuery.isError,
    error: historyQuery.error,
    refetch: historyQuery.refetch,
    createAnalysis: createMutation.mutateAsync,
    isCreating: createMutation.isPending,
    createError: createMutation.error,
  };
}
