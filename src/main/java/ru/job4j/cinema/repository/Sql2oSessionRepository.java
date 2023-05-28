package ru.job4j.cinema.repository;

import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Session;

import java.util.Collection;
import java.util.Optional;

public class Sql2oSessionRepository implements SessionRepository {

    private final Sql2o sql2o;

    public Sql2oSessionRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Session save(Session session) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    insert into film_sessions
                    (film_id, halls_id, start_time, end_time, price)
                    values (:filmId, :hallId, :start, :end, :price)
                    """;
            Query query = connection.createQuery(sql, true)
                    .addParameter("filmId", session.getFilmId())
                    .addParameter("hallId", session.getHallId())
                    .addParameter("start", session.getStart())
                    .addParameter("end", session.getEnd())
                    .addParameter("price", session.getPrice());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            session.setId(generatedId);
            return session;
        }
    }

    @Override
    public boolean deleteById(int id) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    delete from film_sessions
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
    public Optional<Session> findById(int id) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    select * from film_sessions
                    where id = :id
                    """;
            Query query = connection.createQuery(sql)
                    .addParameter("id", id);
            Session session = query.setColumnMappings(Session.COLUMN_MAPPING)
                    .executeAndFetchFirst(Session.class);
            return Optional.ofNullable(session);
        }
    }

    @Override
    public Collection<Session> findAll() {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    select * from film_sessions
                    """;
            Query query = connection.createQuery(sql);
            return query.setColumnMappings(Session.COLUMN_MAPPING)
                    .executeAndFetch(Session.class);
        }
    }
}
