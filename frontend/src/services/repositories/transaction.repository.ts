import { api } from '../api/axios';
import {
  Transaction,
  TransactionFilter,
  CreateTransactionDTO,
  UpdateTransactionDTO,
  TransactionCategory
} from '../../types/transaction';

interface BackendTransacaoResponse {
  id: number;
  descricao: string;
  valor: number;
  categoria: string;
  usuario_id: number;
  criado_em: string;
}

const mapBackendToTransaction = (b: BackendTransacaoResponse): Transaction => {
  // Mapear categorias do backend para formato amigável do frontend se necessário
  let categoryStr: TransactionCategory = 'Outros';
  if (b.categoria) {
    const lower = b.categoria.toLowerCase();
    if (lower.includes('alimenta') || lower.includes('mercado') || lower.includes('restaurante')) {
      categoryStr = 'Alimentação';
    } else if (lower.includes('transporte') || lower.includes('uber') || lower.includes('combustivel')) {
      categoryStr = 'Transporte';
    } else if (lower.includes('moradia') || lower.includes('aluguel') || lower.includes('luz')) {
      categoryStr = 'Moradia';
    } else if (lower.includes('saude') || lower.includes('farmacia')) {
      categoryStr = 'Saúde';
    } else if (lower.includes('educa')) {
      categoryStr = 'Educação';
    } else if (lower.includes('lazer') || lower.includes('entretenimento')) {
      categoryStr = 'Lazer';
    } else if (lower.includes('invest')) {
      categoryStr = 'Investimentos';
    } else if (lower.includes('salar') || lower.includes('receita')) {
      categoryStr = 'Salário';
    } else {
      categoryStr = 'Outros';
    }
  }

  return {
    id: String(b.id),
    description: b.descricao,
    amount: Number(b.valor),
    type: 'EXPENSE',
    category: categoryStr,
    date: b.criado_em || new Date().toISOString(),
    status: 'COMPLETED',
    paymentMethod: 'PIX',
  };
};

export const transactionRepository = {
  getTransactions: async (filter?: TransactionFilter): Promise<Transaction[]> => {
    const response = await api.get<BackendTransacaoResponse[]>('/transacoes');
    let transactions = (response.data || []).map(mapBackendToTransaction);

    if (filter) {
      if (filter.search) {
        const query = filter.search.toLowerCase();
        transactions = transactions.filter(t => t.description.toLowerCase().includes(query));
      }
      if (filter.category && filter.category !== 'ALL') {
        transactions = transactions.filter(t => t.category === filter.category);
      }
      if (filter.type && filter.type !== 'ALL') {
        transactions = transactions.filter(t => t.type === filter.type);
      }
    }

    return transactions;
  },

  getTransactionById: async (id: string): Promise<Transaction | null> => {
    const response = await api.get<BackendTransacaoResponse[]>('/transacoes');
    const item = (response.data || []).find(t => String(t.id) === id);
    return item ? mapBackendToTransaction(item) : null;
  },

  createTransaction: async (dto: CreateTransactionDTO): Promise<Transaction> => {
    const response = await api.post<BackendTransacaoResponse>('/transacoes', {
      descricao: dto.description,
      valor: dto.amount,
    });
    return mapBackendToTransaction(response.data);
  },

  updateTransaction: async (dto: UpdateTransactionDTO): Promise<Transaction> => {
    const response = await api.put<BackendTransacaoResponse>(`/transacoes/${dto.id}`, {
      descricao: dto.description || '',
      valor: dto.amount || 0,
    });
    return mapBackendToTransaction(response.data);
  },

  deleteTransaction: async (id: string): Promise<{ success: boolean }> => {
    await api.delete(`/transacoes/${id}`);
    return { success: true };
  },

  importCSV: async (csvText: string): Promise<{ importedCount: number; transactions: Transaction[] }> => {
    const file = new File([csvText], 'transacoes.csv', { type: 'text/csv' });
    const formData = new FormData();
    formData.append('arquivo', file);

    const response = await api.post<{ totalCriadas: number; totalFalhas: number; erros: string[] }>(
      '/transacoes/lote',
      formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      }
    );

    const updatedList = await transactionRepository.getTransactions();

    return {
      importedCount: response.data.totalCriadas || 0,
      transactions: updatedList,
    };
  }
};
