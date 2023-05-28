package ru.job4j.cinema.dto;

import ru.job4j.cinema.model.Hall;

import java.time.LocalDateTime;

public class SessionDto {
    private int id;
    private FilmDto filmDto;
    private Hall hall;
    private LocalDateTime start;
    private LocalDateTime end;
    private int price;

    public SessionDto(int id, FilmDto filmDto, Hall hall, LocalDateTime start, LocalDateTime end, int price) {
        this.id = id;
        this.filmDto = filmDto;
        this.hall = hall;
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

    public FilmDto getFilmDto() {
        return filmDto;
    }

    public void setFilmDto(FilmDto filmDto) {
        this.filmDto = filmDto;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
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
}
