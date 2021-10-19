package com.rectangles.rectangleapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RectangleDto {
    @NotNull
    @Min(1)
    private Integer id;

    @NotNull(message = "bottomLeftX value cannot be null")
    @Min(value = 0, message = "Positive integer values only")
    private Integer bottomLeftX;

    @NotNull(message = "bottomLeftY value cannot be null")
    @Min(value = 0, message = "Positive integer values only")
    private Integer bottomLeftY;

    @NotNull(message = "topRightX value cannot be null")
    @Min(value = 0, message = "Positive integer values only")
    private Integer topRightX;

    @NotNull(message = "topRightY value cannot be null")
    @Min(value = 0, message = "Positive integer values only")
    private Integer topRightY;
}
