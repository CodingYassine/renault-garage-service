package com.renault.garage.mapper;

import com.renault.garage.domain.Accessory;
import com.renault.garage.dto.AccessoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AccessoryMapper {

    @Mapping(source = "vehicle.id", target = "vehicleId")
    AccessoryResponse toDto(Accessory accessory);

    @Mapping(target = "vehicle", ignore = true)
    Accessory toEntity(AccessoryResponse req);

    @Mapping(target = "vehicle", ignore = true)
    void updateFromRequest(AccessoryResponse req, @MappingTarget Accessory entity);
}