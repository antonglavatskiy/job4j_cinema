package ru.job4j.cinema.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.File;

import java.util.Optional;

public class Sql2oFileRepository implements FileRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(Sql2oFileRepository.class.getName());

    private final Sql2o sql2o;

    public Sql2oFileRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<File> save(File file) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    insert into files (name, path)
                    values (:name, :path)
                    """;
            Query query = connection.createQuery(sql)
                    .addParameter("name", file.getName())
                    .addParameter("path", file.getPath());
            int generatedKey = query.executeUpdate().getKey(Integer.class);
            file.setId(generatedKey);
            return Optional.ofNullable(file);
        } catch (Exception e) {
            LOGGER.error("Файл уже существует", e);
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(int id) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    delete from files
                    where id = :id
                    """;
            Query query = connection.createQuery(sql);
            int affectedRows = query.addParameter("id", id)
                    .executeUpdate()
                    .getResult();
            return affectedRows > 0;
        }
    }

    @Override
    public Optional<File> findById(int id) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    select * from files
                    where id = :id
                    """;
            Query query = connection.createQuery(sql);
            File file = query.addParameter("id", id)
                    .executeAndFetchFirst(File.class);
            return Optional.ofNullable(file);
        }
    }
}
