package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Ticket;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oTicketRepositoryTest {

    private static Sql2oTicketRepository sql2oTicketRepository;

    private static Sql2o sql2o;

    @BeforeAll
    public static void initRepositories() throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = Sql2oTicketRepositoryTest.class
                .getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        String url = properties.getProperty("datasource.url");
        String username = properties.getProperty("datasource.username");
        String password = properties.getProperty("datasource.password");

        DatasourceConfiguration configuration = new DatasourceConfiguration();
        DataSource datasource = configuration.connectionPool(url, username, password);
        sql2o = configuration.databaseClient(datasource);

        sql2oTicketRepository = new Sql2oTicketRepository(sql2o);
    }

    @AfterEach
    public void clearTickets() {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    delete from tickets
                    """;
            connection.createQuery(sql).executeUpdate();
        }
    }

    @Test
    public void whenSaveTicketThenFindBySessionAndPlace() {
        int sessionId = 1;
        int row = 1;
        int place = 1;
        Ticket ticket = sql2oTicketRepository.save(new Ticket(sessionId, row, place, 1)).get();
        Ticket savedTicket = sql2oTicketRepository.findBySessionAndPlace(sessionId, row, place).get();
        assertThat(ticket).usingRecursiveComparison().isEqualTo(savedTicket);
    }

    @Test
    public void whenSaveTwoTicketsWithEqualsSessionsAndPlaceThenEmpty() {
        int sessionId = 1;
        int row = 1;
        int place = 1;
        Ticket ticket1 = new Ticket(sessionId, row, place, 1);
        Ticket ticket2 = new Ticket(sessionId, row, place, 2);
        sql2oTicketRepository.save(ticket1);
        assertThat(sql2oTicketRepository.save(ticket2)).isEmpty();
        Ticket savedTicket = sql2oTicketRepository.findBySessionAndPlace(sessionId, row, place).get();
        assertThat(ticket1).usingRecursiveComparison().isEqualTo(savedTicket);
    }
}