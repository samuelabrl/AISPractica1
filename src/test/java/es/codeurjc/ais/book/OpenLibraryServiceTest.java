package es.codeurjc.ais.book;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OpenLibraryServiceTest {

    private static OpenLibraryService service;

    @BeforeAll
    static void setUp() {
        service = new OpenLibraryService();
    }

    @ParameterizedTest
    @ValueSource(strings = {"drama", "magic", "fantasy"})
    @DisplayName("When we search 'drama','magic' and 'fantasy' we get 15 books for each category")
    public void findByCategoriesTest(String strings) {
        List<OpenLibraryService.BookData> lista = service.searchBooks(strings, 15);

        assertEquals(15, lista.size());
    }

    @Test
    @DisplayName("When we search 'The Name of the Wind' we get that book and any exception are thrown")
    public void findByIDTest() {
        try {
            OpenLibraryService.BookData book = service.getBook("OL8479867W");
            assertEquals(book.key, "/works/OL8479867W");
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }

    }

    @Test
    public void findByIDTestMejorado() {
        assertDoesNotThrow(() -> {
            OpenLibraryService.BookData book = service.getBook("OL8479867W");
            assertEquals(book.key, "/works/OL8479867W");
        });
    }

}
