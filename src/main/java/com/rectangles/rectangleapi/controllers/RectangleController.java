package com.rectangles.rectangleapi.controllers;

import com.rectangles.rectangleapi.api.validation.constraints.MaxSizeConstraint;
import com.rectangles.rectangleapi.dto.PointDto;
import com.rectangles.rectangleapi.dto.RectangleDto;
import com.rectangles.rectangleapi.dto.SaveRectangleDto;
import com.rectangles.rectangleapi.services.RectangleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Validated
@RestController
@RequestMapping("api/rectangle")
public class RectangleController {

    @Autowired
    RectangleService rectangleService;

    @GetMapping
    public List<RectangleDto> rectangles() {
        return rectangleService.findRectangles();
    }

    @GetMapping("/{id}")
    public RectangleDto rectangle(@PathVariable int id) {
        return rectangleService.findRectangle(id);
    }

    @PostMapping
    public RectangleDto rectangle(@RequestBody @Valid SaveRectangleDto rectangle) {
        return rectangleService.saveRectangle(rectangle);
    }

    @PutMapping
    public RectangleDto updateRectangle(@RequestBody @Valid RectangleDto rectangle) {
        return rectangleService.updateRectangle(rectangle);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRectangle(@PathVariable int id) {
        rectangleService.deleteRectangle(id);
    }

    @PostMapping("/intersection")
    public List<PointDto> rectanglesIntersection(
            @RequestBody
            @NotEmpty(message = "Two rectangles are needed for determining intersection")
            @MaxSizeConstraint
            List<@Valid SaveRectangleDto> rectangleDtos) {
        return rectangleService.findIntersection(rectangleDtos);
    }

    @PostMapping("/containment")
    public Boolean rectanglesContainment(
            @RequestBody
            @NotEmpty(message = "Two rectangles are needed for determining containment")
            @MaxSizeConstraint
                    List<@Valid SaveRectangleDto> rectangleDtos) {
        return rectangleService.determineContainment(rectangleDtos);
    }

    @PostMapping("/adjacency")
    public Boolean rectanglesAdjacency(
            @RequestBody
            @NotEmpty(message = "Two rectangles are needed for determining adjacency")
            @MaxSizeConstraint
                    List<@Valid SaveRectangleDto> rectangleDtos) {
        return rectangleService.determineAdjacency(rectangleDtos);
    }

    @GetMapping("/intersection")
    public List<PointDto> rectanglesIntersectionById(
            @NotEmpty(message = "Two rectangle id's are needed for determining intersection")
            @Size(min = 2, max = 2)
            @RequestParam List<String> id) {
        return rectangleService.findIntersectionById(id);
    }

    @GetMapping("/containment")
    public Boolean rectanglesContainmentById(
            @NotEmpty(message = "Two rectangle id's are needed for determining containment")
            @Size(min = 2, max = 2)
            @RequestParam List<String> id) {
        return rectangleService.determineContainmentById(id);
    }

    @GetMapping("/adjacency")
    public Boolean rectanglesAdjacencyById(
            @NotEmpty(message = "Two rectangle id's are needed for determining adjacency")
            @Size(min = 2, max = 2)
            @RequestParam List<String> id) {
        return rectangleService.determineAdjacencyById(id);
    }
}
