package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Hall;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oHallRepositoryTest {

    private static Sql2oHallRepository sql2oHallRepository;

    private static Sql2o sql2o;

    @BeforeAll
    public static void initRepositories() throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = Sql2oHallRepositoryTest.class
                .getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        String url = properties.getProperty("datasource.url");
        String username = properties.getProperty("datasource.username");
        String password = properties.getProperty("datasource.password");

        DatasourceConfiguration configuration = new DatasourceConfiguration();
        DataSource datasource = configuration.connectionPool(url, username, password);
        sql2o = configuration.databaseClient(datasource);

        sql2oHallRepository = new Sql2oHallRepository(sql2o);
    }

    @AfterEach
    public void clearHalls() {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    delete from halls
                    where id > 5
                    """;
            connection.createQuery(sql).executeUpdate();
        }
    }

    @Test
    public void whenSaveHallThenFindById() {
        Hall hall = sql2oHallRepository.save(
                new Hall("name", 10, 10, "description"));
        Hall savedHall = sql2oHallRepository.findById(hall.getId()).get();
        assertThat(hall).usingRecursiveComparison().isEqualTo(savedHall);
    }

    @Test
    public void whenSaveHallAndDeleteThenFindById() {
        Hall hall = sql2oHallRepository.save(
                new Hall("name", 10, 10, "description"));
        int id = hall.getId();
        boolean isDeleted = sql2oHallRepository.deleteById(id);
        assertThat(isDeleted).isTrue();
        assertThat(sql2oHallRepository.findById(id)).isEmpty();
    }

}