package com.rectangles.rectangleapi.api.mappers;

import com.rectangles.rectangleapi.dto.RectangleDto;
import com.rectangles.rectangleapi.dto.SaveRectangleDto;
import com.rectangles.rectangleapi.entity.Rectangle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RectangleMapper {
    RectangleDto toRectangleDto(Rectangle rectangle);
    List<RectangleDto> toRectangleDtoList(List<Rectangle> rectangle);
    List<Rectangle> toRectangleList(List<SaveRectangleDto> rectangle);
    Rectangle toRectangle(RectangleDto rectangleDto);
    Rectangle saveRectangleDtoToRectangle(SaveRectangleDto saveRectangleDto);
    // @Mapping(target = "id", ignore = true)
    List<SaveRectangleDto> toSaveRectangleDtoList(List<RectangleDto> rectangleDto);
}
