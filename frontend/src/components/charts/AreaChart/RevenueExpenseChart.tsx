import React from 'react';
import {
  ResponsiveContainer,
  AreaChart,
  Area,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend
} from 'recharts';
import { CashFlowPoint } from '../../../types/dashboard';
import { formatCurrency } from '../../../utils/currency';

interface RevenueExpenseChartProps {
  data: CashFlowPoint[];
  height?: number;
}

export const RevenueExpenseChart: React.FC<RevenueExpenseChartProps> = ({ data, height = 300 }) => {
  return (
    <div className="w-full" style={{ height }}>
      <ResponsiveContainer width="100%" height="100%">
        <AreaChart data={data} margin={{ top: 10, right: 10, left: 0, bottom: 0 }}>
          <defs>
            <linearGradient id="colorReceitas" x1="0" y1="0" x2="0" y2="1">
              <stop offset="5%" stopColor="#16A34A" stopOpacity={0.3} />
              <stop offset="95%" stopColor="#16A34A" stopOpacity={0.0} />
            </linearGradient>
            <linearGradient id="colorDespesas" x1="0" y1="0" x2="0" y2="1">
              <stop offset="5%" stopColor="#DC2626" stopOpacity={0.3} />
              <stop offset="95%" stopColor="#DC2626" stopOpacity={0.0} />
            </linearGradient>
          </defs>
          <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#E2E8F0" />
          <XAxis
            dataKey="month"
            axisLine={false}
            tickLine={false}
            tick={{ fill: '#64748B', fontSize: 11 }}
          />
          <YAxis
            axisLine={false}
            tickLine={false}
            tick={{ fill: '#64748B', fontSize: 11 }}
            tickFormatter={(val) => `R$ ${(val / 1000).toFixed(0)}k`}
          />
          <Tooltip
            formatter={(value: number) => [formatCurrency(value), '']}
            contentStyle={{
              backgroundColor: '#0F172A',
              borderColor: '#334155',
              borderRadius: '8px',
              color: '#F8FAFC',
              fontSize: '12px',
            }}
            itemStyle={{ color: '#F8FAFC' }}
          />
          <Legend
            verticalAlign="top"
            height={36}
            iconType="circle"
            formatter={(value) => (
              <span className="text-xs font-medium text-slate-700 dark:text-slate-300">
                {value === 'receitas' ? 'Receitas' : 'Despesas'}
              </span>
            )}
          />
          <Area
            type="monotone"
            dataKey="receitas"
            name="receitas"
            stroke="#16A34A"
            strokeWidth={2.5}
            fillOpacity={1}
            fill="url(#colorReceitas)"
          />
          <Area
            type="monotone"
            dataKey="despesas"
            name="despesas"
            stroke="#DC2626"
            strokeWidth={2.5}
            fillOpacity={1}
            fill="url(#colorDespesas)"
          />
        </AreaChart>
      </ResponsiveContainer>
    </div>
  );
};
