package ru.job4j.grabber.stores;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.service.Config;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcStore implements Store {
    private final Connection connection;
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcStore.class);

    public JdbcStore(Config config) throws SQLException {
        this.connection = DriverManager.getConnection(
                config.get("db.url"),
                config.get("db.username"),
                config.get("db.password")
        );
    }

    @Override
    public void save(Post post) {
        String sql = "INSERT INTO post(name, text, link, created) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, post.getTitle());
            statement.setString(2, post.getDescription());
            statement.setString(3, post.getLink());
            statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            statement.execute();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    post.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("When saving to DB", e);
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> result = new ArrayList<>();
        String sql = "SELECT * FROM post;";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                result.add(new Post(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("link"),
                        resultSet.getString("text"),
                        resultSet.getTimestamp("created").getTime())
                );
            }
        } catch (SQLException e) {
            LOGGER.error("When getting all", e);
        }
        return result;
    }

    @Override
    public Optional<Post> findById(Long id) {
        Post post = null;
        String sql = "SELECT * FROM post WHERE id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    post = new Post(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("link"),
                            resultSet.getString("text"),
                            resultSet.getTimestamp("created").getTime()
                    );
                }
            }
        } catch (SQLException e) {
            LOGGER.error("When finding by ID", e);
        }
        return Optional.ofNullable(post);
    }
}
