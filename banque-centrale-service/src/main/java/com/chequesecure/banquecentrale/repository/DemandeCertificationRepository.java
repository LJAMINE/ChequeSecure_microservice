package com.chequesecure.banquecentrale.repository;

import com.chequesecure.banquecentrale.entity.DemandeCertification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemandeCertificationRepository extends JpaRepository<DemandeCertification, Long> {
    Page<DemandeCertification> findAllByOrderByDateDemandeDesc(Pageable pageable);
    long countByCodeBanque(String codeBanque);
}
