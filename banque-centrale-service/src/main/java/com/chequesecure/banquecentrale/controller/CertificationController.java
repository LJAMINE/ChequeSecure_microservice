package com.chequesecure.banquecentrale.controller;

import com.chequesecure.banquecentrale.dto.CertificationRequestDTO;
import com.chequesecure.banquecentrale.dto.CertificationResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoint de certification - reçoit les demandes du commerçant.
 * Stub initial : l'orchestration complète (appel agence via OpenFeign) sera implémentée
 * lors du développement du module Banque Centrale.
 */
@RestController
@RequestMapping("/api/banque-centrale")
@Tag(name = "Certification", description = "Demandes de certification de chèques")
public class CertificationController {

    @PostMapping("/certifications")
    @Operation(summary = "Demander une certification", description = "Reçoit une demande du commerçant, route vers l'agence bancaire")
    public ResponseEntity<CertificationResponseDTO> demanderCertification(@RequestBody CertificationRequestDTO request) {
        // Stub : AgenceBancaire et OpenFeign vers agence seront implémentés dans l'étape suivante
        CertificationResponseDTO response = CertificationResponseDTO.builder()
                .statut("REFUSE")
                .motifRefus("Agence bancaire non encore configurée pour le code " + request.getCodeBanque())
                .build();
        return ResponseEntity.ok(response);
    }
}
