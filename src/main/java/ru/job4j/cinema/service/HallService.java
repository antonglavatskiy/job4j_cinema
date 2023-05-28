package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Hall;

import java.util.Optional;

public interface HallService {
    Hall save(Hall hall);
    Optional<Hall> findById(int id);
    boolean deleteById(int id);
}
