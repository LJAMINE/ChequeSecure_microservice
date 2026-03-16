package com.chequesecure.agencebancaire.service;

import com.chequesecure.agencebancaire.dto.*;
import com.chequesecure.agencebancaire.entity.Compte;
import com.chequesecure.agencebancaire.entity.Operation;
import com.chequesecure.agencebancaire.mapper.CompteMapper;
import com.chequesecure.agencebancaire.repository.CompteRepository;
import com.chequesecure.agencebancaire.repository.OperationRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CompteService {

    private final CompteRepository compteRepository;
    private final OperationRepository operationRepository;
    private final CompteMapper compteMapper;

    public CompteResponseDTO createCompte(CompteRequestDTO request) {
        Compte compte = compteMapper.toEntity(request);
        Compte saved = compteRepository.save(compte);
        return compteMapper.toResponseDTO(saved);
    }

    public CompteResponseDTO getCompteByNumero(String numeroCompte) {
        Compte compte = compteRepository.findByNumeroCompte(numeroCompte)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé"));
        return compteMapper.toResponseDTO(compte);
    }

    public List<CompteResponseDTO> getAllComptes(int page, int size) {
        return compteRepository.findAll(PageRequest.of(page, size))
                .stream()
                .map(compteMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CompteResponseDTO updateCompte(String numeroCompte, CompteRequestDTO request) {
        Compte compte = compteRepository.findByNumeroCompte(numeroCompte)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé"));
        compteMapper.updateEntityFromDto(request, compte);
        Compte updated = compteRepository.save(compte);
        return compteMapper.toResponseDTO(updated);
    }

    public void deleteCompte(String numeroCompte) {
        Compte compte = compteRepository.findByNumeroCompte(numeroCompte)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé"));
        compteRepository.delete(compte);
    }

    public CertificationResponseDTO certifierCheque(String numeroCompte, CertificationRequestDTO request) {
        Compte compte = compteRepository.findByNumeroCompte(numeroCompte)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé"));

        if (!compte.getActif()) {
            return CertificationResponseDTO.builder()
                    .statut("REFUSE")
                    .motifRefus("Compte inactif")
                    .build();
        }

        if (compte.getSolde() < request.getMontant()) {
            return CertificationResponseDTO.builder()
                    .statut("REFUSE")
                    .motifRefus("Solde insuffisant")
                    .build();
        }

        double soldeAvant = compte.getSolde();
        double soldeApres = soldeAvant - request.getMontant();

        compte.setSolde(soldeApres);
        compteRepository.save(compte);

        Operation operation = Operation.builder()
                .date(LocalDateTime.now())
                .montant(request.getMontant())
                .numeroCheque(request.getNumeroCheque())
                .type(Operation.Type.CERTIFICATION_CHEQUE)
                .description("Certification chèque " + request.getNumeroCheque())
                .soldeAvant(soldeAvant)
                .soldeApres(soldeApres)
                .compte(compte)
                .build();
        operationRepository.save(operation);

        return CertificationResponseDTO.builder()
                .statut("CERTIFIE")
                .build();
    }

    public List<OperationResponseDTO> getOperationsByCompte(String numeroCompte, String type, LocalDateTime dateFrom, LocalDateTime dateTo, int page, int size) {
        Compte compte = compteRepository.findByNumeroCompte(numeroCompte)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé"));

        Specification<Operation> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("compte"), compte));

            if (type != null && !type.isBlank()) {
                try {
                    Operation.Type t = Operation.Type.valueOf(type);
                    predicates.add(cb.equal(root.get("type"), t));
                } catch (IllegalArgumentException ignored) {}
            }
            if (dateFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), dateFrom));
            }
            if (dateTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("date"), dateTo));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return operationRepository.findAll(spec, PageRequest.of(page, size))
                .stream()
                .map(compteMapper::toOperationResponseDTO)
                .collect(Collectors.toList());
    }
}
