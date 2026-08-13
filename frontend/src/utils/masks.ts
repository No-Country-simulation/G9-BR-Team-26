export function maskCPF(value: string): string {
  return value
    .replace(/\D/g, '')
    .replace(/(\d{3})(\d)/, '$1.$2')
    .replace(/(\d{3})(\d)/, '$1.$2')
    .replace(/(\d{3})(\d{1,2})/, '$1-$2')
    .replace(/(-\d{2})\d+?$/, '$1');
}

export function maskPhone(value: string): string {
  return value
    .replace(/\D/g, '')
    .replace(/(\d{2})(\d)/, '($1) $2')
    .replace(/(\d{5})(\d)/, '$1-$2')
    .replace(/(-\d{4})\d+?$/, '$1');
}

export function maskCurrencyInput(value: string): string {
  const digits = value.replace(/\D/g, '');
  if (!digits) return '0,00';
  const realValue = (parseInt(digits, 10) / 100).toFixed(2);
  return realValue.replace('.', ',').replace(/\B(?=(\d{3})+(?!\d))/g, '.');
}
