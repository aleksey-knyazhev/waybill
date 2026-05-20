<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                xmlns:w="http://waybill.ru/soap"
                exclude-result-prefixes="soap w">
    <xsl:output method="html" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/">
        <html lang="ru">
        <head>
            <meta charset="UTF-8"/>
            <title>Waybill document</title>
            <style>
                body {
                    margin: 0;
                    color: #263238;
                    background: #eef5f3;
                    font-family: Arial, sans-serif;
                }

                main {
                    max-width: 1120px;
                    margin: 0 auto;
                    padding: 32px 24px;
                }

                h1 {
                    margin: 0 0 24px;
                    color: #0f4c5c;
                    font-size: 28px;
                    font-weight: 700;
                }

                h2 {
                    margin: 0 0 12px;
                    color: #1d6f5f;
                    font-size: 18px;
                }

                section {
                    margin-bottom: 24px;
                    padding: 20px;
                    background: #fffdf8;
                    border: 1px solid #b8d8d0;
                    border-radius: 8px;
                }

                dl {
                    display: grid;
                    grid-template-columns: minmax(160px, 240px) 1fr;
                    gap: 10px 16px;
                    margin: 0;
                }

                dt {
                    color: #4b635f;
                    font-weight: 700;
                }

                dd {
                    margin: 0;
                }

                .organizations {
                    display: grid;
                    grid-template-columns: repeat(2, minmax(0, 1fr));
                    gap: 16px;
                }

                table {
                    width: 100%;
                    border-collapse: collapse;
                    background: #fffdf8;
                }

                th,
                td {
                    padding: 10px 12px;
                    border: 1px solid #b8d8d0;
                    text-align: left;
                    vertical-align: top;
                }

                th {
                    color: #ffffff;
                    background: #1d6f5f;
                    font-weight: 700;
                }

                .number {
                    text-align: right;
                    white-space: nowrap;
                }

                @media (max-width: 760px) {
                    main {
                        padding: 20px 12px;
                    }

                    dl,
                    .organizations {
                        grid-template-columns: 1fr;
                    }

                    table {
                        display: block;
                        overflow-x: auto;
                    }
                }
            </style>
        </head>
        <body>
        <xsl:variable name="document" select="/soap:Envelope/soap:Body/w:getWaybillDocumentResponse/w:waybillDocument"/>
        <main>
            <h1>Waybill document</h1>

            <section>
                <h2>Document</h2>
                <dl>
                    <dt>Invoice number</dt>
                    <dd><xsl:value-of select="$document/w:invoiceNumber"/></dd>
                    <dt>Invoice date</dt>
                    <dd><xsl:value-of select="$document/w:invoiceDate"/></dd>
                    <dt>Status</dt>
                    <dd><xsl:value-of select="$document/w:status"/></dd>
                    <dt>Currency</dt>
                    <dd>
                        <xsl:value-of select="$document/w:currencyName"/>
                        <xsl:text> </xsl:text>
                        <xsl:value-of select="$document/w:currencyCode"/>
                    </dd>
                    <dt>Transfer basis</dt>
                    <dd><xsl:value-of select="$document/w:transferBasis"/></dd>
                </dl>
            </section>

            <section>
                <h2>Organizations</h2>
                <div class="organizations">
                    <dl>
                        <dt>Seller</dt>
                        <dd><xsl:value-of select="$document/w:seller/w:name"/></dd>
                        <dt>INN/KPP</dt>
                        <dd><xsl:value-of select="$document/w:seller/w:innKpp"/></dd>
                    </dl>
                    <dl>
                        <dt>Buyer</dt>
                        <dd><xsl:value-of select="$document/w:buyer/w:name"/></dd>
                        <dt>INN/KPP</dt>
                        <dd><xsl:value-of select="$document/w:buyer/w:innKpp"/></dd>
                    </dl>
                </div>
            </section>

            <section>
                <h2>Lines</h2>
                <table>
                    <thead>
                    <tr>
                        <th>No.</th>
                        <th>Product code</th>
                        <th>Name</th>
                        <th>Unit</th>
                        <th>Quantity</th>
                        <th>Unit price</th>
                        <th>Without tax</th>
                        <th>Tax</th>
                        <th>With tax</th>
                    </tr>
                    </thead>
                    <tbody>
                    <xsl:for-each select="$document/w:lines/w:line">
                        <tr>
                            <td class="number"><xsl:value-of select="w:lineNumber"/></td>
                            <td><xsl:value-of select="w:item/w:productCode"/></td>
                            <td><xsl:value-of select="w:item/w:name"/></td>
                            <td>
                                <xsl:value-of select="w:item/w:unitName"/>
                                <xsl:text> </xsl:text>
                                <xsl:value-of select="w:item/w:unitCode"/>
                            </td>
                            <td class="number"><xsl:value-of select="w:quantity"/></td>
                            <td class="number"><xsl:value-of select="w:unitPrice"/></td>
                            <td class="number"><xsl:value-of select="w:amountWithoutTax"/></td>
                            <td class="number"><xsl:value-of select="w:taxAmount"/></td>
                            <td class="number"><xsl:value-of select="w:amountWithTax"/></td>
                        </tr>
                    </xsl:for-each>
                    </tbody>
                </table>
            </section>
        </main>
        </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
