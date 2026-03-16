package com.chequesecure.banquecentrale.controller;

import com.chequesecure.banquecentrale.dto.CertificationRequestDTO;
import com.chequesecure.banquecentrale.dto.CertificationResponseDTO;
import com.chequesecure.banquecentrale.dto.DemandeCertificationResponseDTO;
import com.chequesecure.banquecentrale.service.CertificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/banque-centrale")
@RequiredArgsConstructor
@Tag(name = "Certification", description = "Demandes de certification de chèques")
public class CertificationController {

    private final CertificationService certificationService;

    @PostMapping("/certifications")
    @Operation(summary = "Demander une certification", description = "Reçoit une demande du commerçant, route vers l'agence bancaire")
    public ResponseEntity<CertificationResponseDTO> demanderCertification(@RequestBody CertificationRequestDTO request) {
        return ResponseEntity.ok(certificationService.demanderCertification(request));
    }

    @GetMapping("/certifications/historique")
    @Operation(summary = "Historique des certifications", description = "Consulter l'historique des demandes de certification traitées")
    public ResponseEntity<List<DemandeCertificationResponseDTO>> getHistorique(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(certificationService.getHistoriqueCertifications(page, size));
    }
}
