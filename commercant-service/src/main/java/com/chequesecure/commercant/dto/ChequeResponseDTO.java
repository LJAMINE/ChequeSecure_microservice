package com.chequesecure.commercant.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChequeResponseDTO {
    private Long id;
    private String numeroCheque;
    private String codeBanque;
    private String numeroCompte;
    private String cleRib;
    private String nomClient;
    private Double montant;
    private String statut;
    private LocalDateTime dateCreation;
    private LocalDateTime dateCertification;
    private String motifRefus;
}
