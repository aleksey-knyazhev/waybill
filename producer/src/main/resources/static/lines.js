function renderLines(lines) {
    const total = lines.reduce((sum, line) => sum + Number(line.amountWithTax ?? 0), 0);
    setText('total', `Итого: ${money(total)}`);

    const tbody = document.getElementById('lines');
    tbody.innerHTML = '';

    for (const line of lines) {
        tbody.appendChild(createLineRow(line));
    }
}

function createLineRow(line) {
    const row = document.createElement('tr');
    row.innerHTML = `
        <td>${line.lineNumber}</td>
        <td>${line.item?.productCode ?? ''}</td>
        <td>${line.item?.name ?? ''}</td>
        <td>${line.item?.unitName ?? ''} (${line.item?.unitCode ?? ''})</td>
        <td class="number">${number(line.quantity)}</td>
        <td class="number">${money(line.unitPrice)}</td>
        <td class="number">${money(line.amountWithoutTax)}</td>
        <td class="number">${money(line.taxAmount)}</td>
        <td class="number">${money(line.amountWithTax)}</td>
    `;
    return row;
}
