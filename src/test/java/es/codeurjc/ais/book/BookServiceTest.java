package es.codeurjc.ais.book;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.codeurjc.ais.notification.NotificationService;
import org.mockito.Mock;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;

public class BookServiceTest {


    @Mock
    OpenLibraryService openLibraryService = mock(OpenLibraryService.class);
    NotificationService notificationService = mock(NotificationService.class);


    @Test
    @DisplayName("When we search for 'magic' we get 2 books and a notification is triggered. ")
    public void findAllByCategoryTest() {
        List<OpenLibraryService.BookData> listBooks = new ArrayList<>();
        OpenLibraryService.BookData book1 = new OpenLibraryService.BookData(
                "A Midsummer Night's Dream",
                "/works/OL259010W",
                null,
                null,
                new String[]{"magic"}
        );
        OpenLibraryService.BookData book2 = new OpenLibraryService.BookData(
                "The Marvelous Land of Oz",
                "/works/OL18396W",
                null,
                null,
                new String[]{"magic"}
        );
        listBooks.add(book2);
        listBooks.add(book1);

        when(openLibraryService.searchBooks("magic", 10)).
                thenReturn(listBooks);

        BookService service = new BookService(openLibraryService, notificationService);
        List<Book> lista = service.findAll("magic");

        assertEquals(2, lista.size());
        verify(notificationService).info(anyString());
        listBooks.clear();
    }


    @Test
    @DisplayName("When searching for a book by ID, nothing is returned and a notification is triggered.")
    public void findBookByIdTest() {
        BookService service = new BookService(openLibraryService, notificationService);
        when(openLibraryService.getBook("OL18396W")).
                thenThrow(new HttpClientErrorException(HttpStatusCode.valueOf(404)));

        service.findById("OL18396W");
        verify(notificationService).error(anyString());
    }

}