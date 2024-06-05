package iss.group.application.Repository;

import iss.group.application.Domain.Bilet;

import java.util.*;
import java.sql.*;

public class RepositoryBilet implements IRepositoryBilet {

    private final String url;
    private final String user;
    private final String password;

    public RepositoryBilet(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.password = pass;
    }

    @Override
    public Bilet findById(Integer id) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Bilet " +
                     "WHERE id = ?");
        ) {
            statement.setInt(1, Math.toIntExact(id));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int idFound = resultSet.getInt("id");
                int idUser = resultSet.getInt("user_id");
                int idLoc = resultSet.getInt("loc_id");
                int idSpectacol = resultSet.getInt("spectacol_id");

                RepositoryUser repositoryUser = new RepositoryUser(url, user, password);
                RepositoryLoc repositoryLoc = new RepositoryLoc(url, user, password);
                RepositorySpectacol repositorySpectacol = new RepositorySpectacol(url, user, password);

                Bilet bilet = new Bilet(
                        idFound,
                        repositoryUser.findById(idUser),
                        repositoryLoc.findById(idLoc),
                        repositorySpectacol.findById(idSpectacol)
                    );
                return bilet;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Collection<Bilet> findAll() {
        List<Bilet> bilete = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Bilet");
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                int idFound = resultSet.getInt("id");
                int idUser = resultSet.getInt("user_id");
                int idLoc = resultSet.getInt("loc_id");
                int idSpectacol = resultSet.getInt("spectacol_id");

                RepositoryUser repositoryUser = new RepositoryUser(url, user, password);
                RepositoryLoc repositoryLoc = new RepositoryLoc(url, user, password);
                RepositorySpectacol repositorySpectacol = new RepositorySpectacol(url, user, password);

                Bilet bilet = new Bilet(
                        idFound,
                        repositoryUser.findById(idUser),
                        repositoryLoc.findById(idLoc),
                        repositorySpectacol.findById(idSpectacol)
                );
                bilete.add(bilet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bilete;
    }

    @Override
    public void save(Bilet entity) {
        try(
                Connection connection = DriverManager.getConnection(url, user, password);
                PreparedStatement statement = connection.prepareStatement("insert into Bilet(user_id, loc_id, spectacol_id) values(?, ?, ?)");
        ) {
            statement.setInt(1, Math.toIntExact(entity.getUser().getId()));
            statement.setInt(2, Math.toIntExact(entity.getLoc().getId()));
            statement.setInt(3, Math.toIntExact(entity.getSpectacol().getId()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id) {
        try(
                Connection connection = DriverManager.getConnection(url, user, password);
                PreparedStatement statement = connection.prepareStatement("delete from Bilet where id = ?");
                ){
            statement.setInt(1, Math.toIntExact(id));
            statement.executeUpdate();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Bilet entity) {
        try(
                Connection connection = DriverManager.getConnection(url, user, password);
                PreparedStatement statement = connection.prepareStatement(
                        "update Bilet set user_id = ?, loc_id = ?, spectacol_id = ? where id = ?"
                );
        ) {
            statement.setInt(1, Math.toIntExact(entity.getUser().getId()));
            statement.setInt(2, Math.toIntExact(entity.getLoc().getId()));
            statement.setInt(3, Math.toIntExact(entity.getSpectacol().getId()));
            statement.setInt(4, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
