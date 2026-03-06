package com.chequesecure.commercant.mapper;

import com.chequesecure.commercant.entity.Cheque;
import com.chequesecure.commercant.dto.ChequeRequestDTO;
import com.chequesecure.commercant.dto.ChequeResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChequeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "statut", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateCertification", ignore = true)
    @Mapping(target = "motifRefus", ignore = true)
    Cheque toEntity(ChequeRequestDTO dto);

    ChequeResponseDTO toResponseDTO(Cheque entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "statut", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateCertification", ignore = true)
    @Mapping(target = "motifRefus", ignore = true)
    void updateEntityFromDto(ChequeRequestDTO dto, @MappingTarget Cheque entity);
}
