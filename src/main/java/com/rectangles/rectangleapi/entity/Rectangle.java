package com.rectangles.rectangleapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Rectangle")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rectangle {
    @Id
    @GeneratedValue
    @Min(value = 0, message = "Positive integer values only")
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
