function renderHeader(documentData) {
    setText('status', documentData.status);
    setText('invoiceNumber', documentData.invoiceNumber);
    setText('invoiceDate', documentData.invoiceDate);
    setText('currency', currencyText(documentData));
    setText('transferBasis', documentData.transferBasis);
    setText('sellerName', documentData.seller?.name);
    setText('sellerInnKpp', documentData.seller?.innKpp);
    setText('buyerName', documentData.buyer?.name);
    setText('buyerInnKpp', documentData.buyer?.innKpp);
}

function currencyText(documentData) {
    return [documentData.currencyName, documentData.currencyCode]
        .filter(value => value !== null && value !== undefined && String(value).trim() !== '')
        .join(', ');
}
