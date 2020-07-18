package com.conferenceManagement.service;

import com.conferenceManagement.model.User;
import com.conferenceManagement.dao.UserDAO;
import com.conferenceManagement.util.PasswordAuthentication;

import java.util.List;

public class UserService implements IUserService {

    UserDAO userDAO = new UserDAO();

    public UserService() {

    }

    @Override
    public User getUserByUserName(String userName) {
        return userDAO.getUserByUsername(userName);
    }

    @Override
    public boolean checkLogin(String username, String password) {
        var user = userDAO.getUserByUsername(username);

        return user.getUserName().equals(username) &&
                PasswordAuthentication.authenticate(password, user.getPassword());
    }

    @Override
    public List<User> getAll() {
        return userDAO.getAll();
    }

    @Override
    public void update(User user) {
        userDAO.update(user);
    }

    @Override
    public User getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    @Override
    public void save(User user) {
        userDAO.save(user);
    }
}
