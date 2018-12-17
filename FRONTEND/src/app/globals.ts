
export function dateToString(date: Date): string {
  if (date !== null && date !== undefined) {
    const _date = new Date(date);
    return _date.toLocaleDateString('hu-HU', {year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit'});
  } else {
    return '';
  }
}
