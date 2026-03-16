package com.chequesecure.commercant.client;

import com.chequesecure.commercant.dto.CertificationRequestDTO;
import com.chequesecure.commercant.dto.CertificationResponseDTO;
import org.springframework.stereotype.Component;

/**
 * Fallback lorsque le service Banque Centrale est indisponible.
 */
@Component
public class BanqueCentraleClientFallback implements BanqueCentraleClient {

    @Override
    public CertificationResponseDTO demanderCertification(CertificationRequestDTO request) {
        return CertificationResponseDTO.builder()
                .statut("REFUSE")
                .motifRefus("Service Banque Centrale temporairement indisponible")
                .build();
    }
}
