function renderLines(lines) {
    const total = lines.reduce((sum, line) => sum + Number(line.amountWithTax ?? 0), 0);
    setText('total', lines.length ? `Итого: ${money(total)}` : '');

    const tbody = document.getElementById('lines');
    tbody.innerHTML = '';

    for (const line of lines) {
        tbody.appendChild(createLineRow(line));
    }
}

function createLineRow(line) {
    const row = document.createElement('tr');
    row.innerHTML = `
        <td>${text(line.lineNumber)}</td>
        <td>${text(line.item?.productCode)}</td>
        <td>${text(line.item?.name)}</td>
        <td>${unitText(line.item)}</td>
        <td class="number">${formattedNumber(line.quantity)}</td>
        <td class="number">${formattedMoney(line.unitPrice)}</td>
        <td class="number">${formattedMoney(line.amountWithoutTax)}</td>
        <td class="number">${formattedMoney(line.taxAmount)}</td>
        <td class="number">${formattedMoney(line.amountWithTax)}</td>
    `;
    return row;
}

function text(value) {
    return value === null || value === undefined ? '' : value;
}

function unitText(item) {
    return [item?.unitName, item?.unitCode]
        .filter(value => value !== null && value !== undefined && String(value).trim() !== '')
        .join(' ');
}

function formattedNumber(value) {
    return value === null || value === undefined ? '' : number(value);
}

function formattedMoney(value) {
    return value === null || value === undefined ? '' : money(value);
}
