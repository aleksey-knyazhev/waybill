const moneyFormat = new Intl.NumberFormat('ru-RU', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
});

const numberFormat = new Intl.NumberFormat('ru-RU', {
    maximumFractionDigits: 3
});

function setText(id, value) {
    document.getElementById(id).textContent = value ?? '';
}

function money(value) {
    return moneyFormat.format(Number(value ?? 0));
}

function number(value) {
    return numberFormat.format(Number(value ?? 0));
}

function renderDocument(documentData) {
    setText('status', `Статус ${documentData.status}`);
    setText('invoiceNumber', documentData.invoiceNumber);
    setText('invoiceDate', documentData.invoiceDate);
    setText('currency', `${documentData.currencyName}, ${documentData.currencyCode}`);
    setText('transferBasis', documentData.transferBasis);
    setText('sellerName', documentData.seller?.name);
    setText('sellerInnKpp', documentData.seller?.innKpp);
    setText('buyerName', documentData.buyer?.name);
    setText('buyerInnKpp', documentData.buyer?.innKpp);

    const lines = documentData.lines ?? [];
    const total = lines.reduce((sum, line) => sum + Number(line.amountWithTax ?? 0), 0);
    setText('total', `Итого: ${money(total)}`);

    const tbody = document.getElementById('lines');
    tbody.innerHTML = '';

    for (const line of lines) {
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
        tbody.appendChild(row);
    }
}

async function loadDocument() {
    try {
        const response = await fetch('/api/waybill/example');
        if (!response.ok) {
            throw new Error(`HTTP ${response.status}`);
        }
        renderDocument(await response.json());
    } catch (error) {
        const status = document.getElementById('status');
        status.textContent = 'Ошибка';
        status.classList.add('error');
        console.error(error);
    }
}

loadDocument();
