package com.chequesecure.commercant.controller;

import com.chequesecure.commercant.dto.ChequeRequestDTO;
import com.chequesecure.commercant.dto.ChequeResponseDTO;
import com.chequesecure.commercant.service.interf.ChequeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/commercant/cheques")
public class ChequeController {

    @Autowired
    private ChequeService chequeService;

    @PostMapping
    public ResponseEntity<ChequeResponseDTO> createCheque(@Validated @RequestBody ChequeRequestDTO request) {
        return ResponseEntity.ok(chequeService.createCheque(request));
    }

    @GetMapping("/{numeroCheque}")
    public ResponseEntity<ChequeResponseDTO> getCheque(@PathVariable String numeroCheque) {
        return ResponseEntity.ok(chequeService.getChequeByNumero(numeroCheque));
    }

    @GetMapping
    public ResponseEntity<List<ChequeResponseDTO>> getAllCheques(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(chequeService.getAllCheques(page, size));
    }

    @PutMapping("/{numeroCheque}")
    public ResponseEntity<ChequeResponseDTO> updateCheque(@PathVariable String numeroCheque,
                                                         @Validated @RequestBody ChequeRequestDTO request) {
        return ResponseEntity.ok(chequeService.updateCheque(numeroCheque, request));
    }

    @DeleteMapping("/{numeroCheque}")
    public ResponseEntity<Void> deleteCheque(@PathVariable String numeroCheque) {
        chequeService.deleteCheque(numeroCheque);
        return ResponseEntity.noContent().build();
    }
}
