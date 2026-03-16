package com.chequesecure.banquecentrale.repository;

import com.chequesecure.banquecentrale.entity.AgenceBancaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgenceBancaireRepository extends JpaRepository<AgenceBancaire, Long> {
    Optional<AgenceBancaire> findByCodeBanque(String codeBanque);
}
