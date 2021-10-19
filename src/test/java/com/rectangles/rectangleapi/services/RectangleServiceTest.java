package com.rectangles.rectangleapi.services;

import com.rectangles.rectangleapi.api.exception.ResourceNotFoundException;
import com.rectangles.rectangleapi.api.mappers.RectangleMapper;
import com.rectangles.rectangleapi.dto.PointDto;
import com.rectangles.rectangleapi.dto.RectangleDto;
import com.rectangles.rectangleapi.dto.SaveRectangleDto;
import com.rectangles.rectangleapi.entity.Rectangle;
import com.rectangles.rectangleapi.repository.RectangleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RectangleServiceTest {
    @Mock
    private RectangleRepository rectangleRepository;
    @Mock
    private RectangleMapper rectangleMapper;

    @InjectMocks RectangleService rectangleService;

    private final Rectangle testRectangle = new Rectangle(1, 0, 0, 4, 4);
    private final RectangleDto testRectangleDto = new RectangleDto(1, 0, 0, 4, 4);
    private final SaveRectangleDto testSaveRectangleDto = new SaveRectangleDto(0, 0, 4, 4);

    @Test
    void findRectangles_HappyPath_ReturnsRectangleDtoList() {
        when(rectangleMapper.toRectangleDtoList(any())).thenReturn(Collections.singletonList(testRectangleDto));

        assertEquals(Collections.singletonList(testRectangleDto), rectangleService.findRectangles());
    }

    @Test
    void findRectangleById_HappyPath_ReturnsRectangleDto() {
        when(rectangleRepository.findById(any())).thenReturn(Optional.of(testRectangle));
        when(rectangleMapper.toRectangleDto(any())).thenReturn(testRectangleDto);

        assertEquals(testRectangleDto, rectangleService.findRectangle(1));
    }

    @Test()
    void findRectangleById_NotFound_ThrowsResourceNotFoundException() {
        assertThrows(ResourceNotFoundException.class, () -> {
            when(rectangleRepository.findById(any())).thenReturn(Optional.empty());

            rectangleService.findRectangle(1);
        });
    }

    @Test
    void saveRectangle_HappyPath_ReturnsRectangleDto() {
        when(rectangleMapper.saveRectangleDtoToRectangle(any())).thenReturn(testRectangle);
        when(rectangleMapper.toRectangleDto(any())).thenReturn(testRectangleDto);

        assertEquals(testRectangleDto, rectangleService.saveRectangle(testSaveRectangleDto));
    }

    @Test
    void deleteRectangle_HappyPath_DeletesRectangle() {
        when(rectangleRepository.findById(any())).thenReturn(Optional.of(testRectangle));
        when(rectangleMapper.toRectangleDto(any())).thenReturn(testRectangleDto);

        rectangleService.deleteRectangle(1);

        verify(rectangleRepository, times(1)).deleteById(1);
    }

    @Test()
    void deleteRectangle_RectangleNotFound_ThrowsResourceNotFoundException() {
        assertThrows(ResourceNotFoundException.class, () -> {
            when(rectangleRepository.findById(any())).thenReturn(Optional.empty());
            rectangleService.deleteRectangle(1);
        });
    }

    @Test
    void updateRectangleBy_HappyPath_ReturnsRectangleDto() {
        when(rectangleRepository.findById(any())).thenReturn(Optional.of(testRectangle));
        when(rectangleMapper.toRectangle(any())).thenReturn(testRectangle);
        when(rectangleMapper.toRectangleDto(any())).thenReturn(testRectangleDto);

        assertEquals(testRectangleDto, rectangleService.updateRectangle(testRectangleDto));
    }

    @Test()
    void updateRectangle_RectangleNotFound_ThrowsResourceNotFoundException() {
        assertThrows(ResourceNotFoundException.class, () -> {
            when(rectangleRepository.findById(any())).thenReturn(Optional.empty());
            when(rectangleMapper.toRectangle(any())).thenReturn(testRectangle);
            rectangleService.updateRectangle(testRectangleDto);
        });
    }

    @Test
    void findIntersection_HappyPathRectanglesDoHaveIntersectionPoints_ReturnsIntersectionPointDtoList() {
        final SaveRectangleDto rectangleDto1 = new SaveRectangleDto(1, 1, 5, 3);
        final SaveRectangleDto rectangleDto2 = new SaveRectangleDto(2, 0, 4, 4);
        final Rectangle rectangle1 = new Rectangle(0, 1, 1, 5, 3);
        final Rectangle rectangle2 = new Rectangle(0, 2, 0, 4, 4);
        final List<SaveRectangleDto> input = Arrays.asList(rectangleDto1, rectangleDto2);

        final PointDto pointDto1 = new PointDto(2, 3);
        final PointDto pointDto2 = new PointDto(4, 3);
        final PointDto pointDto3 = new PointDto(2, 1);
        final PointDto pointDto4 = new PointDto(4, 1);
        final List<PointDto> result = Arrays.asList(pointDto1, pointDto2, pointDto3, pointDto4);

        when(rectangleMapper.saveRectangleDtoToRectangle(rectangleDto1)).thenReturn(rectangle1);
        when(rectangleMapper.saveRectangleDtoToRectangle(rectangleDto2)).thenReturn(rectangle2);

        assertEquals(result, rectangleService.findIntersection(input));
    }

    @Test
    void findIntersection_SeparatedRectangles_ReturnsEmptyPointDtoList() {
        final SaveRectangleDto rectangleDto1 = new SaveRectangleDto(0, 0, 2, 2);
        final SaveRectangleDto rectangleDto2 = new SaveRectangleDto(2, 2, 4, 4);
        final Rectangle rectangle1 = new Rectangle(0, 0, 0, 2, 2);
        final Rectangle rectangle2 = new Rectangle(0, 2, 2, 4, 4);
        final List<SaveRectangleDto> input = Arrays.asList(rectangleDto1, rectangleDto2);

        final List<PointDto> result = Collections.emptyList();

        when(rectangleMapper.saveRectangleDtoToRectangle(rectangleDto1)).thenReturn(rectangle1);
        when(rectangleMapper.saveRectangleDtoToRectangle(rectangleDto2)).thenReturn(rectangle2);

        assertEquals(result, rectangleService.findIntersection(input));
    }

    @Test
    void determineContainment_HappyPathRectangleAContainsRectangleB_ReturnsTrue() {
        final SaveRectangleDto rectangleDto1 = new SaveRectangleDto(0, 0, 4, 4);
        final SaveRectangleDto rectangleDto2 = new SaveRectangleDto(1, 1, 3, 3);
        final Rectangle rectangle1 = new Rectangle(0, 0, 0, 4, 4);
        final Rectangle rectangle2 = new Rectangle(0, 1, 1, 3, 3);
        final List<SaveRectangleDto> input = Arrays.asList(rectangleDto1, rectangleDto2);

        when(rectangleMapper.saveRectangleDtoToRectangle(rectangleDto1)).thenReturn(rectangle1);
        when(rectangleMapper.saveRectangleDtoToRectangle(rectangleDto2)).thenReturn(rectangle2);

        assertTrue(rectangleService.determineContainment(input));
    }

    @Test
    void determineContainment_PassedInRectangleAIsNotContainedWithinRectangleB_ReturnsFalse() {
        final SaveRectangleDto rectangleDto1 = new SaveRectangleDto(0, 0, 4, 4);
        final SaveRectangleDto rectangleDto2 = new SaveRectangleDto(1, 1, 3, 3);
        final Rectangle rectangle1 = new Rectangle(0, 0, 0, 4, 4);
        final Rectangle rectangle2 = new Rectangle(0, 1, 1, 3, 3);
        final List<SaveRectangleDto> input = Arrays.asList(rectangleDto2, rectangleDto1);

        when(rectangleMapper.saveRectangleDtoToRectangle(rectangleDto1)).thenReturn(rectangle1);
        when(rectangleMapper.saveRectangleDtoToRectangle(rectangleDto2)).thenReturn(rectangle2);

        assertFalse(rectangleService.determineContainment(input));
    }

    @Test
    void determineContainment_PassedInRectanglesAreSeparated_ReturnsFalse() {
        final SaveRectangleDto rectangleDto1 = new SaveRectangleDto(0, 0, 2, 2);
        final SaveRectangleDto rectangleDto2 = new SaveRectangleDto(2, 2, 4, 4);
        final Rectangle rectangle1 = new Rectangle(0, 0, 0, 2, 2);
        final Rectangle rectangle2 = new Rectangle(0, 2, 2, 4, 4);
        final List<SaveRectangleDto> input = Arrays.asList(rectangleDto2, rectangleDto1);

        when(rectangleMapper.saveRectangleDtoToRectangle(rectangleDto1)).thenReturn(rectangle1);
        when(rectangleMapper.saveRectangleDtoToRectangle(rectangleDto2)).thenReturn(rectangle2);

        assertFalse(rectangleService.determineContainment(input));
    }

    @Test
    void determineAdjacency_HappyPathRectangleAAndRectangleBAreProperlyAdjacent_ReturnsTrue() {
        final SaveRectangleDto rectangleDto1 = new SaveRectangleDto(0, 0, 2, 2);
        final SaveRectangleDto rectangleDto2 = new SaveRectangleDto(2, 0, 4, 2);
        final Rectangle rectangle1 = new Rectangle(0, 0, 0, 2, 2);
        final Rectangle rectangle2 = new Rectangle(0, 2, 0, 4, 2);
        final List<SaveRectangleDto> input = Arrays.asList(rectangleDto1, rectangleDto2);

        when(rectangleMapper.saveRectangleDtoToRectangle(rectangleDto1)).thenReturn(rectangle1);
        when(rectangleMapper.saveRectangleDtoToRectangle(rectangleDto2)).thenReturn(rectangle2);

        assertTrue(rectangleService.determineAdjacency(input));
    }

    @Test
    void determineAdjacency_RectangleAAndRectangleBAreNotProperlyAdjacent_ReturnsFalse() {
        final SaveRectangleDto rectangleDto1 = new SaveRectangleDto(0, 0, 2, 2);
        final SaveRectangleDto rectangleDto2 = new SaveRectangleDto(2, 0, 4, 3);
        final Rectangle rectangle1 = new Rectangle(0, 0, 0, 2, 2);
        final Rectangle rectangle2 = new Rectangle(0, 2, 0, 4, 3);
        final List<SaveRectangleDto> input = Arrays.asList(rectangleDto1, rectangleDto2);

        when(rectangleMapper.saveRectangleDtoToRectangle(rectangleDto1)).thenReturn(rectangle1);
        when(rectangleMapper.saveRectangleDtoToRectangle(rectangleDto2)).thenReturn(rectangle2);

        assertFalse(rectangleService.determineAdjacency(input));
    }

    @Test
    void determineAdjacency_RectanglesAreSeparated_ReturnsFalse() {
        final SaveRectangleDto rectangleDto1 = new SaveRectangleDto(0, 0, 2, 2);
        final SaveRectangleDto rectangleDto2 = new SaveRectangleDto(2, 2, 4, 4);
        final Rectangle rectangle1 = new Rectangle(0, 0, 0, 2, 2);
        final Rectangle rectangle2 = new Rectangle(0, 2, 2, 4, 4);
        final List<SaveRectangleDto> input = Arrays.asList(rectangleDto1, rectangleDto2);

        when(rectangleMapper.saveRectangleDtoToRectangle(rectangleDto1)).thenReturn(rectangle1);
        when(rectangleMapper.saveRectangleDtoToRectangle(rectangleDto2)).thenReturn(rectangle2);

        assertFalse(rectangleService.determineAdjacency(input));
    }
}
