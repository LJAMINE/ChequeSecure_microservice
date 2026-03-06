package com.chequesecure.commercant.service.interf;

import com.chequesecure.commercant.dto.ChequeRequestDTO;
import com.chequesecure.commercant.dto.ChequeResponseDTO;
import java.util.List;

public interface ChequeService {
    ChequeResponseDTO createCheque(ChequeRequestDTO request);
    ChequeResponseDTO getChequeByNumero(String numeroCheque);
    List<ChequeResponseDTO> getAllCheques(int page, int size);
    ChequeResponseDTO updateCheque(String numeroCheque, ChequeRequestDTO request);
    void deleteCheque(String numeroCheque);
}
