package com.chequesecure.banquecentrale.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemandeCertificationResponseDTO {
    private Long id;
    private String numeroCheque;
    private String codeBanque;
    private String numeroCompte;
    private String nomClient;
    private Double montant;
    private String statut;
    private String motifRefus;
    private LocalDateTime dateDemande;
}
