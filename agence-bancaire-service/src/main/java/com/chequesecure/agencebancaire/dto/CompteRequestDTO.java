package com.chequesecure.agencebancaire.dto;

import com.chequesecure.agencebancaire.entity.Compte;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompteRequestDTO {
    @NotBlank
    private String numeroCompte;

    @NotNull
    @DecimalMin("0")
    private Double solde;

    @NotBlank
    private String referenceClient;

    @NotBlank
    private String nomClient;

    @NotNull
    private Compte.Type type;

    @NotNull
    private LocalDate dateOuverture;

    @Builder.Default
    private Boolean actif = true;
}
