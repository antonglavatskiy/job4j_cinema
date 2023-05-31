package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.HallDto;
import ru.job4j.cinema.model.Hall;

import java.util.Optional;

public interface HallService {
    Hall save(HallDto hallDto);
    Optional<HallDto> getHallById(int id);
    boolean deleteById(int id);
}
