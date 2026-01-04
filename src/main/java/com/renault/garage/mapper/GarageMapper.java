package com.renault.garage.mapper;

import com.renault.garage.domain.Garage;
import com.renault.garage.dto.GarageRequest;
import com.renault.garage.dto.GarageSummaryDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper pour Garage <-> DTOs.
 * componentModel = "spring" permet l'injection dans les services.
 */
@Mapper(componentModel = "spring")
public interface GarageMapper {
    // request -> entity (new)
    Garage toEntity(GarageRequest req);

    // update existing entity from request
    void updateFromRequest(GarageRequest req, @MappingTarget Garage entity);

    // entity -> detail dto
    GarageRequest toDetailDto(Garage garage);

    // entity -> summary dto
    GarageSummaryDto toSummaryDto(Garage garage);
}