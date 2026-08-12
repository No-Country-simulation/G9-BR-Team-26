import { useQuery } from '@tanstack/react-query';
import { dashboardRepository } from '../services/repositories/dashboard.repository';

export function useDashboard() {
  const query = useQuery({
    queryKey: ['dashboardSummary'],
    queryFn: () => dashboardRepository.getSummary(),
    staleTime: 1000 * 30, // 30 seconds
  });

  return {
    summary: query.data,
    isLoading: query.isLoading,
    isError: query.isError,
    error: query.error,
    refetch: query.refetch,
  };
}
