export function formatCurrency(value: number, currency: string = 'BRL', locale: string = 'pt-BR'): string {
  try {
    return new Intl.NumberFormat(locale, {
      style: 'currency',
      currency: currency,
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    }).format(value);
  } catch {
    return `R$ ${value.toFixed(2).replace('.', ',')}`;
  }
}

export function parseCurrency(valueString: string): number {
  if (!valueString) return 0;
  const cleanValue = valueString.replace(/[^\d,-]/g, '').replace(',', '.');
  const parsed = parseFloat(cleanValue);
  return isNaN(parsed) ? 0 : parsed;
}
