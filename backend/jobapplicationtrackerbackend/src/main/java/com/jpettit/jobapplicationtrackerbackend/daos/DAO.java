package com.jpettit.jobapplicationtrackerbackend.daos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Collection;

public interface DAO<T> {
//    Optional<T> getById(Long id);
//    ArrayList<T> getAll();
    int insertOne(T t);
    T update(T t);
    T delete(T t);
//    int insertMany(Collection<T> list);
}
