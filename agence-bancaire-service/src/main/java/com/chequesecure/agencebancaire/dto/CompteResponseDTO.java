package com.chequesecure.agencebancaire.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompteResponseDTO {
    private Long id;
    private String numeroCompte;
    private Double solde;
    private String referenceClient;
    private String nomClient;
    private String type;
    private LocalDate dateOuverture;
    private Boolean actif;
}
