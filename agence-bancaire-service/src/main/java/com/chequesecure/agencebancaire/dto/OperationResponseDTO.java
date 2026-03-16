package com.chequesecure.agencebancaire.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationResponseDTO {
    private Long id;
    private LocalDateTime date;
    private Double montant;
    private String numeroCheque;
    private String type;
    private String description;
    private Double soldeAvant;
    private Double soldeApres;
}
