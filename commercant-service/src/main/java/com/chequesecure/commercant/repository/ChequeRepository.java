package com.chequesecure.commercant.repository;

import com.chequesecure.commercant.entity.Cheque;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

public interface ChequeRepository extends JpaRepository<Cheque, Long>, JpaSpecificationExecutor<Cheque> {
    Optional<Cheque> findByNumeroCheque(String numeroCheque);
}
