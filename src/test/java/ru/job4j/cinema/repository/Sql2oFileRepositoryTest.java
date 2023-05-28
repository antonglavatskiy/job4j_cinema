package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.File;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oFileRepositoryTest {

    private static Sql2oFileRepository sql2oFileRepository;

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

        sql2oFileRepository = new Sql2oFileRepository(sql2o);
    }

    @AfterEach
    public void clearFiles() {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    delete from files
                    where id > 5
                    """;
            connection.createQuery(sql).executeUpdate();
        }
    }

    @Test
    public void whenSaveFileThenFindById() {
        File file = sql2oFileRepository.save(new File("name", "files/name.txt")).get();
        File savedFile = sql2oFileRepository.findById(file.getId()).get();
        assertThat(file).usingRecursiveComparison().isEqualTo(savedFile);
    }

    @Test
    public void whenSaveTwoFilesWithEqualsPathsThenEmpty() {
        File file1 = new File("name1", "files/name.txt");
        File file2 = new File("name2", "files/name.txt");
        sql2oFileRepository.save(file1);
        assertThat(sql2oFileRepository.save(file2)).isEmpty();
        File savedFile = sql2oFileRepository.findById(file1.getId()).get();
        assertThat(file1).usingRecursiveComparison().isEqualTo(savedFile);
    }

    @Test
    public void whenSaveFileAndDeleteThenFindById() {
        File file = sql2oFileRepository.save(new File("name", "files/name.txt")).get();
        int id = file.getId();
        boolean isDeleted = sql2oFileRepository.deleteById(id);
        assertThat(isDeleted).isTrue();
        assertThat(sql2oFileRepository.findById(id)).isEmpty();
    }
}