package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Hall;

import java.util.Optional;

public interface HallRepository {
    Hall save(Hall hall);
    Optional<Hall> findById(int id);
    boolean deleteById(int id);
}
