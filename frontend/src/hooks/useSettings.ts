import { useQuery } from '@tanstack/react-query';
import { userRepository } from '../services/repositories/user.repository';

export function useSettings() {
  const profileQuery = useQuery({ queryKey: ['userProfile'], queryFn: userRepository.getProfile });
  return { profile: profileQuery.data, isLoadingProfile: profileQuery.isLoading, profileError: profileQuery.error, refetchProfile: profileQuery.refetch };
}
