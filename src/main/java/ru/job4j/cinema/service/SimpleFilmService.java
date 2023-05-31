package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FileDto;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.model.File;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.repository.FilmRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class SimpleFilmService implements FilmService {

    private final FilmRepository filmRepository;

    private final FileService fileService;

    private final GenreService genreService;

    public SimpleFilmService(FilmRepository sql2oFilmRepository, FileService fileService, GenreService genreService) {
        this.filmRepository = sql2oFilmRepository;
        this.fileService = fileService;
        this.genreService = genreService;
    }

    @Override
    public Optional<Film> save(FilmDto filmDto, FileDto image) {
        Optional<File> optionalFile = fileService.save(image);
        Optional<Genre> optionalGenre = genreService.findByName(filmDto.getGenre());
        if (optionalFile.isEmpty() || optionalGenre.isEmpty()) {
            return Optional.empty();
        }
        Film film = filmRepository.save(
                new Film(filmDto.getName(), filmDto.getDescription(),
                        filmDto.getYear(), optionalGenre.get().getId(),
                        filmDto.getMinimalAge(), filmDto.getDurationInMinutes(),
                        optionalFile.get().getId()));
        return Optional.of(film);
    }

    @Override
    public boolean deleteById(int id) {
        Optional<Film> optionalFilm = filmRepository.findById(id);
        if (optionalFilm.isEmpty()) {
            return false;
        }
        boolean isDeleted = filmRepository.deleteById(id);
        fileService.deleteById(optionalFilm.get().getFileId());
        return isDeleted;
    }

    @Override
    public Optional<FilmDto> getFilmById(int id) {
        Optional<Film> optionalFilm = filmRepository.findById(id);
        if (optionalFilm.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(saveFilmDto(optionalFilm.get()));
    }

    @Override
    public Collection<FilmDto> findAll() {
        Collection<FilmDto> rsl = new ArrayList<>();
        Collection<Film> films = filmRepository.findAll();
        for (Film film : films) {
            rsl.add(saveFilmDto(film));
        }
        return rsl;
    }

    private FilmDto saveFilmDto(Film film) {
        Optional<Genre> optionalGenre = genreService.findById(film.getGenreId());
        return new FilmDto(film.getId(), film.getName(),
                film.getDescription(), film.getYear(),
                film.getMinimalAge(), film.getDuration(),
                optionalGenre.get().getName(), film.getFileId());
    }
}
