package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.dto.SessionDto;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.SessionService;
import ru.job4j.cinema.service.TicketService;
import java.util.Optional;

@ThreadSafe
@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    private final SessionService sessionService;

    public TicketController(TicketService ticketService, SessionService sessionService) {
        this.ticketService = ticketService;
        this.sessionService = sessionService;
    }

    @PostMapping("/bye")
    public String byeTicket(Model model, @ModelAttribute Ticket ticket) {
        Optional<Ticket> savedTicket = ticketService.save(ticket);
        if (savedTicket.isEmpty()) {
            String message = """
                    Не удалось приобрести билет на заданное место. Вероятно оно уже занято.
                    Перейдите на страницу бронирования билетов и попробуйте снова.
                    """;
            model.addAttribute("message", message);
            return "errors/404";
        }
        Optional<SessionDto> optionalSessionDto = sessionService.getSessionById(ticket.getSessionId());
        if (optionalSessionDto.isEmpty()) {
            String message = "Сеанс не найден";
            model.addAttribute("message", message);
            return "errors/404";
        }
        model.addAttribute("sess", optionalSessionDto.get());
        model.addAttribute("ticket", ticket);
        return "tickets/bye";
    }
}
