package ru.job4j.cinema.repository;

import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Ticket;
import java.util.Optional;

@ThreadSafe
@Repository
public class Sql2oTicketRepository implements TicketRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(Sql2oTicketRepository.class.getName());

    private final Sql2o sql2o;

    public Sql2oTicketRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Ticket> save(Ticket ticket) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    insert into tickets (session_id, row_number, place_number, user_id)
                    values (:sessionId, :row, :place, :userId)
                    """;
            Query query = connection.createQuery(sql)
                    .addParameter("sessionId", ticket.getSessionId())
                    .addParameter("row", ticket.getRow())
                    .addParameter("place", ticket.getPlace())
                    .addParameter("userId", ticket.getUserId());
            int generatedKey = query.executeUpdate().getKey(Integer.class);
            ticket.setId(generatedKey);
            return Optional.of(ticket);
        } catch (Exception e) {
            LOGGER.error("Билет уже куплен", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Ticket> findBySessionAndPlace(int sessionId, int row, int place) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    select * from tickets
                    where session_id = :sessionId
                    and row_number = :row
                    and place_number = :place
                    """;
            Query query = connection.createQuery(sql)
                    .addParameter("sessionId", sessionId)
                    .addParameter("row", row)
                    .addParameter("place", place);
            Ticket ticket = query.setColumnMappings(Ticket.COLUMN_MAPPING)
                    .executeAndFetchFirst(Ticket.class);
            return Optional.ofNullable(ticket);
        }
    }

    @Override
    public Optional<Ticket> findById(int id) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    select * from tickets
                    where id = :id
                    """;
            Query query = connection.createQuery(sql)
                    .addParameter("id", id);
            Ticket ticket = query.setColumnMappings(Ticket.COLUMN_MAPPING)
                    .executeAndFetchFirst(Ticket.class);
            return Optional.ofNullable(ticket);
        }
    }
}
