package ru.job4j.cinema.dto;

import java.util.Collection;

public class HallDto {
    private int id;
    private String name;
    private Collection<Integer> rows;
    private Collection<Integer> places;
    private String description;

    public HallDto(int id, String name, Collection<Integer> rows, Collection<Integer> places, String description) {
        this.id = id;
        this.name = name;
        this.rows = rows;
        this.places = places;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Integer> getRows() {
        return rows;
    }

    public void setRows(Collection<Integer> rows) {
        this.rows = rows;
    }

    public Collection<Integer> getPlaces() {
        return places;
    }

    public void setPlaces(Collection<Integer> places) {
        this.places = places;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
