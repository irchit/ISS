package iss.group.application.Repository;

import iss.group.application.Domain.Loc;
import java.sql.*;
import java.util.*;

public class RepositoryLoc implements IRepositoryLoc {

    private final String url;
    private final String user;
    private final String password;

    public RepositoryLoc(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.password = pass;
    }


    @Override
    public Loc findById(Integer id) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Loc " +
                     "WHERE id = ?");
        ) {
            statement.setInt(1, Math.toIntExact(id));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Loc(id, resultSet.getInt("nrordine"), resultSet.getInt("rand"), resultSet.getInt("coloana"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Collection<Loc> findAll() {
        List<Loc> locuri = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Loc");
        ) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                locuri.add(new Loc(resultSet.getInt("id"), resultSet.getInt("nrordine"), resultSet.getInt("rand"), resultSet.getInt("coloana")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return locuri;
    }

    @Override
    public void save(Loc entity) {
        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement("insert into loc(nrordine, rand, coloana) values(?, ?, ?)");){

            statement.setInt(1, entity.getNrOrdine());
            statement.setInt(2, entity.getRand());
            statement.setInt(3, entity.getColoana());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer integer) {
        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement("delete from loc where id = ?");){

            statement.setInt(1, integer);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(Loc entity) {
        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement("update loc set nrordine=?, rand=?, coloana=? where id=?");){

            statement.setInt(1, entity.getNrOrdine());
            statement.setInt(2, entity.getRand());
            statement.setInt(3, entity.getColoana());
            statement.setInt(4, entity.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
