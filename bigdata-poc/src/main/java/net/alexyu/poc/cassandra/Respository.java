package net.alexyu.poc.cassandra;

import java.util.List;

public interface Respository<T, P> {

    void insert(T t);

    void insert(List<T> list);

    T select(P primaryKey);

    List<T> selectAll();


    void delete(P primaryKey);

    void deleteAll();
}
