package com.chequesecure.agencebancaire.repository;

import com.chequesecure.agencebancaire.entity.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OperationRepository extends JpaRepository<Operation, Long>, JpaSpecificationExecutor<Operation> {
    Page<Operation> findByCompteId(Long compteId, Pageable pageable);
}
