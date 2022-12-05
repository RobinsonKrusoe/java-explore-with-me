package ru.practicum.explore.ewm.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Широта и долгота места проведения события
 */

@Getter
@Setter
@Builder
public class Location {
    private BigDecimal lat; //Широта
    private BigDecimal lon; //Долгота
}
