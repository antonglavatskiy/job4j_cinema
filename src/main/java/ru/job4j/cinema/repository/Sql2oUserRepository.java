package ru.job4j.cinema.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.User;

import java.util.Optional;

public class Sql2oUserRepository implements UserRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(Sql2oUserRepository.class.getName());

    private final Sql2o sql2o;

    public Sql2oUserRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<User> save(User user) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    insert into users (full_name, email, password)
                    values (:name, :email, :password)
                    """;
            Query query = connection.createQuery(sql)
                    .addParameter("name", user.getName())
                    .addParameter("email", user.getEmail())
                    .addParameter("password", user.getPassword());
            int generatedKey = query.executeUpdate().getKey(Integer.class);
            user.setId(generatedKey);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            LOGGER.error("Пользователь с такой почтой уже существует", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    select * from users
                    where email = :email
                    and password = :password
                    """;
            Query query = connection.createQuery(sql)
                    .addParameter("email", email)
                    .addParameter("password", password);
            User user = query.setColumnMappings(User.COLUMN_MAPPING)
                    .executeAndFetchFirst(User.class);
            return Optional.ofNullable(user);
        }
    }
}
