package com.chequesecure.commercant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO reçu de la Banque Centrale après traitement de la certification.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificationResponseDTO {
    private String statut;      // CERTIFIE ou REFUSE
    private String motifRefus;  // Optionnel, présent si REFUSE
}
