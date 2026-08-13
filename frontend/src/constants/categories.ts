import { TransactionCategory } from '../types/transaction';

export interface CategoryMeta {
  name: TransactionCategory;
  color: string;
  bgColor: string;
  iconName: string;
}

export const CATEGORIES_META: Record<TransactionCategory, CategoryMeta> = {
  'Alimentação': { name: 'Alimentação', color: '#EA580C', bgColor: '#FFEDD5', iconName: 'Utensils' },
  'Moradia': { name: 'Moradia', color: '#1E40AF', bgColor: '#DBEAFE', iconName: 'Home' },
  'Transporte': { name: 'Transporte', color: '#0284C7', bgColor: '#E0F2FE', iconName: 'Car' },
  'Saúde': { name: 'Saúde', color: '#DC2626', bgColor: '#FEE2E2', iconName: 'Activity' },
  'Educação': { name: 'Educação', color: '#7C3AED', bgColor: '#EDE9FE', iconName: 'GraduationCap' },
  'Lazer': { name: 'Lazer', color: '#D97706', bgColor: '#FEF3C7', iconName: 'Gamepad2' },
  'Investimentos': { name: 'Investimentos', color: '#16A34A', bgColor: '#DCFCE7', iconName: 'TrendingUp' },
  'Salário': { name: 'Salário', color: '#059669', bgColor: '#D1FAE5', iconName: 'Briefcase' },
  'Vendas': { name: 'Vendas', color: '#0D9488', bgColor: '#CCFBF1', iconName: 'ShoppingBag' },
  'Serviços': { name: 'Serviços', color: '#4F46E5', bgColor: '#E0E7FF', iconName: 'Wrench' },
  'Outros': { name: 'Outros', color: '#64748B', bgColor: '#F1F5F9', iconName: 'MoreHorizontal' },
};

export const CATEGORY_OPTIONS: TransactionCategory[] = [
  'Alimentação',
  'Moradia',
  'Transporte',
  'Saúde',
  'Educação',
  'Lazer',
  'Investimentos',
  'Salário',
  'Vendas',
  'Serviços',
  'Outros',
];
