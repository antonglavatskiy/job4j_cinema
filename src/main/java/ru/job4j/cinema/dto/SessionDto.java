package ru.job4j.cinema.dto;

import java.time.LocalDateTime;

public class SessionDto {
    private int id;
    private FilmDto filmDto;
    private HallDto hallDto;
    private LocalDateTime start;
    private LocalDateTime end;
    private int price;

    public SessionDto(int id, FilmDto filmDto, HallDto hallDto, LocalDateTime start, LocalDateTime end, int price) {
        this.id = id;
        this.filmDto = filmDto;
        this.hallDto = hallDto;
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

    public HallDto getHallDto() {
        return hallDto;
    }

    public void setHallDto(HallDto hallDto) {
        this.hallDto = hallDto;
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
