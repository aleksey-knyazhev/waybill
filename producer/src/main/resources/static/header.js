function renderHeader(documentData) {
    setText('status', `Статус ${documentData.status}`);
    setText('invoiceNumber', documentData.invoiceNumber);
    setText('invoiceDate', documentData.invoiceDate);
    setText('currency', `${documentData.currencyName}, ${documentData.currencyCode}`);
    setText('transferBasis', documentData.transferBasis);
    setText('sellerName', documentData.seller?.name);
    setText('sellerInnKpp', documentData.seller?.innKpp);
    setText('buyerName', documentData.buyer?.name);
    setText('buyerInnKpp', documentData.buyer?.innKpp);
}
