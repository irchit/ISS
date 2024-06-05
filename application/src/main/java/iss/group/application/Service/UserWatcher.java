package iss.group.application.Service;

import iss.group.application.Domain.User;

import java.util.HashMap;
import java.util.Map;

public class UserWatcher {
    private static final Map<String, User> loggedUsers = new HashMap<>();
    public static Boolean loggedIn(User User){
        return loggedUsers.containsKey(User.getUsername());
    }
    public static void login(User User){
        loggedUsers.put(User.getUsername(),User);
    }
    public static void logout(User User){
        loggedUsers.remove(User.getUsername());
    }
}
