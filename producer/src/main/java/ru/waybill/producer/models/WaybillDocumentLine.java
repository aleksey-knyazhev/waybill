package ru.waybill.producer.models;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WaybillDocumentLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Integer lineNumber;

    @Valid
    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "productCode", column = @Column(name = "item_product_code")),
            @AttributeOverride(name = "name", column = @Column(name = "item_name")),
            @AttributeOverride(name = "unitCode", column = @Column(name = "item_unit_code")),
            @AttributeOverride(name = "unitName", column = @Column(name = "item_unit_name"))
    })
    private Item item;

    @NotNull
    private BigDecimal quantity;

    private BigDecimal unitPrice;
    private BigDecimal amountWithoutTax;
    private BigDecimal taxAmount;
    private BigDecimal amountWithTax;
}
