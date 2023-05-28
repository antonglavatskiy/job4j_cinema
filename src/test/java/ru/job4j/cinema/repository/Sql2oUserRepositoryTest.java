package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oUserRepositoryTest {

    private static Sql2oUserRepository sql2oUserRepository;

    private static Sql2o sql2o;

    @BeforeAll
    public static void initRepositories() throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = Sql2oUserRepositoryTest.class
                .getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        String url = properties.getProperty("datasource.url");
        String username = properties.getProperty("datasource.username");
        String password = properties.getProperty("datasource.password");

        DatasourceConfiguration configuration = new DatasourceConfiguration();
        DataSource datasource = configuration.connectionPool(url, username, password);
        sql2o = configuration.databaseClient(datasource);

        sql2oUserRepository = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    public void clearUsers() {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    delete from users
                    """;
            connection.createQuery(sql).executeUpdate();
        }
    }

    @Test
    public void whenSaveUserThenFindByEmailAndPassword() {
        String email = "test@test.ru";
        String pass = "pass";
        User user = sql2oUserRepository.save(new User("name", email, pass)).get();
        User savedUser = sql2oUserRepository.findByEmailAndPassword(email, pass).get();
        assertThat(user).usingRecursiveComparison().isEqualTo(savedUser);
    }

    @Test
    public void whenSaveTwoUsersWithEqualsEmailsThenEmpty() {
        String email = "test@test.ru";
        User user1 = new User("name1", email, "pass1");
        User user2 = new User("name2", email, "pass2");
        sql2oUserRepository.save(user1);
        assertThat(sql2oUserRepository.save(user2)).isEmpty();
        User savedUser = sql2oUserRepository.findByEmailAndPassword(email, "pass1").get();
        assertThat(user1).usingRecursiveComparison().isEqualTo(savedUser);
    }

    @Test
    public void whenSaveUserThenFindById() {
        User user = sql2oUserRepository.save(
                new User("name", "test@test.ru", "pass")).get();
        User savedUser = sql2oUserRepository.findById(user.getId()).get();
        assertThat(user).usingRecursiveComparison().isEqualTo(savedUser);
    }
}