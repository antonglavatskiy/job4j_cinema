package ru.job4j.cinema.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

public class Session {

    public static final Map<String, String> COLUMN_MAPPING = Map.of(
            "id", "id",
            "film_id", "filmId",
            "halls_id", "hallId",
            "start_time", "start",
            "end_time", "end",
            "price", "price"
    );

    private int id;
    private int filmId;
    private int hallId;
    private LocalDateTime start;
    private LocalDateTime end;
    private int price;

    public Session(int filmId, int hallId, LocalDateTime start, LocalDateTime end, int price) {
        this.filmId = filmId;
        this.hallId = hallId;
        this.start = start;
        this.end = end;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public int getHallId() {
        return hallId;
    }

    public void setHallId(int hallId) {
        this.hallId = hallId;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Session session = (Session) o;
        return id == session.id && filmId == session.filmId && hallId == session.hallId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, filmId, hallId);
    }
}
