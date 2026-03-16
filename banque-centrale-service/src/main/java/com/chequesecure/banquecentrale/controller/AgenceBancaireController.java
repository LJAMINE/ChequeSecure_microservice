package com.chequesecure.banquecentrale.controller;

import com.chequesecure.banquecentrale.dto.AgenceBancaireRequestDTO;
import com.chequesecure.banquecentrale.dto.AgenceBancaireResponseDTO;
import com.chequesecure.banquecentrale.service.AgenceBancaireService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/banque-centrale/agences")
@RequiredArgsConstructor
@Tag(name = "Agences Bancaires", description = "CRUD des agences bancaires")
public class AgenceBancaireController {

    private final AgenceBancaireService agenceService;

    @PostMapping
    @Operation(summary = "Créer une agence", description = "Créer une nouvelle agence bancaire")
    public ResponseEntity<AgenceBancaireResponseDTO> create(@Valid @RequestBody AgenceBancaireRequestDTO request) {
        return ResponseEntity.ok(agenceService.create(request));
    }

    @GetMapping("/{codeBanque}")
    @Operation(summary = "Détail d'une agence", description = "Consulter une agence par son code banque")
    public ResponseEntity<AgenceBancaireResponseDTO> getByCodeBanque(
            @Parameter(description = "Code banque (3 chiffres)") @PathVariable String codeBanque) {
        return ResponseEntity.ok(agenceService.getByCodeBanque(codeBanque));
    }

    @GetMapping
    @Operation(summary = "Liste des agences", description = "Consulter toutes les agences avec pagination")
    public ResponseEntity<List<AgenceBancaireResponseDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(agenceService.getAll(page, size));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une agence", description = "Mettre à jour une agence")
    public ResponseEntity<AgenceBancaireResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody AgenceBancaireRequestDTO request) {
        return ResponseEntity.ok(agenceService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une agence", description = "Supprimer une agence")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        agenceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
