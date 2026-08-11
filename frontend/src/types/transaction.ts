export type TransactionType = 'INCOME' | 'EXPENSE';

export type TransactionCategory =
  | 'Alimentação'
  | 'Moradia'
  | 'Transporte'
  | 'Saúde'
  | 'Educação'
  | 'Lazer'
  | 'Investimentos'
  | 'Salário'
  | 'Vendas'
  | 'Serviços'
  | 'Outros';

export type TransactionStatus = 'COMPLETED' | 'PENDING' | 'CANCELLED';

export type PaymentMethod = 'PIX' | 'CREDIT_CARD' | 'DEBIT_CARD' | 'BANK_TRANSFER' | 'BOLETO' | 'CASH';

export interface Transaction {
  id: string;
  description: string;
  amount: number;
  type: TransactionType;
  category: TransactionCategory;
  date: string; // ISO format
  status: TransactionStatus;
  paymentMethod: PaymentMethod;
  notes?: string;
  tags?: string[];
  recurring?: boolean;
}

export interface TransactionFilter {
  search?: string;
  category?: TransactionCategory | 'ALL';
  type?: TransactionType | 'ALL';
  status?: TransactionStatus | 'ALL';
  period?: 'THIS_MONTH' | 'LAST_MONTH' | 'LAST_30_DAYS' | 'THIS_YEAR' | 'ALL';
  startDate?: string;
  endDate?: string;
  minAmount?: number;
  maxAmount?: number;
}

export interface CreateTransactionDTO {
  description: string;
  amount: number;
  type: TransactionType;
  category: TransactionCategory;
  date: string;
  status: TransactionStatus;
  paymentMethod: PaymentMethod;
  notes?: string;
  recurring?: boolean;
}

export interface UpdateTransactionDTO extends Partial<CreateTransactionDTO> {
  id: string;
}
