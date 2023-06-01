package ru.job4j.cinema.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Hall;
import java.util.Optional;

@ThreadSafe
@Repository
public class Sql2oHallRepository implements HallRepository {

    private final Sql2o sql2o;

    public Sql2oHallRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Hall save(Hall hall) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    insert into halls
                    (name, row_count, place_count, description)
                    values (:name, :rowCount, :placeCount, :description)
                    """;
            Query query = connection.createQuery(sql, true)
                    .addParameter("name", hall.getName())
                    .addParameter("rowCount", hall.getRowCount())
                    .addParameter("placeCount", hall.getPlaceCount())
                    .addParameter("description", hall.getDescription());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            hall.setId(generatedId);
            return hall;
        }
    }


    @Override
    public boolean deleteById(int id) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    delete from halls
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
    public Optional<Hall> findById(int id) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    select * from halls
                    where id = :id
                    """;
            Query query = connection.createQuery(sql)
                    .addParameter("id", id);
            Hall hall = query.setColumnMappings(Hall.COLUMN_MAPPING)
                    .executeAndFetchFirst(Hall.class);
            return Optional.ofNullable(hall);
        }
    }
}
