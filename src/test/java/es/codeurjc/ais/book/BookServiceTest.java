package es.codeurjc.ais.book;

import static org.mockito.Mockito.*;
import java.util.List;
import java.util.ArrayList;

import es.codeurjc.ais.notification.NotificationService;
import static org.junit.jupiter.api.Assertions.*;

import static org.junit.Assert.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClientException;

import org.hamcrest.CoreMatchers;


public class BookServiceTest {

    @Test
    public void findAllSize() {
        BookService service = new BookService(new OpenLibraryService(), new NotificationService());
        List<Book> lista = service.findAll("magic");
        assertEquals(10, lista.size());
    }

   /* @Test
    public void findAllNotification() {
        NotificationService notificationService = mock(NotificationService.class);
        when(notificationService.info()).thenReturn();
        BookService service = new BookService(new OpenLibraryService(), notificationService);
        List<Book> lista = service.findAll("magic");

        verify(notificationService);
    }*/

    @Test
    public void findById() {
        BookService service = new BookService(new OpenLibraryService(), new NotificationService());
        service.findById("ID");
    }
}