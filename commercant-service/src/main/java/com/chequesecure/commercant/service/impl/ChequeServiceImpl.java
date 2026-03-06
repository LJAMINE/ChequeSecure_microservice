package com.chequesecure.commercant.service.impl;

import com.chequesecure.commercant.dto.ChequeRequestDTO;
import com.chequesecure.commercant.dto.ChequeResponseDTO;
import com.chequesecure.commercant.entity.Cheque;
import com.chequesecure.commercant.mapper.ChequeMapper;
import com.chequesecure.commercant.repository.ChequeRepository;
import com.chequesecure.commercant.service.interf.ChequeService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class ChequeServiceImpl implements ChequeService {

    private final ChequeRepository chequeRepository;
    private final ChequeMapper chequeMapper;

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
    public ChequeResponseDTO updateCheque(String numeroCheque, ChequeRequestDTO request) {
        Cheque cheque = chequeRepository.findByNumeroCheque(numeroCheque)
                .orElseThrow(() -> new RuntimeException("Cheque not found"));
        chequeMapper.updateEntityFromDto(request, cheque);
        Cheque updated = chequeRepository.save(cheque);
        return chequeMapper.toResponseDTO(updated);
    }

    @Override
    public void deleteCheque(String numeroCheque) {
        Cheque cheque = chequeRepository.findByNumeroCheque(numeroCheque)
                .orElseThrow(() -> new RuntimeException("Cheque not found"));
        chequeRepository.delete(cheque);
    }
}
