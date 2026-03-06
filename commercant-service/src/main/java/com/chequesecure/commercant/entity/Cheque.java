package com.chequesecure.commercant.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "cheques", uniqueConstraints = @UniqueConstraint(columnNames = "numeroCheque"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cheque {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroCheque;

    @Column(nullable = false, length = 3)
    private String codeBanque;

    @Column(nullable = false)
    private String numeroCompte;

    @Column(nullable = false, length = 2)
    private String cleRib;

    @Column(nullable = false)
    private String nomClient;

    @Column(nullable = false)
    private Double montant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Statut statut = Statut.EN_ATTENTE;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateCertification;

    @Column(length = 255)
    private String motifRefus;

    @PrePersist
    public void prePersist() {
        this.dateCreation = LocalDateTime.now();
    }

    public enum Statut {
        EN_ATTENTE,
        CERTIFIE,
        REFUSE
    }
}
