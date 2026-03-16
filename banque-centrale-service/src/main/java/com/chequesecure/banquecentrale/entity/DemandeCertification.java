package com.chequesecure.banquecentrale.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "demandes_certification")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemandeCertification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String numeroCheque;

    @Column(nullable = false, length = 3)
    private String codeBanque;

    @Column(nullable = false)
    private String numeroCompte;

    @Column(nullable = false)
    private String nomClient;

    @Column(nullable = false)
    private Double montant;

    @Column(nullable = false)
    private String statut;  // CERTIFIE ou REFUSE

    @Column(length = 255)
    private String motifRefus;

    @Column(nullable = false)
    private LocalDateTime dateDemande;

    @PrePersist
    public void prePersist() {
        if (this.dateDemande == null) {
            this.dateDemande = LocalDateTime.now();
        }
    }
}
