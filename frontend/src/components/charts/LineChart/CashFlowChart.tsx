import React from 'react';
import {
  ResponsiveContainer,
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend
} from 'recharts';
import { CashFlowPoint } from '../../../types/dashboard';
import { formatCurrency } from '../../../utils/currency';

interface CashFlowChartProps {
  data: CashFlowPoint[];
  height?: number;
}

export const CashFlowChart: React.FC<CashFlowChartProps> = ({ data, height = 300 }) => {
  return (
    <div className="w-full" style={{ height }}>
      <ResponsiveContainer width="100%" height="100%">
        <LineChart data={data} margin={{ top: 10, right: 10, left: 0, bottom: 0 }}>
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
          />
          <Legend
            verticalAlign="top"
            height={36}
            iconType="circle"
            formatter={(value) => (
              <span className="text-xs font-medium text-slate-700 dark:text-slate-300">
                {value === 'saldo' ? 'Saldo Acumulado Líquido' : value}
              </span>
            )}
          />
          <Line
            type="monotone"
            dataKey="saldo"
            name="saldo"
            stroke="#1E40AF"
            strokeWidth={3}
            dot={{ r: 4, fill: '#1E40AF' }}
            activeDot={{ r: 6, fill: '#1D4ED8' }}
          />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
};
