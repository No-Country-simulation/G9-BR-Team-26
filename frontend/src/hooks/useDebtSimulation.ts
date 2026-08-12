import { useMutation } from '@tanstack/react-query';
import { debtSimulationRepository } from '../services/repositories/debt-simulation.repository';

export function useDebtSimulation() {
  const mutation = useMutation({ mutationFn: debtSimulationRepository.simulate });
  return { simulate: mutation.mutateAsync, result: mutation.data, isSimulating: mutation.isPending, error: mutation.error };
}
