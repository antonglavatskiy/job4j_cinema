package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cinema.dto.SessionDto;
import ru.job4j.cinema.service.SessionService;
import java.util.Optional;

@ThreadSafe
@Controller
@RequestMapping("/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("sessionList", sessionService.findAll());
        return "sessions/list";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        Optional<SessionDto> optionalSessionDto = sessionService.getSessionById(id);
        if (optionalSessionDto.isEmpty()) {
            model.addAttribute("message", "Сессия с указанным идентификатором не найдена");
            return "errors/404";
        }
        model.addAttribute("sess", optionalSessionDto.get());
        return "sessions/one";
    }
}
