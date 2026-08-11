import React from 'react';
import { Sparkles, MessageCircle } from 'lucide-react';
import { Card, CardContent } from '../../components/common/Card/Card';

// O chat Gemini permanece visível, mas não realiza chamadas enquanto não houver integração autorizada.
export const AIPage: React.FC = () => <div className="space-y-6 max-w-4xl"><div><h1 className="text-2xl font-bold text-slate-900 dark:text-white">Assistente financeiro</h1><p className="text-sm text-slate-500">Conversa com IA.</p></div><Card><CardContent className="p-12 text-center"><Sparkles className="w-10 h-10 text-blue-600 mx-auto mb-4"/><h2 className="font-bold text-lg">Em breve</h2><p className="text-sm text-slate-500 mt-2">O chat com Gemini está visível, mas desabilitado nesta versão. Nenhuma chamada à API Gemini é realizada.</p><div className="mt-6 rounded-xl bg-slate-50 dark:bg-slate-800 p-4 text-left text-sm text-slate-400 flex gap-2"><MessageCircle className="w-4 h-4"/>Digite uma pergunta quando a integração estiver disponível.</div></CardContent></Card></div>;
