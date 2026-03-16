package com.chequesecure.commercant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO envoyé à la Banque Centrale pour une demande de certification.
 */
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
