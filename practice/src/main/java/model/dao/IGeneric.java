package model.dao;

import java.sql.SQLException;
import java.util.List;

public interface IGeneric<T,ID>{
    List<T> findAll();
    boolean save(T t) throws SQLException;
    T findById(ID id) throws SQLException;
    void delete(ID id) throws SQLException;
}
