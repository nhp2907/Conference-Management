package com.conferenceManagement.dao;

import com.conferenceManagement.model.User;

import java.io.Serializable;
import java.util.List;

public interface IUserDAO extends CrudRepository<User, Long>{
    List<User> getAll();
}
