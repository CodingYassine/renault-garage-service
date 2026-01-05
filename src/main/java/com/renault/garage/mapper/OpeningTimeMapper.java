package com.renault.garage.mapper;

import com.renault.garage.domain.Garage;
import com.renault.garage.domain.GarageOpeningTime;
import com.renault.garage.dto.OpeningTimeDto;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utilisé depuis GarageService pour construire et extraire les horaires d'ouverture.
 */
@Component
public class OpeningTimeMapper {

    public List<GarageOpeningTime> toEntities(Map<DayOfWeek, List<OpeningTimeDto>> byDay, Garage garage) {
        if (byDay == null) return Collections.emptyList();
        List<GarageOpeningTime> list = new ArrayList<>();
        byDay.forEach((day, dtos) -> {
            if (dtos != null) {
                for (OpeningTimeDto dto : dtos) {
                    GarageOpeningTime got = GarageOpeningTime.builder()
                            .dayOfWeek(day)
                            .startTime(dto.getStartTime())
                            .endTime(dto.getEndTime())
                            .garage(garage)
                            .build();
                    list.add(got);
                }
            }
        });
        return list;
    }

    public Map<DayOfWeek, List<OpeningTimeDto>> toDtoMap(List<GarageOpeningTime> openingTimes) {
        if (openingTimes == null) return Collections.emptyMap();
        return openingTimes.stream()
                .collect(Collectors.groupingBy(
                        GarageOpeningTime::getDayOfWeek,
                        Collectors.mapping(
                                got -> new OpeningTimeDto(got.getStartTime(), got.getEndTime()),
                                Collectors.toList()
                        )
                ));
    }
}