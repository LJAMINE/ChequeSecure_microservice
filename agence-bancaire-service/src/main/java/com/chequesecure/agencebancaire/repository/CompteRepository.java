package com.chequesecure.agencebancaire.repository;

import com.chequesecure.agencebancaire.entity.Compte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CompteRepository extends JpaRepository<Compte, Long>, JpaSpecificationExecutor<Compte> {
    Optional<Compte> findByNumeroCompte(String numeroCompte);
}
