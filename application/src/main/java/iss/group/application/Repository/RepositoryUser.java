package iss.group.application.Repository;

import iss.group.application.Domain.Type;
import iss.group.application.Domain.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RepositoryUser implements IRepositoryUsers {

    private final String url;
    private final String user;
    private final String password;

    public RepositoryUser(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.password = pass;
    }

    @Override
    public User findByCredentials(String username, String password) {

        try (Connection connection = DriverManager.getConnection(url, user, this.password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users " +
                     "WHERE username = ? and password = ?");
        ) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("name"), resultSet.getString("email"), Type.valueOf(resultSet.getString("type")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public User findById(Integer integer) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users " +
                     "WHERE id = ?");
        ) {
            statement.setInt(1, Math.toIntExact(integer));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(integer, resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("name"), resultSet.getString("email"), Type.valueOf(resultSet.getString("type")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Collection<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users ");
        ) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                 users.add(new User(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("name"), resultSet.getString("email"), Type.valueOf(resultSet.getString("type"))));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public void save(User entity) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("insert into users(username, password, name, email, type) values(?, ?, ?, ?, ?)");
        ) {
            statement.setString(1, entity.getUsername());
            statement.setString(2, entity.getPassword());
            statement.setString(3, entity.getName());
            statement.setString(4, entity.getEmail());
            statement.setString(5, entity.getType().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE id = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(User entity) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("update users set username=?, password=?, name=?, email=?, type=? where id=?");
        ) {
            statement.setString(1, entity.getUsername());
            statement.setString(2, entity.getPassword());
            statement.setString(3, entity.getName());
            statement.setString(4, entity.getEmail());
            statement.setString(5, entity.getType().toString());
            statement.setInt(6, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
