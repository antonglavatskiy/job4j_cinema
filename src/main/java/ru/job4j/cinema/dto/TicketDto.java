package ru.job4j.cinema.dto;

import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.User;

public class TicketDto {
    private int id;
    private SessionDto sessionDto;
    private int row;
    private int place;
    private User user;

    public TicketDto(int id, SessionDto sessionDto, int row, int place, User user) {
        this.id = id;
        this.sessionDto = sessionDto;
        this.row = row;
        this.place = place;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SessionDto getSessionDto() {
        return sessionDto;
    }

    public void setSessionDto(SessionDto sessionDto) {
        this.sessionDto = sessionDto;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
