package iss.group.application.Repository;

import iss.group.application.Domain.User;

public interface IRepositoryUsers extends IRepository<Integer, User> {
    public User findByCredentials(String username, String password);
}
