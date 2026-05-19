const moneyFormat = new Intl.NumberFormat('ru-RU', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
});

const numberFormat = new Intl.NumberFormat('ru-RU', {
    maximumFractionDigits: 3
});

let uploadAfterFileSelection = false;

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

async function importDocument(event) {
    event?.preventDefault();

    const form = document.getElementById('importForm');
    const fileInput = document.getElementById('importFile');
    const importStatus = document.getElementById('importStatus');
    const submitButton = form.querySelector('button');

    updateSelectedFileName();

    if (!fileInput.files.length) {
        importStatus.textContent = 'Файл не выбран';
        return;
    }

    const formData = new FormData();
    formData.append('file', fileInput.files[0]);
    submitButton.disabled = true;
    importStatus.textContent = 'Загрузка';

    try {
        const response = await fetch('/api/import/waybill', {
            method: 'POST',
            body: formData
        });
        if (!response.ok) {
            throw new Error(`HTTP ${response.status}`);
        }
        renderDocument(await response.json());
        importStatus.textContent = 'Файл загружен';
    } catch (error) {
        importStatus.textContent = 'Ошибка загрузки';
        console.error(error);
    } finally {
        submitButton.disabled = false;
    }
}

function updateSelectedFileName() {
    const fileInput = document.getElementById('importFile');
    const fileName = document.getElementById('fileName');
    const selectedName = fileInput.files.length
        ? fileInput.files[0].name
        : fileInput.value.split('\\').pop();

    fileName.textContent = selectedName || 'Файл не выбран';
}

function chooseAndUpload() {
    const fileInput = document.getElementById('importFile');
    uploadAfterFileSelection = true;
    fileInput.value = '';
    updateSelectedFileName();
    fileInput.click();
}

function handleFileSelection() {
    updateSelectedFileName();
    if (uploadAfterFileSelection) {
        uploadAfterFileSelection = false;
        importDocument();
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

document.getElementById('importFile').addEventListener('change', handleFileSelection);
document.getElementById('importFile').addEventListener('input', updateSelectedFileName);
document.getElementById('importForm').addEventListener('submit', importDocument);
document.getElementById('uploadButton').addEventListener('click', chooseAndUpload);
loadDocument();
