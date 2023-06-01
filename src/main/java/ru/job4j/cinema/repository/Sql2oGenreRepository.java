package ru.job4j.cinema.repository;

import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Genre;
import java.util.Optional;

@ThreadSafe
@Repository
public class Sql2oGenreRepository implements GenreRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(Sql2oGenreRepository.class.getName());

    private final Sql2o sql2o;

    public Sql2oGenreRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Genre> save(Genre genre) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    insert into genres (name)
                    values (:name)
                    """;
            Query query = connection.createQuery(sql)
                    .addParameter("name", genre.getName());
            int generatedKey = query.executeUpdate().getKey(Integer.class);
            genre.setId(generatedKey);
            return Optional.ofNullable(genre);
        } catch (Exception e) {
            LOGGER.error("Жанр уже существует", e);
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(int id) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    delete from genres
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
    public Optional<Genre> findById(int id) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    select * from genres
                    where id = :id
                    """;
            Query query = connection.createQuery(sql);
            Genre genre = query.addParameter("id", id)
                    .executeAndFetchFirst(Genre.class);
            return Optional.ofNullable(genre);
        }
    }

    @Override
    public Optional<Genre> findByName(String name) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    select * from genres
                    where name = :name
                    """;
            Query query = connection.createQuery(sql);
            Genre genre = query.addParameter("name", name)
                    .executeAndFetchFirst(Genre.class);
            return Optional.ofNullable(genre);
        }
    }
}
