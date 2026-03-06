package com.chequesecure.commercant.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChequeRequestDTO {
    @NotBlank
    private String numeroCheque;

    @NotBlank
    @Size(min = 3, max = 3)
    private String codeBanque;

    @NotBlank
    private String numeroCompte;

    @NotBlank
    @Size(min = 2, max = 2)
    private String cleRib;

    @NotBlank
    private String nomClient;

    @NotNull
    @DecimalMin("0.01")
    private Double montant;
}
