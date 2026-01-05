package com.renault.garage.mapper;

import com.renault.garage.domain.Garage;
import com.renault.garage.dto.GarageRequest;
import com.renault.garage.dto.GarageSummaryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper pour Garage <-> DTOs.
 * componentModel = "spring" permet l'injection dans les services.
 *
 * Remarques:
 * - MapStruct mappe automatiquement les propriétés qui ont le même nom et un type compatible.
 * - Les conversions non triviales (ex: openingTimes <-> openingTimesByDay) sont ignorées ici.
 *   Si tu veux gérer openingTimes, fournis les classes OpeningTimeDto / GarageOpeningTime et
 *   je peux ajouter une méthode par défaut ou un OpeningTimeMapper utilisable via 'uses = { ... }'.
 */
@Mapper(componentModel = "spring")
public interface GarageMapper {

    // request -> entity (création)
    // openingTimes (List<GarageOpeningTime>) est incompatible avec openingTimesByDay (Map<DayOfWeek,List<OpeningTimeDto>>)
    // on ignore donc openingTimes ici ; city n'est pas fourni par GarageRequest
    @Mapping(target = "openingTimes", ignore = true)
    @Mapping(target = "city", ignore = true)
    Garage toEntity(GarageRequest req);

    // request -> entity (update) : ne pas écraser l'id existant, ignorer openingTimes et city
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "openingTimes", ignore = true)
    @Mapping(target = "city", ignore = true)
    void updateFromRequest(GarageRequest req, @MappingTarget Garage entity);

    // entity -> detail dto
    // conversion inverse openingTimes -> openingTimesByDay non gérée ici
    @Mapping(target = "openingTimesByDay", ignore = true)
    GarageRequest toDetailDto(Garage garage);

    // entity -> summary dto (champs identiques mappés automatiquement)
    GarageSummaryDto toSummaryDto(Garage garage);

    // summary dto -> entity (utile si tu veux reconstruire une entité à partir d'un summary)
    // on mappe id, name, address, telephone, email, city automatiquement ; on ignore openingTimes
    @Mapping(target = "openingTimes", ignore = true)
    Garage toEntity(GarageSummaryDto dto);
}