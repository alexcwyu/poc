package net.alexyu.common.jdbc;

import java.io.Serializable;

public interface IDExtractor<T, ID extends Serializable> {
    ID extract(T entity);
}
