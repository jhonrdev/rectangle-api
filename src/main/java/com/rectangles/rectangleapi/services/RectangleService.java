package com.rectangles.rectangleapi.services;

import com.rectangles.rectangleapi.dto.PointDto;
import com.rectangles.rectangleapi.dto.RectangleDto;
import com.rectangles.rectangleapi.dto.SaveRectangleDto;
import com.rectangles.rectangleapi.entity.Rectangle;
import com.rectangles.rectangleapi.api.exception.ResourceNotFoundException;
import com.rectangles.rectangleapi.api.mappers.RectangleMapper;
import com.rectangles.rectangleapi.repository.RectangleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

@Service
public class RectangleService {

    @Autowired
    RectangleRepository rectangleRepository;

    @Autowired
    RectangleMapper rectangleMapper;

    public List<RectangleDto> findRectangles() {
        return rectangleMapper.toRectangleDtoList(rectangleRepository.findAll());
    }

    public RectangleDto findRectangle(int id) {
        return rectangleMapper.toRectangleDto(rectangleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find any Rectangle with id: " + id)));
    }

    public RectangleDto saveRectangle(SaveRectangleDto rectangleDto) {
        Rectangle rectangle = rectangleMapper.saveRectangleDtoToRectangle(rectangleDto);
        validateRectangle(rectangle);
        return rectangleMapper.toRectangleDto(rectangleRepository.save(rectangle));
    }

    public void deleteRectangle(int id) {
        rectangleMapper.toRectangleDto(rectangleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find any Rectangle with id: " + id)));
        rectangleRepository.deleteById(id);
    }

    public RectangleDto updateRectangle(RectangleDto rectangleDto) {
        Rectangle rectangle = rectangleMapper.toRectangle(rectangleDto);
        validateRectangle(rectangle);
        Rectangle existingRectangle = rectangleRepository.findById(rectangle.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Could not find any Rectangle with id: " + rectangle.getId()));
        existingRectangle.setBottomLeftX(rectangle.getBottomLeftX());
        existingRectangle.setBottomLeftY(rectangle.getBottomLeftY());
        existingRectangle.setTopRightX(rectangle.getTopRightX());
        existingRectangle.setTopRightY(rectangle.getTopRightY());
        return rectangleMapper.toRectangleDto(rectangleRepository.save(existingRectangle));
    }

    private void validateRectangle(Rectangle rectangle) {
        if (!(rectangle.getBottomLeftX() < rectangle.getTopRightX() &&
                rectangle.getBottomLeftY() < rectangle.getTopRightY())) {
            throw new BadRequestException("Provided top (x, y) coordinates must be greater than bottom's");
        }
    }

    public List<PointDto> findIntersection(List<SaveRectangleDto> rectangleDtos) {
        List<Rectangle> rectangleList = validateAndTransformRectangles(rectangleDtos);

        List<PointDto> intersectionPoints = new ArrayList<>();
        final Rectangle rectangleA = rectangleList.get(0);
        final Rectangle rectangleB = rectangleList.get(1);

        if (separatedRectangles(rectangleA, rectangleB)) return intersectionPoints;

        intersectionPoints.addAll(calculateIntersectionLeft(rectangleA, rectangleB));
        intersectionPoints.addAll(calculateIntersectionRight(rectangleA, rectangleB));
        intersectionPoints.addAll(calculateIntersectionTop(rectangleA, rectangleB));
        intersectionPoints.addAll(calculateIntersectionBottom(rectangleA, rectangleB));

        return intersectionPoints;
    }

    private List<PointDto> calculateIntersectionLeft(Rectangle rectangleA, Rectangle rectangleB) {
        List<PointDto> intersectionPoints = new ArrayList<>();
        if (rectangleA.getBottomLeftX() > rectangleB.getBottomLeftX() && rectangleA.getBottomLeftX() < rectangleB.getTopRightX()) {
            if (rectangleB.getTopRightY() < rectangleA.getTopRightY() && rectangleB.getTopRightY() > rectangleA.getBottomLeftY()) {
                final PointDto intersectionPoint = new PointDto(rectangleA.getBottomLeftX(), rectangleB.getTopRightY());
                intersectionPoints.add(intersectionPoint);
            }
            if (rectangleB.getBottomLeftY() < rectangleA.getTopRightY() && rectangleB.getBottomLeftY() > rectangleA.getBottomLeftY()) {
                final PointDto intersectionPoint = new PointDto(rectangleA.getBottomLeftX(), rectangleB.getBottomLeftY());
                intersectionPoints.add(intersectionPoint);
            }
        }
        return intersectionPoints;
    }

    private List<PointDto> calculateIntersectionRight(Rectangle rectangleA, Rectangle rectangleB) {
        List<PointDto> intersectionPoints = new ArrayList<>();
        if (rectangleA.getTopRightX() > rectangleB.getBottomLeftX() && rectangleA.getTopRightX() < rectangleB.getTopRightX()) {
            if (rectangleB.getTopRightY() < rectangleA.getTopRightY() && rectangleB.getTopRightY() > rectangleA.getBottomLeftY()) {
                final PointDto intersectionPoint = new PointDto(rectangleA.getTopRightX(), rectangleB.getTopRightY());
                intersectionPoints.add(intersectionPoint);
            }
            if (rectangleB.getBottomLeftY() < rectangleA.getTopRightY() && rectangleB.getBottomLeftY() > rectangleA.getBottomLeftY()) {
                final PointDto intersectionPoint = new PointDto(rectangleA.getTopRightX(), rectangleB.getBottomLeftY());
                intersectionPoints.add(intersectionPoint);
            }
        }
        return intersectionPoints;
    }

    private List<PointDto> calculateIntersectionTop(Rectangle rectangleA, Rectangle rectangleB) {
        List<PointDto> intersectionPoints = new ArrayList<>();
        if (rectangleA.getTopRightY() > rectangleB.getBottomLeftY() && rectangleA.getTopRightY() < rectangleB.getTopRightY()) {
            if (rectangleB.getBottomLeftX() < rectangleA.getTopRightX() && rectangleB.getBottomLeftX() > rectangleA.getBottomLeftX()) {
                final PointDto intersectionPoint = new PointDto(rectangleB.getBottomLeftX(), rectangleA.getTopRightY());
                intersectionPoints.add(intersectionPoint);
            }
            if (rectangleB.getTopRightX() < rectangleA.getTopRightX() && rectangleB.getTopRightX() > rectangleA.getBottomLeftX()) {
                final PointDto intersectionPoint = new PointDto(rectangleB.getTopRightX(), rectangleA.getTopRightY());
                intersectionPoints.add(intersectionPoint);
            }
        }
        return intersectionPoints;
    }

    private List<PointDto> calculateIntersectionBottom(Rectangle rectangleA, Rectangle rectangleB) {
        List<PointDto> intersectionPoints = new ArrayList<>();
        if (rectangleA.getBottomLeftY() > rectangleB.getBottomLeftY() && rectangleA.getBottomLeftY() < rectangleB.getTopRightY()) {
            if (rectangleB.getBottomLeftX() < rectangleA.getTopRightX() && rectangleB.getBottomLeftX() > rectangleA.getBottomLeftX()) {
                final PointDto intersectionPoint = new PointDto(rectangleB.getBottomLeftX(), rectangleA.getBottomLeftY());
                intersectionPoints.add(intersectionPoint);
            }
            if (rectangleB.getTopRightX() < rectangleA.getTopRightX() && rectangleB.getTopRightX() > rectangleA.getBottomLeftX()) {
                final PointDto intersectionPoint = new PointDto(rectangleB.getTopRightX(), rectangleA.getBottomLeftY());
                intersectionPoints.add(intersectionPoint);
            }
        }
        return intersectionPoints;
    }

    private boolean separatedRectangles(Rectangle a, Rectangle b) {
        return b.getBottomLeftX() > a.getTopRightX() ||
                b.getBottomLeftY() > a.getTopRightY() ||
                a.getBottomLeftX() > b.getTopRightX() ||
                a.getBottomLeftY() > b.getTopRightY();
    }

    private List<Rectangle> validateAndTransformRectangles(List<SaveRectangleDto> rectangleDtos) {
        List<Rectangle> rectangleList =
                rectangleDtos.stream()
                        .map(saveRectangleDto -> rectangleMapper
                                .saveRectangleDtoToRectangle(saveRectangleDto)).collect(Collectors.toList());
        rectangleList.forEach(this::validateRectangle);
        return rectangleList;
    }

    public Boolean determineContainment(List<SaveRectangleDto> rectangleDtos) {
        List<Rectangle> rectangleList = validateAndTransformRectangles(rectangleDtos);
        final Rectangle rectangleA = rectangleList.get(0);
        final Rectangle rectangleB = rectangleList.get(1);

        if (separatedRectangles(rectangleA, rectangleB)) return false;

        BiPredicate<Rectangle, Rectangle> contains = (outerRectangle , innerRectangle) ->
                ((innerRectangle.getBottomLeftX() > outerRectangle.getBottomLeftX()) &&
                        (innerRectangle.getTopRightX() < outerRectangle.getTopRightX()) &&
                        (innerRectangle.getTopRightY() < outerRectangle.getTopRightY()) &&
                        (innerRectangle.getBottomLeftY() > outerRectangle.getBottomLeftY()));

        return contains.test(rectangleA, rectangleB);
    }

    public Boolean determineAdjacency(List<SaveRectangleDto> rectangleDtos) {
        List<Rectangle> rectangleList = validateAndTransformRectangles(rectangleDtos);
        final Rectangle rectangleA = rectangleList.get(0);
        final Rectangle rectangleB = rectangleList.get(1);

        if (separatedRectangles(rectangleA, rectangleB)) return false;

        if (rectangleA.getBottomLeftX().equals(rectangleB.getTopRightX()) ||
                rectangleA.getTopRightX().equals(rectangleB.getBottomLeftX())) {
            return (rectangleA.getTopRightY().equals(rectangleB.getTopRightY()) &&
                    rectangleA.getBottomLeftY().equals(rectangleB.getBottomLeftY()));
        }
        if (rectangleA.getTopRightY().equals(rectangleB.getBottomLeftY()) ||
                rectangleA.getBottomLeftY().equals(rectangleB.getTopRightY())) {
            return (rectangleA.getBottomLeftX().equals(rectangleB.getBottomLeftX()) &&
                    rectangleA.getTopRightX().equals(rectangleB.getTopRightX()));
        }
        return false;
    }

    public List<PointDto> findIntersectionById(List<String> rectangleIds) {
        List<RectangleDto> rectangleList =
                rectangleIds.stream()
                        .map(rectangleId -> findRectangle(Integer.parseInt(rectangleId)))
                        .collect(Collectors.toList());
        return findIntersection(rectangleMapper.toSaveRectangleDtoList(rectangleList));
    }

    public Boolean determineContainmentById(List<String> rectangleIds) {
        List<RectangleDto> rectangleList =
                rectangleIds.stream()
                        .map(rectangleId -> findRectangle(Integer.parseInt(rectangleId)))
                        .collect(Collectors.toList());
        return determineContainment(rectangleMapper.toSaveRectangleDtoList(rectangleList));
    }

    public Boolean determineAdjacencyById(List<String> rectangleIds) {
        List<RectangleDto> rectangleList =
                rectangleIds.stream()
                        .map(rectangleId -> findRectangle(Integer.parseInt(rectangleId)))
                        .collect(Collectors.toList());
        return determineAdjacency(rectangleMapper.toSaveRectangleDtoList(rectangleList));
    }
}
