package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import ru.job4j.cinema.dto.SessionDto;
import ru.job4j.cinema.service.SessionService;

import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SessionControllerTest {

    private SessionService sessionService;

    private SessionController sessionController;

    @BeforeEach
    public void initServices() {
        sessionService = mock(SessionService.class);
        sessionController = new SessionController(sessionService);
    }

    @Test
    public void whenRequestSessionListPageThenGetPageWithSessions() {
        SessionDto sessionDto1 = mock(SessionDto.class);
        SessionDto sessionDto2 = mock(SessionDto.class);
        List<SessionDto> expected = List.of(sessionDto1, sessionDto2);
        when(sessionService.findAll()).thenReturn(expected);

        Model model = new ConcurrentModel();
        String view = sessionController.getAll(model);
        Object actual = model.getAttribute("sessionList");

        assertThat(view).isEqualTo("sessions/list");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenRequestSessionPageThenGetPageWithSession() {
        SessionDto sessionDto = mock(SessionDto.class);
        when(sessionService.getSessionById(any(Integer.class))).thenReturn(Optional.of(sessionDto));

        Model model = new ConcurrentModel();
        String view = sessionController.getById(model, sessionDto.getId());
        Object expected = model.getAttribute("sess");

        assertThat(view).isEqualTo("sessions/one");
        assertThat(expected).isEqualTo(sessionDto);
    }

    @Test
    public void whenRequestSessionPageThenGetErrorPage() {
        SessionDto sessionDto = mock(SessionDto.class);
        Exception expectedException = new RuntimeException("Сессия с указанным идентификатором не найдена");
        when(sessionService.getSessionById(any(Integer.class))).thenReturn(Optional.empty());

        Model model = new ConcurrentModel();
        String view = sessionController.getById(model, sessionDto.getId());
        Object actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }
}