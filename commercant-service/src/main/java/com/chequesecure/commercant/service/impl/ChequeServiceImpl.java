package com.chequesecure.commercant.service.impl;

import com.chequesecure.commercant.client.BanqueCentraleClient;
import com.chequesecure.commercant.dto.CertificationRequestDTO;
import com.chequesecure.commercant.dto.CertificationResponseDTO;
import com.chequesecure.commercant.dto.ChequeRequestDTO;
import com.chequesecure.commercant.dto.ChequeResponseDTO;
import com.chequesecure.commercant.entity.Cheque;
import com.chequesecure.commercant.mapper.ChequeMapper;
import com.chequesecure.commercant.repository.ChequeRepository;
import com.chequesecure.commercant.service.interf.ChequeService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class ChequeServiceImpl implements ChequeService {

    private final ChequeRepository chequeRepository;
    private final ChequeMapper chequeMapper;
    private final BanqueCentraleClient banqueCentraleClient;

    @Override
    public ChequeResponseDTO createCheque(ChequeRequestDTO request) {
        Cheque cheque = chequeMapper.toEntity(request);
        cheque.setStatut(Cheque.Statut.EN_ATTENTE);
        Cheque saved = chequeRepository.save(cheque);
        return chequeMapper.toResponseDTO(saved);
    }

    @Override
    public ChequeResponseDTO getChequeByNumero(String numeroCheque) {
        Cheque cheque = chequeRepository.findByNumeroCheque(numeroCheque)
                .orElseThrow(() -> new RuntimeException("Cheque not found"));
        return chequeMapper.toResponseDTO(cheque);
    }

    @Override
    public List<ChequeResponseDTO> getAllCheques(int page, int size) {
        return chequeRepository.findAll(PageRequest.of(page, size))
                .stream()
                .map(chequeMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChequeResponseDTO> searchCheques(String numeroCheque, String nomClient, String statut, int page, int size) {
        Specification<Cheque> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (numeroCheque != null && !numeroCheque.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("numeroCheque")), "%" + numeroCheque.toLowerCase() + "%"));
            }
            if (nomClient != null && !nomClient.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("nomClient")), "%" + nomClient.toLowerCase() + "%"));
            }
            if (statut != null && !statut.isBlank()) {
                try {
                    Cheque.Statut s = Cheque.Statut.valueOf(statut);
                    predicates.add(cb.equal(root.get("statut"), s));
                } catch (IllegalArgumentException ignored) {
                    // Statut invalide ignoré
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return chequeRepository.findAll(spec, PageRequest.of(page, size))
                .stream()
                .map(chequeMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ChequeResponseDTO submitForCertification(String numeroCheque) {
        Cheque cheque = chequeRepository.findByNumeroCheque(numeroCheque)
                .orElseThrow(() -> new RuntimeException("Cheque not found"));

        if (cheque.getStatut() != Cheque.Statut.EN_ATTENTE) {
            throw new IllegalStateException("Seul un chèque en attente peut être soumis à certification");
        }

        CertificationRequestDTO request = CertificationRequestDTO.builder()
                .numeroCheque(cheque.getNumeroCheque())
                .codeBanque(cheque.getCodeBanque())
                .numeroCompte(cheque.getNumeroCompte())
                .cleRib(cheque.getCleRib())
                .nomClient(cheque.getNomClient())
                .montant(cheque.getMontant())
                .build();

        CertificationResponseDTO response = banqueCentraleClient.demanderCertification(request);

        cheque.setDateCertification(LocalDateTime.now());
        cheque.setStatut(Cheque.Statut.valueOf(response.getStatut()));
        cheque.setMotifRefus(response.getMotifRefus());
        Cheque updated = chequeRepository.save(cheque);

        return chequeMapper.toResponseDTO(updated);
    }

    @Override
    public ChequeResponseDTO updateCheque(String numeroCheque, ChequeRequestDTO request) {
        Cheque cheque = chequeRepository.findByNumeroCheque(numeroCheque)
                .orElseThrow(() -> new RuntimeException("Cheque not found"));
        if (cheque.getStatut() != Cheque.Statut.EN_ATTENTE) {
            throw new IllegalStateException("Seul un chèque en attente peut être modifié");
        }
        chequeMapper.updateEntityFromDto(request, cheque);
        Cheque updated = chequeRepository.save(cheque);
        return chequeMapper.toResponseDTO(updated);
    }

    @Override
    public void deleteCheque(String numeroCheque) {
        Cheque cheque = chequeRepository.findByNumeroCheque(numeroCheque)
                .orElseThrow(() -> new RuntimeException("Cheque not found"));
        if (cheque.getStatut() != Cheque.Statut.EN_ATTENTE) {
            throw new IllegalStateException("Seul un chèque en attente peut être supprimé");
        }
        chequeRepository.delete(cheque);
    }
}
