package com.conferenceManagement.dao;

import java.io.Serializable;

public interface CrudRepository<T, ID extends Serializable> {
    void save(T t);
    void read(ID id);
    void update(T t);
    void delete(T t);

}
