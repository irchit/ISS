package iss.group.application.Repository;

import iss.group.application.Domain.Spectacol;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RepositorySpectacol implements IRepositorySpectacol {

    private final String url;
    private final String user;
    private final String password;

    public RepositorySpectacol(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.password = pass;
    }

    @Override
    public Spectacol findById(Integer id) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Spectacol WHERE id = ?")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Spectacol(
                        resultSet.getInt("id"),
                        resultSet.getString("titlu"),
                        resultSet.getString("descriere"),
                        resultSet.getDate("data"),
                        resultSet.getFloat("pret")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Collection<Spectacol> findAll() {
        List<Spectacol> spectacols = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Spectacol")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                spectacols.add(new Spectacol(
                        resultSet.getInt("id"),
                        resultSet.getString("titlu"),
                        resultSet.getString("descriere"),
                        resultSet.getDate("data"),
                        resultSet.getFloat("pret")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return spectacols;
    }

    @Override
    public void save(Spectacol entity) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO Spectacol (titlu, descriere, data, pret) VALUES (?, ?, ?, ?)")) {
            statement.setString(1, entity.getTitlu());
            statement.setString(2, entity.getDescriere());
            statement.setDate(3, new java.sql.Date(entity.getData().getTime()));
            statement.setFloat(4, entity.getPret());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM Spectacol WHERE id = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Spectacol entity) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("UPDATE Spectacol SET titlu=?, descriere=?, data=?, pret=? WHERE id=?")) {
            statement.setString(1, entity.getTitlu());
            statement.setString(2, entity.getDescriere());
            statement.setDate(3, new java.sql.Date(entity.getData().getTime()));
            statement.setFloat(4, entity.getPret());
            statement.setInt(5, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
