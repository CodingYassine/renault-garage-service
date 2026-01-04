package com.renault.garage.mapper;

import com.renault.garage.domain.Vehicle;
import com.renault.garage.dto.VehicleCreateRequest;
import com.renault.garage.dto.VehicleSummaryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    @Mapping(source = "garage.id", target = "garageId")
    VehicleSummaryDto toSummaryDto(Vehicle vehicle);

    // On ignore la relation JPA "garage" : on la gère explicitement dans le service
    @Mapping(target = "garage", ignore = true)
    Vehicle toEntity(VehicleCreateRequest req);

    @Mapping(target = "garage", ignore = true)
    void updateFromRequest(VehicleCreateRequest req, @MappingTarget Vehicle entity);
}