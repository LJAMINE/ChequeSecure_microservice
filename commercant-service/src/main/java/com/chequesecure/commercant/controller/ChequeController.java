package com.chequesecure.commercant.controller;

import com.chequesecure.commercant.dto.ChequeRequestDTO;
import com.chequesecure.commercant.dto.ChequeResponseDTO;
import com.chequesecure.commercant.service.interf.ChequeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/commercant/cheques")
@Tag(name = "Chèques", description = "Gestion des chèques : saisie, consultation, certification")
public class ChequeController {

    @Autowired
    private ChequeService chequeService;

    @PostMapping
    @Operation(summary = "Créer un chèque", description = "Saisir les informations d'un chèque (numéro, RIB, montant, nom du client)")
    public ResponseEntity<ChequeResponseDTO> createCheque(@Validated @RequestBody ChequeRequestDTO request) {
        return ResponseEntity.ok(chequeService.createCheque(request));
    }

    @GetMapping("/{numeroCheque}")
    @Operation(summary = "Détail d'un chèque", description = "Consulter le détail d'un chèque et son statut de certification")
    public ResponseEntity<ChequeResponseDTO> getCheque(
            @Parameter(description = "Numéro unique du chèque") @PathVariable String numeroCheque) {
        return ResponseEntity.ok(chequeService.getChequeByNumero(numeroCheque));
    }

    @GetMapping
    @Operation(summary = "Liste des chèques", description = "Consulter la liste de tous les chèques avec pagination")
    public ResponseEntity<List<ChequeResponseDTO>> getAllCheques(
            @Parameter(description = "Numéro de page (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Taille de la page") @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(chequeService.getAllCheques(page, size));
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des chèques", description = "Rechercher par numéro, nom du client ou statut")
    public ResponseEntity<List<ChequeResponseDTO>> searchCheques(
            @Parameter(description = "Numéro du chèque (recherche partielle)") @RequestParam(required = false) String numeroCheque,
            @Parameter(description = "Nom du client (recherche partielle)") @RequestParam(required = false) String nomClient,
            @Parameter(description = "Statut : EN_ATTENTE, CERTIFIE, REFUSE") @RequestParam(required = false) String statut,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(chequeService.searchCheques(numeroCheque, nomClient, statut, page, size));
    }

    @PostMapping("/{numeroCheque}/certifier")
    @Operation(summary = "Soumettre à certification", description = "Soumettre un chèque à la Banque Centrale pour demande de certification")
    public ResponseEntity<ChequeResponseDTO> submitCertification(
            @Parameter(description = "Numéro du chèque à certifier") @PathVariable String numeroCheque) {
        return ResponseEntity.ok(chequeService.submitForCertification(numeroCheque));
    }

    @PutMapping("/{numeroCheque}")
    @Operation(summary = "Modifier un chèque", description = "Mettre à jour les informations d'un chèque (uniquement si EN_ATTENTE)")
    public ResponseEntity<ChequeResponseDTO> updateCheque(
            @Parameter(description = "Numéro du chèque") @PathVariable String numeroCheque,
            @Validated @RequestBody ChequeRequestDTO request) {
        return ResponseEntity.ok(chequeService.updateCheque(numeroCheque, request));
    }

    @DeleteMapping("/{numeroCheque}")
    @Operation(summary = "Supprimer un chèque", description = "Supprimer un chèque (uniquement si EN_ATTENTE)")
    public ResponseEntity<Void> deleteCheque(
            @Parameter(description = "Numéro du chèque") @PathVariable String numeroCheque) {
        chequeService.deleteCheque(numeroCheque);
        return ResponseEntity.noContent().build();
    }
}
