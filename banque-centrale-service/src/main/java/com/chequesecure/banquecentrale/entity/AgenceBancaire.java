package com.chequesecure.banquecentrale.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "agences_bancaires", uniqueConstraints = @UniqueConstraint(columnNames = "codeBanque"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgenceBancaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String ville;

    @Column(nullable = false)
    private String adresse;

    @Column(nullable = false, unique = true, length = 3)
    private String codeBanque;

    @Column(nullable = false)
    private String urlServiceWeb;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String telephone;

    @Column(nullable = false)
    @Builder.Default
    private Boolean actif = true;
}
