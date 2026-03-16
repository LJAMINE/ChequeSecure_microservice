package com.chequesecure.banquecentrale.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgenceBancaireResponseDTO {
    private Long id;
    private String nom;
    private String ville;
    private String adresse;
    private String codeBanque;
    private String urlServiceWeb;
    private String email;
    private String telephone;
    private Boolean actif;
}
