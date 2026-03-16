package com.chequesecure.agencebancaire.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificationRequestDTO {
    private String numeroCheque;
    private String codeBanque;
    private String numeroCompte;
    private String cleRib;
    private String nomClient;
    private Double montant;
}
