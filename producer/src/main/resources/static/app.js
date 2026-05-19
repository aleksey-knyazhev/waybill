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
    renderHeader(documentData);
    renderLines(documentData.lines ?? []);
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
