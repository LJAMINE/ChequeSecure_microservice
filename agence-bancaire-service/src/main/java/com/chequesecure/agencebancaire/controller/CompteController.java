package com.chequesecure.agencebancaire.controller;

import com.chequesecure.agencebancaire.dto.*;
import com.chequesecure.agencebancaire.service.CompteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/agence/comptes")
@RequiredArgsConstructor
@Tag(name = "Comptes", description = "Gestion des comptes bancaires")
public class CompteController {

    private final CompteService compteService;

    @PostMapping
    @Operation(summary = "Créer un compte", description = "Créer un nouveau compte bancaire")
    public ResponseEntity<CompteResponseDTO> createCompte(@Valid @RequestBody CompteRequestDTO request) {
        return ResponseEntity.ok(compteService.createCompte(request));
    }

    @GetMapping("/{numeroCompte}")
    @Operation(summary = "Détail d'un compte", description = "Consulter le détail d'un compte")
    public ResponseEntity<CompteResponseDTO> getCompte(
            @Parameter(description = "Numéro du compte") @PathVariable String numeroCompte) {
        return ResponseEntity.ok(compteService.getCompteByNumero(numeroCompte));
    }

    @GetMapping
    @Operation(summary = "Liste des comptes", description = "Consulter la liste des comptes avec pagination")
    public ResponseEntity<List<CompteResponseDTO>> getAllComptes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(compteService.getAllComptes(page, size));
    }

    @PutMapping("/{numeroCompte}")
    @Operation(summary = "Modifier un compte", description = "Mettre à jour les informations d'un compte")
    public ResponseEntity<CompteResponseDTO> updateCompte(
            @PathVariable String numeroCompte,
            @Valid @RequestBody CompteRequestDTO request) {
        return ResponseEntity.ok(compteService.updateCompte(numeroCompte, request));
    }

    @DeleteMapping("/{numeroCompte}")
    @Operation(summary = "Supprimer un compte", description = "Supprimer un compte")
    public ResponseEntity<Void> deleteCompte(@PathVariable String numeroCompte) {
        compteService.deleteCompte(numeroCompte);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{numeroCompte}/certifier")
    @Operation(summary = "Certifier un chèque", description = "Vérifier le solde, réserver le montant et enregistrer l'opération CERTIFICATION_CHEQUE")
    public ResponseEntity<CertificationResponseDTO> certifierCheque(
            @Parameter(description = "Numéro du compte") @PathVariable String numeroCompte,
            @RequestBody CertificationRequestDTO request) {
        return ResponseEntity.ok(compteService.certifierCheque(numeroCompte, request));
    }

    @GetMapping("/{numeroCompte}/operations")
    @Operation(summary = "Historique des opérations", description = "Consulter l'historique des opérations d'un compte avec filtres")
    public ResponseEntity<List<OperationResponseDTO>> getOperations(
            @PathVariable String numeroCompte,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(compteService.getOperationsByCompte(numeroCompte, type, dateFrom, dateTo, page, size));
    }
}
