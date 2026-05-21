INSERT INTO waybill_consumer.xslt_template (version, file_name, content_type, content)
VALUES ('01', 'waybill-document_version_01.xsl', 'application/xslt+xml', $xslt$<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                xmlns:w="http://waybill.ru/soap"
                exclude-result-prefixes="soap w">
    <xsl:output method="html" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/">
        <xsl:variable name="document" select="/soap:Envelope/soap:Body/w:getWaybillDocumentResponse/w:waybillDocument"/>
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
    </xsl:template>
</xsl:stylesheet>
$xslt$)
ON CONFLICT (version) DO UPDATE
SET file_name = EXCLUDED.file_name,
    content_type = EXCLUDED.content_type,
    content = EXCLUDED.content;

INSERT INTO waybill_consumer.xslt_template (version, file_name, content_type, content)
VALUES ('02', 'waybill-document_version_02.xsl', 'application/xslt+xml', $xslt$<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                xmlns:w="http://waybill.ru/soap"
                exclude-result-prefixes="soap w">
    <xsl:output method="html" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/">
        <xsl:variable name="document" select="/soap:Envelope/soap:Body/w:getWaybillDocumentResponse/w:waybillDocument"/>
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
    </xsl:template>
</xsl:stylesheet>
$xslt$)
ON CONFLICT (version) DO UPDATE
SET file_name = EXCLUDED.file_name,
    content_type = EXCLUDED.content_type,
    content = EXCLUDED.content;
