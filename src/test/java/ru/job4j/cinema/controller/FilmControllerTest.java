package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.service.FilmService;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FilmControllerTest {

    private FilmService filmService;

    private FilmController filmController;

    @BeforeEach
    public void initServices() {
        filmService = mock(FilmService.class);
        filmController = new FilmController(filmService);
    }

    @Test
    public void whenRequestFilmListPageThenGetPageWithFilms() {
        FilmDto filmDto1 = mock(FilmDto.class);
        FilmDto filmDto2 = mock(FilmDto.class);
        List<FilmDto> expected = List.of(filmDto1, filmDto2);
        when(filmService.findAll()).thenReturn(expected);

        Model model = new ConcurrentModel();
        String view = filmController.getAll(model);
        Object actual = model.getAttribute("films");

        assertThat(view).isEqualTo("films/list");
        assertThat(actual).isEqualTo(expected);
    }
}