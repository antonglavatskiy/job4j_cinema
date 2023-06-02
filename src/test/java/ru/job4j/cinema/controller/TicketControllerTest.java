package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import ru.job4j.cinema.dto.SessionDto;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.SessionService;
import ru.job4j.cinema.service.TicketService;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TicketControllerTest {

    private TicketService ticketService;

    private SessionService sessionService;

    private TicketController ticketController;

    @BeforeEach
    public void initServices() {
        ticketService = mock(TicketService.class);
        sessionService = mock(SessionService.class);
        ticketController = new TicketController(ticketService, sessionService);
    }

    @Test
    public void whenRequestTicketPageThenGetPageWithTicket() {
        Ticket ticket = mock(Ticket.class);
        SessionDto sessionDto = mock(SessionDto.class);
        when(ticketService.save(ticket)).thenReturn(Optional.of(ticket));
        when(sessionService.getSessionById(any(Integer.class))).thenReturn(Optional.of(sessionDto));

        Model model = new ConcurrentModel();
        model.getAttribute("ticket");
        model.getAttribute("sess");
        String view = ticketController.byeTicket(model, ticket);

        assertThat(view).isEqualTo("tickets/bye");
    }

    @Test
    public void whenRequestTicketPageThenGetErrorPage() {
        Ticket ticket = mock(Ticket.class);
        String message = """
                    Не удалось приобрести билет на заданное место. Вероятно оно уже занято.
                    Перейдите на страницу бронирования билетов и попробуйте снова.
                    """;
        Exception expectedException = new RuntimeException(message);
        when(ticketService.save(ticket)).thenReturn(Optional.empty());

        Model model = new ConcurrentModel();
        String view = ticketController.byeTicket(model, ticket);
        Object actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/409");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }

    @Test
    public void whenRequestWithoutSessionToTicketPageThenGetErrorPage() {
        Ticket ticket = mock(Ticket.class);
        SessionDto sessionDto = mock(SessionDto.class);
        String message = "Сеанс не найден";
        Exception expectedException = new RuntimeException(message);
        when(ticketService.save(ticket)).thenReturn(Optional.of(ticket));
        when(sessionService.getSessionById(sessionDto.getId())).thenReturn(Optional.empty());

        Model model = new ConcurrentModel();
        String view = ticketController.byeTicket(model, ticket);
        Object actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }
}