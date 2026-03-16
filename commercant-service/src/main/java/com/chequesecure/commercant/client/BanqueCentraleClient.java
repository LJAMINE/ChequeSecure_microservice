package com.chequesecure.commercant.client;

import com.chequesecure.commercant.dto.CertificationRequestDTO;
import com.chequesecure.commercant.dto.CertificationResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Client OpenFeign pour appeler le service Banque Centrale.
 * Route les demandes de certification vers l'agence bancaire concernée.
 */
@FeignClient(
        name = "banque-centrale-service",
        path = "/api/banque-centrale",
        url = "${banque-centrale.url:}",
        fallback = BanqueCentraleClientFallback.class
)
public interface BanqueCentraleClient {

    @PostMapping("/certifications")
    CertificationResponseDTO demanderCertification(@RequestBody CertificationRequestDTO request);
}
