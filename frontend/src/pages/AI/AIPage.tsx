import React, { FormEvent, useState } from 'react';
import { Bot, Loader2, Send, Sparkles, UserRound } from 'lucide-react';
import { Card, CardContent } from '../../components/common/Card/Card';
import { api } from '../../services/api/axios';

type Message = { role: 'user' | 'assistant'; content: string };
type FaiChatResponse = { resposta: string };
const welcome: Message = { role: 'assistant', content: 'Olá, eu sou a Fai. Posso ajudar com suas finanças, transações, análises e dúvidas sobre o SmartFinance.' };

export const AIPage: React.FC = () => {
  const [messages, setMessages] = useState<Message[]>([welcome]);
  const [message, setMessage] = useState('');
  const [isSending, setIsSending] = useState(false);
  const [error, setError] = useState('');
  const [isUnavailable, setIsUnavailable] = useState(false);

  const send = async (event: FormEvent) => {
    event.preventDefault();
    const question = message.trim();
    if (!question || isSending || isUnavailable) return;
    setMessage(''); setError(''); setIsSending(true);
    setMessages(current => [...current, { role: 'user', content: question }]);
    try {
      const { data } = await api.post<FaiChatResponse>('/fai/chat', { mensagem: question });
      setMessages(current => [...current, { role: 'assistant', content: data.resposta }]);
    } catch (requestError) {
      console.error('Falha no chat da Fai.', requestError);
      setError('Funcionalidade indisponível no momento. Tente novamente mais tarde.');
      setIsUnavailable(true);
    } finally { setIsSending(false); }
  };

  return <div className="mx-auto flex max-w-4xl flex-col gap-6">
    <div className="flex items-center gap-3"><div className="rounded-xl bg-blue-600 p-2 text-white"></div><div><h1 className="text-2xl font-bold text-slate-900 dark:text-white">Fai</h1><p className="text-sm text-slate-500">Sua assistente de finanças pessoais, ela irá lhe auxiliar a tomar as melhores decisões baseado no seu contexto</p></div></div>
    <Card><CardContent className="p-0"><div className="h-[28rem] space-y-5 overflow-y-auto p-5 sm:p-6">
      {messages.map((item, index) => <div key={`${item.role}-${index}`} className={`flex gap-3 ${item.role === 'user' ? 'justify-end' : ''}`}>
        {item.role === 'assistant' && <div className="mt-1 rounded-full bg-blue-100 p-2 text-blue-700 dark:bg-blue-950 dark:text-blue-300"><Bot className="h-4 w-4" /></div>}
        <p className={`max-w-[80%] whitespace-pre-wrap rounded-2xl px-4 py-3 text-sm leading-6 ${item.role === 'user' ? 'bg-blue-600 text-white' : 'bg-slate-100 text-slate-700 dark:bg-slate-800 dark:text-slate-100'}`}>{item.content}</p>
        {item.role === 'user' && <div className="mt-1 rounded-full bg-slate-200 p-2 text-slate-600 dark:bg-slate-700 dark:text-slate-200"><UserRound className="h-4 w-4" /></div>}
      </div>)}
      {isSending && <div className="flex items-center gap-2 text-sm text-slate-500"><Loader2 className="h-4 w-4 animate-spin" />Fai está pensando...</div>}
    </div>
    {error && <p className="mx-5 mb-3 rounded-lg bg-rose-50 p-3 text-sm text-rose-700 dark:bg-rose-950/40 dark:text-rose-200">{error}</p>}
    <fieldset disabled={isSending || isUnavailable} className="min-w-0 disabled:cursor-not-allowed">
    <form onSubmit={send} className="flex gap-2 border-t border-slate-200 p-4 dark:border-slate-800"><label className="sr-only" htmlFor="fai-message">Pergunta para Fai</label><input id="fai-message" value={message} onChange={event => setMessage(event.target.value)} maxLength={2000} disabled={isSending} placeholder="Pergunte sobre suas finanças..." className="min-w-0 flex-1 rounded-xl border border-slate-300 bg-white px-4 py-2.5 text-sm outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-200 dark:border-slate-700 dark:bg-slate-900" /><button type="submit" disabled={!message.trim() || isSending} className="inline-flex items-center gap-2 rounded-xl bg-blue-600 px-4 py-2.5 text-sm font-semibold text-white hover:bg-blue-500 disabled:cursor-not-allowed disabled:opacity-50"><Send className="h-4 w-4" />Enviar</button></form>
    </fieldset>
    </CardContent></Card>
  </div>;
};
