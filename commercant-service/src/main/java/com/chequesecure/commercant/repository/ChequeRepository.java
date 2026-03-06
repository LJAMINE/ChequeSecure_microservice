package com.chequesecure.commercant.repository;

import com.chequesecure.commercant.entity.Cheque;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ChequeRepository extends JpaRepository<Cheque, Long> {
    Optional<Cheque> findByNumeroCheque(String numeroCheque);
}
