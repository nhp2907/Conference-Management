package com.conferenceManagement.service;

import com.conferenceManagement.model.User;

import java.util.List;

public interface IUserService {
    List<User> getAll();

    void update(User user);

    User getUserByUsername(String t1);

    void save(User user);

    User getUserByUserName(String username);

    boolean checkLogin(String username, String password);
}
