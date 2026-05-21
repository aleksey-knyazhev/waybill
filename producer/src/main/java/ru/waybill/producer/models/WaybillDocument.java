package ru.waybill.producer.models;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WaybillDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String invoiceNumber;

    @NotNull
    private LocalDate invoiceDate;

    private Integer status;

    @Valid
    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "seller_name")),
            @AttributeOverride(name = "innKpp", column = @Column(name = "seller_inn_kpp"))
    })
    private Organization seller;

    @Valid
    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "buyer_name")),
            @AttributeOverride(name = "innKpp", column = @Column(name = "buyer_inn_kpp"))
    })
    private Organization buyer;

    private String currencyName;
    private String currencyCode;
    private String transferBasis;

    @Valid
    @NotNull
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WaybillDocumentLine> lines = new ArrayList<>();
}
