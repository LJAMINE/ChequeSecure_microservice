package com.chequesecure.banquecentrale.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgenceBancaireRequestDTO {
    @NotBlank
    private String nom;

    @NotBlank
    private String ville;

    @NotBlank
    private String adresse;

    @NotBlank
    @Size(min = 3, max = 3)
    private String codeBanque;

    @NotBlank
    private String urlServiceWeb;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String telephone;

    @Builder.Default
    private Boolean actif = true;
}
