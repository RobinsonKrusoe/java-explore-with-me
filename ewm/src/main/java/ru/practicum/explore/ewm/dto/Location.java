package ru.practicum.explore.ewm.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Широта и долгота места проведения события
 */

@Data
@Builder
public class Location {
    private BigDecimal lat; //Широта
    private BigDecimal lon; //Долгота
}
