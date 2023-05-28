package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Session;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oSessionRepositoryTest {

    private static Sql2oSessionRepository sql2oSessionRepository;

    private static Sql2o sql2o;

    @BeforeAll
    public static void initRepositories() throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = Sql2oSessionRepositoryTest.class
                .getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        String url = properties.getProperty("datasource.url");
        String username = properties.getProperty("datasource.username");
        String password = properties.getProperty("datasource.password");

        DatasourceConfiguration configuration = new DatasourceConfiguration();
        DataSource datasource = configuration.connectionPool(url, username, password);
        sql2o = configuration.databaseClient(datasource);

        sql2oSessionRepository = new Sql2oSessionRepository(sql2o);
    }

    @AfterEach
    public void clearSessions() {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    delete from film_sessions
                    where id > 15
                    """;
            connection.createQuery(sql).executeUpdate();
        }
    }

    @Test
    public void whenSaveSessionThenFindById() {
        LocalDateTime start = now().truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime end = now().plusHours(2).truncatedTo(ChronoUnit.MINUTES);
        Session session = sql2oSessionRepository.save(
                new Session(1, 1, start, end, 100));
        Session savedSession = sql2oSessionRepository.findById(session.getId()).get();
        assertThat(session).usingRecursiveComparison().isEqualTo(savedSession);
    }

    @Test
    public void whenSaveSessionAndDeleteThenFindById() {
        LocalDateTime start = now().truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime end = now().plusHours(2).truncatedTo(ChronoUnit.MINUTES);
        Session session = sql2oSessionRepository.save(
                new Session(1, 1, start, end, 100));
        int id = session.getId();
        boolean isDeleted = sql2oSessionRepository.deleteById(id);
        assertThat(isDeleted).isTrue();
        assertThat(sql2oSessionRepository.findById(id)).isEmpty();
    }

    @Test
    public void whenFindAllThenGetLast() {
        LocalDateTime start = now().truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime end = now().plusHours(2).truncatedTo(ChronoUnit.MINUTES);
        Session session = sql2oSessionRepository.save(
                new Session(1, 1, start, end, 100));
        List<Session> sessions = new ArrayList<>(sql2oSessionRepository.findAll());
        assertThat(sessions.get(sessions.size() - 1)).usingRecursiveComparison().isEqualTo(session);
    }

}