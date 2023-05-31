package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.dto.HallDto;
import ru.job4j.cinema.dto.SessionDto;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.repository.SessionRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class SimpleSessionService implements SessionService {

    private final SessionRepository sessionRepository;

    private final FilmService filmService;

    private final HallService hallService;

    public SimpleSessionService(SessionRepository sql2oSessionRepository, FilmService filmService, HallService hallService) {
        this.sessionRepository = sql2oSessionRepository;
        this.filmService = filmService;
        this.hallService = hallService;
    }

    @Override
    public Optional<Session> save(SessionDto sessionDto) {
        Optional<FilmDto> optionalFilmDto = filmService.getFilmById(sessionDto.getFilmDto().getId());
        Optional<HallDto> optionalHallDto = hallService.getHallById(sessionDto.getHallDto().getId());
        if (optionalFilmDto.isEmpty() || optionalHallDto.isEmpty()) {
            return Optional.empty();
        }
        FilmDto filmDto = optionalFilmDto.get();
        HallDto hallDto = optionalHallDto.get();
        Session session = sessionRepository.save(
                new Session(filmDto.getId(), hallDto.getId(),
                        sessionDto.getStart(), sessionDto.getEnd(), sessionDto.getPrice()));
        return Optional.of(session);
    }

    @Override
    public boolean deleteById(int id) {
        Optional<Session> optionalSession = sessionRepository.findById(id);
        if (optionalSession.isEmpty()) {
            return false;
        }
        return sessionRepository.deleteById(id);
    }

    @Override
    public Optional<SessionDto> getSessionById(int id) {
        Optional<Session> optionalSession = sessionRepository.findById(id);
        if (optionalSession.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(saveSessionDto(optionalSession.get()));
    }

    @Override
    public Collection<SessionDto> findAll() {
        Collection<SessionDto> rsl = new ArrayList<>();
        Collection<Session> sessions = sessionRepository.findAll();
        for (Session session : sessions) {
            rsl.add(saveSessionDto(session));
        }
        return rsl;
    }

    private SessionDto saveSessionDto(Session session) {
        Optional<FilmDto> optionalFilmDto = filmService.getFilmById(session.getFilmId());
        Optional<HallDto> optionalHallDto = hallService.getHallById(session.getHallId());
        return new SessionDto(session.getId(), optionalFilmDto.get(),
                optionalHallDto.get(), session.getStart(), session.getEnd(), session.getPrice());
    }
}
