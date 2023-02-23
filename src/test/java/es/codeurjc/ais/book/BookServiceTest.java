package es.codeurjc.ais.book;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import es.codeurjc.ais.notification.NotificationService;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.Assert.*;
import org.aspectj.apache.bcel.classfile.Module;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClientException;

import org.hamcrest.CoreMatchers;
import java.util.*;

public class BookServiceTest {


    @Test
    public void findAllSize() {
        OpenLibraryService openLibraryService = mock(OpenLibraryService.class);
        NotificationService notificationService = mock(NotificationService.class);

        String[] subjects = new String[]{"magic"};


        OpenLibraryService.BookData book1 = new OpenLibraryService.BookData(
                "A Midsummer Night's Dream",
                "/works/OL259010W",
                null,
                null,
                new String[]{"magic"}
                            );
        OpenLibraryService.BookData book2 = new OpenLibraryService.BookData(
                "A Midsummer 222Night's Dream",
                "/works/OL679010W",
                null,
                null,
                new String[]{"magic"}
        );
        List <OpenLibraryService.BookData> list_books = new ArrayList<>();
        list_books.add(book2);
        list_books.add(book1);
        when(openLibraryService.searchBooks("magic",10)).thenReturn(list_books);

        BookService service = new BookService(openLibraryService, notificationService);
        List<Book> lista = service.findAll("magic");

        assertEquals(2, lista.size());
        verify(notificationService).info(anyString());

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