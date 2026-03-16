package com.chequesecure.banquecentrale.mapper;

import com.chequesecure.banquecentrale.dto.AgenceBancaireRequestDTO;
import com.chequesecure.banquecentrale.dto.AgenceBancaireResponseDTO;
import com.chequesecure.banquecentrale.dto.DemandeCertificationResponseDTO;
import com.chequesecure.banquecentrale.entity.AgenceBancaire;
import com.chequesecure.banquecentrale.entity.DemandeCertification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AgenceBancaireMapper {

    @Mapping(target = "id", ignore = true)
    AgenceBancaire toEntity(AgenceBancaireRequestDTO dto);

    AgenceBancaireResponseDTO toResponseDTO(AgenceBancaire entity);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AgenceBancaireRequestDTO dto, @MappingTarget AgenceBancaire entity);

    DemandeCertificationResponseDTO toDemandeResponseDTO(DemandeCertification entity);
}
