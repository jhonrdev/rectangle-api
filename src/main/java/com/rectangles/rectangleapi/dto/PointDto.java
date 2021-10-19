package com.rectangles.rectangleapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointDto {
    @NotNull(message = "x value cannot be null")
    @Min(value = 0, message = "Positive integer values only")
    private Integer x;

    @NotNull(message = "y value cannot be null")
    @Min(value = 0, message = "Positive integer values only")
    private Integer y;
}
