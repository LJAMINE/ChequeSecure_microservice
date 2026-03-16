package com.chequesecure.agencebancaire.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comptes", uniqueConstraints = @UniqueConstraint(columnNames = "numeroCompte"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroCompte;

    @Column(nullable = false)
    private Double solde;

    @Column(nullable = false)
    private String referenceClient;

    @Column(nullable = false)
    private String nomClient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Column(nullable = false)
    private LocalDate dateOuverture;

    @Column(nullable = false)
    @Builder.Default
    private Boolean actif = true;

    @OneToMany(mappedBy = "compte", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Operation> operations = new ArrayList<>();

    public enum Type {
        COURANT,
        EPARGNE
    }
}
