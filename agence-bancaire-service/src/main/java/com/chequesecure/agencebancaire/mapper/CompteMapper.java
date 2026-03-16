package com.chequesecure.agencebancaire.mapper;

import com.chequesecure.agencebancaire.dto.CompteRequestDTO;
import com.chequesecure.agencebancaire.dto.CompteResponseDTO;
import com.chequesecure.agencebancaire.dto.OperationResponseDTO;
import com.chequesecure.agencebancaire.entity.Compte;
import com.chequesecure.agencebancaire.entity.Operation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "operations", ignore = true)
    Compte toEntity(CompteRequestDTO dto);

    CompteResponseDTO toResponseDTO(Compte entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "operations", ignore = true)
    void updateEntityFromDto(CompteRequestDTO dto, @MappingTarget Compte entity);

    OperationResponseDTO toOperationResponseDTO(Operation operation);
}
