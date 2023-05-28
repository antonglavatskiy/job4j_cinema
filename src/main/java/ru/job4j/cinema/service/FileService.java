package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.FileDto;
import ru.job4j.cinema.model.File;

import java.util.Optional;

public interface FileService {
    Optional<File> save(FileDto fileDto);
    boolean deleteById(int id);
    Optional<FileDto> getFileById(int id);
}
