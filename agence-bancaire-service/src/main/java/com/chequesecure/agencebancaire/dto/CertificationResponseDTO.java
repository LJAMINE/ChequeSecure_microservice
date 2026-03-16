package com.chequesecure.agencebancaire.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificationResponseDTO {
    private String statut;      // CERTIFIE ou REFUSE
    private String motifRefus;  // Optionnel, présent si REFUSE
}
