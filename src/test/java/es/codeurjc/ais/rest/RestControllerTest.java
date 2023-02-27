package es.codeurjc.ais.rest;

import com.fasterxml.jackson.databind.util.JSONPObject;
import es.codeurjc.ais.book.*;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import static org.junit.Assert.*;

import org.json.JSONException;
import org.json.JSONObject;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientException;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class RestControllerTest {

    @Test
    @DisplayName("Sends a GET petition to the RestController with the request param topic")
    public void testGetTopicDrama() {

        Collection list =

        given().
                contentType("application/json").
        when().
                get("/api/books/?topic=drama").
        then().
                statusCode(200).
                extract().response().as(Collection.class);


        Assertions.assertEquals(10, list.size());
    }

    @Test
    @DisplayName("Checks the recovery of the first ?topic=fantasy book and creates a review")
    public void testCreacionReview() throws JSONException {
        Response response =
                given().
                        contentType("application/json").
                when().
                        get("/api/books/?topic=fantasy").
                then().
                        extract().response().andReturn();

        //JsonPath jsonResponse = response.jsonPath();
        String bookId = response.jsonPath().get("[0].id");

        JSONObject body = new JSONObject();
        // Aunque en la petición POST indiques el id = 1 si ese id ya está asignado el Review service asignará otro id
        body.put("id", 1);
        body.put("nickname", "Samuel");
        body.put("content", "Me encanto el libro, muy recomendable");

        given().
                request().
                body(body.toString()).
                contentType("application/json").
                pathParam("bookId", bookId).
        when().
                post("/api/books/{bookId}/review").
        then().
                statusCode(201);
    }

  /*  @Test
    @DisplayName("Tests the correct deletion of a review")
    public void testBorradoReview() throws JSONException {
        // The way of kings id
        String bookId = "OL15358691W";

        Response book =
                given().
                        contentType("application/json").
                        pathParam("bookId", bookId).
                when().
                        get("/api/books/{bookId}").
                then().
                        extract().response().andReturn();

        List<String> reviews = book.jsonPath().get("reviews");
        int lastReviewId = reviews.size();

        JSONObject body = new JSONObject();
        body.put("id", lastReviewId + 1);
        body.put("nickname", "Samuel");
        body.put("content", "Me encanto el libro, muy recomendable");

        given().
                request().
                body(body.toString()).
                contentType("application/json").
                pathParam("bookId", bookId).
                when().
                post("/api/books/{bookId}/review");
        given().
                pathParam("bookId", bookId).
                pathParam("reviewId", lastReviewId + 1).when().
                get("/api/books/{bookId}/review/{reviewId}").then().statusCode(200);
    }*/
}