package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Film;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oFilmRepositoryTest {

    private static Sql2oFilmRepository sql2oFilmRepository;

    private static Sql2o sql2o;

    @BeforeAll
    public static void initRepositories() throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = Sql2oFilmRepositoryTest.class
                .getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        String url = properties.getProperty("datasource.url");
        String username = properties.getProperty("datasource.username");
        String password = properties.getProperty("datasource.password");

        DatasourceConfiguration configuration = new DatasourceConfiguration();
        DataSource datasource = configuration.connectionPool(url, username, password);
        sql2o = configuration.databaseClient(datasource);

        sql2oFilmRepository = new Sql2oFilmRepository(sql2o);
    }

    @AfterEach
    public void clearFilms() {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    delete from films
                    where id > 5
                    """;
            connection.createQuery(sql).executeUpdate();
        }
    }

    @Test
    public void whenSaveFilmThenFindById() {
        Film film = sql2oFilmRepository.save(
                new Film("name", "description",
                        2000, 1, 18, 100, 1));
        Film savedFilm = sql2oFilmRepository.findById(film.getId()).get();
        assertThat(film).usingRecursiveComparison().isEqualTo(savedFilm);
    }

    @Test
    public void whenSaveFilmAndDeleteThenFindById() {
        Film film = sql2oFilmRepository.save(
                new Film("name", "description",
                2000, 1, 18, 100, 1));
        int id = film.getId();
        boolean isDeleted = sql2oFilmRepository.deleteById(id);
        assertThat(isDeleted).isTrue();
        assertThat(sql2oFilmRepository.findById(id)).isEmpty();
    }

    @Test
    public void whenFindAllThenGetLast() {
        Film film = sql2oFilmRepository.save(
                new Film("name", "description",
                        2000, 1, 18, 100, 1));
        List<Film> films = new ArrayList<>(sql2oFilmRepository.findAll());
        assertThat(films.get(films.size() - 1)).usingRecursiveComparison().isEqualTo(film);
    }
}