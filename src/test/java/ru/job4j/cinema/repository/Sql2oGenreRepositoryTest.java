package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Genre;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oGenreRepositoryTest {

    private static Sql2oGenreRepository sql2oGenreRepository;

    private static Sql2o sql2o;

    @BeforeAll
    public static void initRepositories() throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = Sql2oFileRepositoryTest.class
                .getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        String url = properties.getProperty("datasource.url");
        String username = properties.getProperty("datasource.username");
        String password = properties.getProperty("datasource.password");

        DatasourceConfiguration configuration = new DatasourceConfiguration();
        DataSource datasource = configuration.connectionPool(url, username, password);
        sql2o = configuration.databaseClient(datasource);

        sql2oGenreRepository = new Sql2oGenreRepository(sql2o);
    }

    @AfterEach
    public void clearGenres() {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    delete from genres
                    where id > 5
                    """;
            connection.createQuery(sql).executeUpdate();
        }
    }

    @Test
    public void whenSaveGenreThenFindById() {
        Genre genre = sql2oGenreRepository.save(new Genre("name")).get();
        Genre savedGenre = sql2oGenreRepository.findById(genre.getId()).get();
        assertThat(genre).usingRecursiveComparison().isEqualTo(savedGenre);
    }

    @Test
    public void whenSaveTwoGenresWithEqualsNamesThenEmpty() {
        Genre genre1 = new Genre("name1");
        Genre genre2 = new Genre("name1");
        sql2oGenreRepository.save(genre1);
        assertThat(sql2oGenreRepository.save(genre2)).isEmpty();
        Genre savedGenre = sql2oGenreRepository.findById(genre1.getId()).get();
        assertThat(genre1).usingRecursiveComparison().isEqualTo(savedGenre);
    }

    @Test
    public void whenSaveGenreAndDeleteThenFindById() {
        Genre genre = sql2oGenreRepository.save(new Genre("name")).get();
        int id = genre.getId();
        boolean isDeleted = sql2oGenreRepository.deleteById(id);
        assertThat(isDeleted).isTrue();
        assertThat(sql2oGenreRepository.findById(id)).isEmpty();
    }
}