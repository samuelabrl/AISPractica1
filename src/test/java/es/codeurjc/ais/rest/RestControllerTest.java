package es.codeurjc.ais.rest;

import com.fasterxml.jackson.databind.util.JSONPObject;
import es.codeurjc.ais.book.*;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import static org.junit.Assert.*;

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
    public void testCreacionReview() {
        Response response =
                given().
                        contentType("application/json").
                when().
                        get("/api/books/?topic=fantasy").
                then().
                        extract().response().andReturn();

        //JsonPath jsonResponse = response.jsonPath();
        String bookId = response.jsonPath().get("[0].id");

        given().
               pathParam("id", bookId).
               param("id", 5).
               param("nickname", "Samuel").
               param("content", "Me encanto el libro, muy recomendable").
        when().
               post("/api/books/{id}/review").
        then().
               statusCode(201);
    }

}