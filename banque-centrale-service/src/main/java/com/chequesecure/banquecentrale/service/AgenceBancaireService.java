package com.chequesecure.banquecentrale.service;

import com.chequesecure.banquecentrale.dto.AgenceBancaireRequestDTO;
import com.chequesecure.banquecentrale.dto.AgenceBancaireResponseDTO;
import com.chequesecure.banquecentrale.entity.AgenceBancaire;
import com.chequesecure.banquecentrale.mapper.AgenceBancaireMapper;
import com.chequesecure.banquecentrale.repository.AgenceBancaireRepository;
import com.chequesecure.banquecentrale.repository.DemandeCertificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AgenceBancaireService {

    private final AgenceBancaireRepository agenceRepository;
    private final DemandeCertificationRepository demandeRepository;
    private final AgenceBancaireMapper mapper;

    public AgenceBancaireResponseDTO create(AgenceBancaireRequestDTO request) {
        AgenceBancaire agence = mapper.toEntity(request);
        AgenceBancaire saved = agenceRepository.save(agence);
        return mapper.toResponseDTO(saved);
    }

    public AgenceBancaireResponseDTO getByCodeBanque(String codeBanque) {
        AgenceBancaire agence = agenceRepository.findByCodeBanque(codeBanque)
                .orElseThrow(() -> new RuntimeException("Agence non trouvée pour le code " + codeBanque));
        return mapper.toResponseDTO(agence);
    }

    public List<AgenceBancaireResponseDTO> getAll(int page, int size) {
        return agenceRepository.findAll(PageRequest.of(page, size))
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public AgenceBancaireResponseDTO update(Long id, AgenceBancaireRequestDTO request) {
        AgenceBancaire agence = agenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agence non trouvée"));
        mapper.updateEntityFromDto(request, agence);
        AgenceBancaire updated = agenceRepository.save(agence);
        return mapper.toResponseDTO(updated);
    }

    public void delete(Long id) {
        AgenceBancaire agence = agenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agence non trouvée"));
        // Règle: pas de suppression si des certifications sont associées
        long count = demandeRepository.countByCodeBanque(agence.getCodeBanque());
        if (count > 0) {
            throw new IllegalStateException("Impossible de supprimer l'agence : " + count + " certification(s) associée(s)");
        }
        agenceRepository.deleteById(id);
    }

    public AgenceBancaire findAgenceByCodeBanque(String codeBanque) {
        return agenceRepository.findByCodeBanque(codeBanque)
                .orElse(null);
    }
}
