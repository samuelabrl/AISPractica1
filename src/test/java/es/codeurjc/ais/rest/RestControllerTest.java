package es.codeurjc.ais.rest;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

import es.codeurjc.ais.book.Book;
import es.codeurjc.ais.book.BookDetail;
import es.codeurjc.ais.review.Review;
import io.restassured.RestAssured;
import org.json.JSONException;
import org.json.JSONObject;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RestControllerTest {

    @BeforeEach
    public void setUp() {
        RestAssured.port = 8080;
    }

    @Test
    @DisplayName("Sends a GET petition to the RestController with the request param topic")
    public void getTopicDramaYListaLongitu10Test() {

        Collection<Book> list =

        given().
                contentType("application/json").
        when().
                get("/api/books/?topic=drama").
        then().
                statusCode(200).
                extract().jsonPath().getList(".", Book.class);


        assertEquals(10, list.size());
    }

    @Test
    @DisplayName("Checks the recovery of the first ?topic=fantasy book and creates a review")
    public void creacionReviewYCombrpobacionTest() throws JSONException {
        Response libroAntesReview =
                given().
                        contentType("application/json").
                when().
                        get("/api/books/?topic=drama").
                then().
                        extract().response().andReturn();
        // get("/api/books/?topic=drama");
        String bookId = libroAntesReview.jsonPath().get("[0].id");

        JSONObject body = new JSONObject();
        // No incluimos el id de review en la petición json pues nos la dá el servidor en la respuesta del put
        body.put("nickname", "Samuel");
        body.put("content", "Me encanto el libro, muy recomendable");

        Response responsePut =
                given().
                        request().
                        body(body.toString()).
                        contentType("application/json").
                        pathParam("bookId", bookId).
                when().
                        post("/api/books/{bookId}/review").
                then().
                        statusCode(201).
                        extract().response();

        // Comprobación de que de verdad la review ha sido creada y no solo la petición ha sido correcta

        Integer idReviewEsperada = responsePut.jsonPath().get("id");
        BookDetail libroDespuesReview =
                given().
                        contentType("application/json").
                        pathParam("bookId", bookId).
                when().
                        get("/api/books/{bookId}").
                then().extract().response().as(BookDetail.class);
        /*
           No podemos simplemente buscar .get("reviews.id[-1]") puesto que estamos asumiendo
           que la aplicaión solo la estamos usando nosostros.
           Se comprueba que la lista de reviews contenga una con el número de id recibido en la respuesta de la petición
           en vez de simplemente hacer:
           int idUltimaReview = libroDespuesReview.jsonPath().get("reviews.id[-1]") pues no nos garantizaría en una aplicación en funcinamiento
           que la resolución del test es correcta en todos los casos.
         */
        List<Review> listaReviews = libroDespuesReview.getReviews();
        //Funcional. Any match comprueba si algún elemento de stream satisface idReview == o.getId -> Si algún elemento de reviews coincide
        assertTrue(listaReviews.stream().anyMatch(o -> idReviewEsperada == o.getId()));
    }

    @Test
    @DisplayName("Tests the correct deletion of a review")
    public void creacionReviewYBorradoTest() throws JSONException {
        // The way of kings id
        String bookId = "OL15358691W";

        JSONObject body = new JSONObject();
        body.put("nickname", "Samuel");
        body.put("content", "Me encanto el libro, muy recomendable");

        Response respuesta =
                given().
                        request().
                        body(body.toString()).
                        contentType("application/json").
                        pathParam("bookId", bookId).
                when().
                        post("/api/books/{bookId}/review").
                then().
                        statusCode(201).
                        extract().response();

        int reviewId = respuesta.jsonPath().get("id");

        given().
                pathParam("bookId", bookId).
                pathParam("reviewId", reviewId).
        when().
                delete("/api/books/{bookId}/review/{reviewId}").
        then().
                statusCode(204);

        // Comprobación de que la review se ha borrado. Mismo caso anterior test
        BookDetail libroDespuesReview =
                given().
                        contentType("application/json").
                        pathParam("bookId", bookId).
                when().
                        get("/api/books/{bookId}").
                then().
                        extract().response().as(BookDetail.class);
        List<Review> listaReviews = libroDespuesReview.getReviews();

        assertFalse(listaReviews.stream().anyMatch(o -> reviewId == o.getId()));
    }
}