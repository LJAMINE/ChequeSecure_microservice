package com.chequesecure.banquecentrale.service;

import com.chequesecure.banquecentrale.dto.CertificationRequestDTO;
import com.chequesecure.banquecentrale.dto.CertificationResponseDTO;
import com.chequesecure.banquecentrale.dto.DemandeCertificationResponseDTO;
import com.chequesecure.banquecentrale.entity.AgenceBancaire;
import com.chequesecure.banquecentrale.entity.DemandeCertification;
import com.chequesecure.banquecentrale.mapper.AgenceBancaireMapper;
import com.chequesecure.banquecentrale.repository.DemandeCertificationRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CertificationService {

    private final AgenceBancaireService agenceService;
    private final DemandeCertificationRepository demandeRepository;
    private final AgenceBancaireMapper mapper;
    private final RestTemplate restTemplate;

    @CircuitBreaker(name = "agenceBancaireService", fallbackMethod = "certifierFallback")
    @Retry(name = "agenceBancaireService")
    public CertificationResponseDTO demanderCertification(CertificationRequestDTO request) {
        AgenceBancaire agence = agenceService.findAgenceByCodeBanque(request.getCodeBanque());

        if (agence == null || !agence.getActif()) {
            return enregistrerEtRetourner(request, "REFUSE", "Agence non trouvée ou inactive pour le code " + request.getCodeBanque());
        }

        String url = agence.getUrlServiceWeb().replaceAll("/$", "") + "/api/agence/comptes/" + request.getNumeroCompte() + "/certifier";
        CertificationResponseDTO response;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<CertificationRequestDTO> entity = new HttpEntity<>(request, headers);
            response = restTemplate.postForObject(url, entity, CertificationResponseDTO.class);
        } catch (Exception e) {
            log.warn("Erreur appel agence {}: {}", agence.getCodeBanque(), e.getMessage());
            return enregistrerEtRetourner(request, "REFUSE", "Erreur lors de l'appel à l'agence: " + e.getMessage());
        }

        enregistrerDemande(request, response.getStatut(), response.getMotifRefus());
        return response;
    }

    private CertificationResponseDTO certifierFallback(CertificationRequestDTO request, Exception ex) {
        log.warn("Fallback circuit breaker pour certification: {}", ex.getMessage());
        return enregistrerEtRetourner(request, "REFUSE", "Service agence temporairement indisponible");
    }

    private CertificationResponseDTO enregistrerEtRetourner(CertificationRequestDTO request, String statut, String motifRefus) {
        enregistrerDemande(request, statut, motifRefus);
        return CertificationResponseDTO.builder()
                .statut(statut)
                .motifRefus(motifRefus)
                .build();
    }

    private void enregistrerDemande(CertificationRequestDTO request, String statut, String motifRefus) {
        DemandeCertification demande = DemandeCertification.builder()
                .numeroCheque(request.getNumeroCheque())
                .codeBanque(request.getCodeBanque())
                .numeroCompte(request.getNumeroCompte())
                .nomClient(request.getNomClient())
                .montant(request.getMontant())
                .statut(statut)
                .motifRefus(motifRefus)
                .build();
        demandeRepository.save(demande);
    }

    public List<DemandeCertificationResponseDTO> getHistoriqueCertifications(int page, int size) {
        return demandeRepository.findAllByOrderByDateDemandeDesc(PageRequest.of(page, size))
                .stream()
                .map(mapper::toDemandeResponseDTO)
                .collect(Collectors.toList());
    }
}
