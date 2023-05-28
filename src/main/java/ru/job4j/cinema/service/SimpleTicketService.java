package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.SessionDto;
import ru.job4j.cinema.dto.TicketDto;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.TicketRepository;

import java.util.Optional;

@Service
public class SimpleTicketService implements TicketService {

    private final TicketRepository ticketRepository;

    private final SessionService sessionService;

    private final UserService userService;

    public SimpleTicketService(TicketRepository sql2oTicketRepository,
                               SessionService sessionService, UserService userService) {
        this.ticketRepository = sql2oTicketRepository;
        this.sessionService = sessionService;
        this.userService = userService;
    }

    @Override
    public Optional<Ticket> save(TicketDto ticketDto) {
        Optional<SessionDto> optionalSessionDto = sessionService.getSessionById(ticketDto.getSessionDto().getId());
        Optional<User> optionalUser = userService
                .findByEmailAndPassword(ticketDto.getUser().getEmail(), ticketDto.getUser().getEmail());
        if (optionalSessionDto.isEmpty() || optionalUser.isEmpty()) {
            return Optional.empty();
        }
        SessionDto sessionDto = optionalSessionDto.get();
        User user = optionalUser.get();
        Ticket ticket = ticketRepository.save(
                new Ticket(sessionDto.getId(), ticketDto.getRow(), ticketDto.getPlace(), user.getId())).get();
        return Optional.of(ticket);
    }

    @Override
    public Optional<Ticket> findBySessionAndPlace(int sessionId, int row, int place) {
        return ticketRepository.findBySessionAndPlace(sessionId, row, place);
    }

    @Override
    public Optional<TicketDto> getTicketById(int id) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(id);
        if (optionalTicket.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(saveTicketDto(optionalTicket.get()));
    }

    private TicketDto saveTicketDto(Ticket ticket) {
        Optional<SessionDto> optionalSessionDto = sessionService.getSessionById(ticket.getSessionId());
        Optional<User> optionalUser = userService.findById(ticket.getUserId());
        return new TicketDto(ticket.getId(), optionalSessionDto.get(),
                ticket.getRow(), ticket.getPlace(), optionalUser.get());
    }
}
