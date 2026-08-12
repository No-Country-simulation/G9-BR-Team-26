import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { goalsRepository } from '../services/repositories/goals.repository';

export function useGoals() {
  const client = useQueryClient();
  const query = useQuery({ queryKey: ['financialGoals'], queryFn: goalsRepository.getAll });
  const mutation = useMutation({ mutationFn: goalsRepository.create, onSuccess: () => client.invalidateQueries({ queryKey: ['financialGoals'] }) });
  return { goals: query.data ?? [], isLoading: query.isLoading, error: query.error, refetch: query.refetch, createGoal: mutation.mutateAsync, isCreating: mutation.isPending, createError: mutation.error };
}
