package com.rectangles.rectangleapi.api;

import com.rectangles.rectangleapi.dto.SaveRectangleDto;
import io.restassured.RestAssured;
import io.restassured.RestAssured.*;
import io.restassured.http.ContentType;
import io.restassured.matcher.RestAssuredMatchers.*;
import org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RectangleApiRestApiTests {

    @LocalServerPort
    int port;

    @Before
    public void setup() {
        RestAssured.port = 5000;
    }

    @Test
    void whenPostRectangle_thenOK(){
        given().port(port).
                with().body(new SaveRectangleDto(1, 1, 5, 3))
                .contentType(ContentType.JSON)
                .when()
                .request("POST", "/api/rectangle")
                .then()
                .statusCode(200);
    }

    @Test
    void whenGetRectangles_thenOK(){
        given().port(port)
                .when()
                .request("GET", "/api/rectangle")
                .then()
                .statusCode(200);
    }

    @Test
    void whenPostIntersection_thenOK(){
        given().port(port).
                with().body(Arrays.asList
                        (new SaveRectangleDto(1, 1, 5, 3),
                                new SaveRectangleDto(2, 0, 4, 4)))
                .contentType(ContentType.JSON)
                .when()
                .request("POST", "/api/rectangle/intersection")
                .then()
                .statusCode(200);
    }

    @Test
    void whenPostIntersection_InvalidArgsAmount_thenBadRequest(){
        given().port(port).
                with().body(Arrays.asList
                        (new SaveRectangleDto(1, 1, 5, 3),
                                new SaveRectangleDto(2, 0, 4, 4),
                                new SaveRectangleDto(2, 0, 4, 4)))
                .contentType(ContentType.JSON)
                .when()
                .request("POST", "/api/rectangle/intersection")
                .then()
                .statusCode(400);
    }

    @Test
    void whenPostIntersection_NoArgs_thenBadRequest(){
        given().port(port).
                with().body(Collections.emptyList())
                .contentType(ContentType.JSON)
                .when()
                .request("POST", "/api/rectangle/intersection")
                .then()
                .statusCode(400);
    }

    @Test
    void whenPostContainment_thenOK(){
        boolean result = given().port(port).
                with().body(Arrays.asList(new SaveRectangleDto(0, 0, 4, 4),
                                new SaveRectangleDto(1, 1, 3, 3)))
                .contentType(ContentType.JSON)
                .when()
                .request("POST", "/api/rectangle/containment")
                .then()
                .statusCode(200)
                .and().extract()
                .response()
                .as(Boolean.class);
        assertTrue(result);
    }

    @Test
    void whenPostContainment_InvalidArgsAmount_thenBadRequest(){
        given().port(port).
                with().body(Arrays.asList
                        (new SaveRectangleDto(1, 1, 5, 3),
                                new SaveRectangleDto(2, 0, 4, 4),
                                new SaveRectangleDto(2, 0, 4, 4)))
                .contentType(ContentType.JSON)
                .when()
                .request("POST", "/api/rectangle/containment")
                .then()
                .statusCode(400);
    }

    @Test
    void whenPostContainment_NoArgs_thenBadRequest(){
        given().port(port).
                with().body(Collections.emptyList())
                .contentType(ContentType.JSON)
                .when()
                .request("POST", "/api/rectangle/containment")
                .then()
                .statusCode(400);
    }

    @Test
    void whenPostAdjacency_thenOK(){
        boolean result = given().port(port).
                with().body(Arrays.asList(new SaveRectangleDto(0, 0, 2, 2),
                                new SaveRectangleDto(2, 0, 4, 2)))
                .contentType(ContentType.JSON)
                .when()
                .request("POST", "/api/rectangle/adjacency")
                .then()
                .statusCode(200)
                .and().extract()
                .response()
                .as(Boolean.class);
        assertTrue(result);
    }

    @Test
    void whenPostAdjacency_InvalidArgsAmount_thenBadRequest(){
        given().port(port).
                with().body(Collections.singletonList(
                        (new SaveRectangleDto(1, 1, 5, 3))))
                .contentType(ContentType.JSON)
                .when()
                .request("POST", "/api/rectangle/adjacency")
                .then()
                .statusCode(400);
    }

    @Test
    void whenPostAdjacency_NoArgs_thenBadRequest(){
        given().port(port).
                with().body(Collections.emptyList())
                .contentType(ContentType.JSON)
                .when()
                .request("POST", "/api/rectangle/adjacency")
                .then()
                .statusCode(400);
    }
}
