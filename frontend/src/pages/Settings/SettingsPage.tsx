import React from 'react';
import { Loader2, User } from 'lucide-react';
import { useSettings } from '../../hooks/useSettings';
import { Card, CardContent, CardHeader, CardTitle } from '../../components/common/Card/Card';
import { ApiErrorState } from '../../components/common/ApiErrorState/ApiErrorState';

export const SettingsPage: React.FC = () => {
  const { profile, isLoadingProfile, profileError, refetchProfile } = useSettings();
  if (isLoadingProfile) return <div className="flex justify-center p-12"><Loader2 className="animate-spin text-blue-600" /></div>;
  if (profileError) return <ApiErrorState onRetry={refetchProfile} />;
  if (!profile) return null;
  return <div className="space-y-6 max-w-3xl"><div><h1 className="text-2xl font-bold text-slate-900 dark:text-white">Perfil</h1><p className="text-sm text-slate-500">Informações disponíveis para a conta autenticada.</p></div><Card><CardHeader><CardTitle>Dados da conta</CardTitle></CardHeader><CardContent className="flex items-center gap-5"><div className="rounded-full bg-slate-100 p-4 dark:bg-slate-800"><User className="w-8 h-8" /></div><dl className="space-y-2 text-sm"><div><dt className="text-slate-500">Nome</dt><dd className="font-semibold">{profile.name}</dd></div><div><dt className="text-slate-500">E-mail</dt><dd className="font-semibold">{profile.email}</dd></div><div><dt className="text-slate-500">ID</dt><dd className="font-semibold">{profile.id}</dd></div></dl></CardContent></Card><p className="text-xs text-slate-500">A edição de perfil e preferências está em desenvolvimento.</p></div>;
};
