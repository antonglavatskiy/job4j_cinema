package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.HallDto;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.repository.HallRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class SimpleHallService implements HallService {

    private final HallRepository hallRepository;

    public SimpleHallService(HallRepository sql2oHallRepository) {
        this.hallRepository = sql2oHallRepository;
    }

    @Override
    public Hall save(HallDto hallDto) {
        return hallRepository.save(new Hall(hallDto.getName(), hallDto.getRows().size(),
                hallDto.getPlaces().size(), hallDto.getDescription()));
    }

    @Override
    public Optional<HallDto> getHallById(int id) {
        Optional<Hall> optionalHall = hallRepository.findById(id);
        if (optionalHall.isEmpty()) {
            return Optional.empty();
        }
        Collection<Integer> rows = new ArrayList<>();
        Collection<Integer> places = new ArrayList<>();
        Hall hall = optionalHall.get();
        for (int i = 1; i <= hall.getRowCount(); i++) {
            rows.add(i);
        }
        for (int i = 1; i <= hall.getPlaceCount(); i++) {
            places.add(i);
        }
        HallDto hallDto = new HallDto(hall.getId(), hall.getName(), rows, places, hall.getDescription());
        return Optional.of(hallDto);
    }

    @Override
    public boolean deleteById(int id) {
        Optional<Hall> optionalHall = hallRepository.findById(id);
        if (optionalHall.isEmpty()) {
            return false;
        }
        return hallRepository.deleteById(id);
    }
}
