import React from 'react';
import {
  ResponsiveContainer,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Cell
} from 'recharts';
import { formatCurrency } from '../../../utils/currency';

interface CategoryBarItem {
  category: string;
  spent: number;
  color?: string;
}

interface CategoryBarChartProps {
  data?: CategoryBarItem[];
  height?: number;
}

export const CategoryBarChart: React.FC<CategoryBarChartProps> = ({
  data = [],
  height = 260
}) => {
  return (
    <div className="w-full" style={{ height }}>
      <ResponsiveContainer width="100%" height="100%">
        <BarChart data={data} margin={{ top: 15, right: 10, left: -15, bottom: 0 }}>
          <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#E2E8F0" />
          <XAxis
            dataKey="category"
            axisLine={false}
            tickLine={false}
            tick={{ fill: '#64748B', fontSize: 11, fontWeight: 600 }}
          />
          <YAxis
            axisLine={false}
            tickLine={false}
            tick={{ fill: '#94A3B8', fontSize: 11 }}
            tickFormatter={(val) => `R$ ${val}`}
          />
          <Tooltip
            formatter={(value: number) => [formatCurrency(value), 'Gasto Total']}
            contentStyle={{
              backgroundColor: '#0F172A',
              borderColor: '#1E293B',
              borderRadius: '12px',
              color: '#F8FAFC',
              fontSize: '12px',
              padding: '8px 12px',
            }}
          />
          <Bar dataKey="spent" radius={[6, 6, 0, 0]} barSize={36}>
            {data.map((entry, index) => (
              <Cell
                key={`bar-${index}`}
                fill={entry.color || '#3B82F6'}
              />
            ))}
          </Bar>
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
};

