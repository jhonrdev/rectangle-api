package com.rectangles.rectangleapi.repository;

import com.rectangles.rectangleapi.entity.Rectangle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RectangleRepository extends JpaRepository<Rectangle, Integer> {
}
