package model.service;

import model.dao.StudentDAO;
import model.dao.StudentDAOmpl;
import model.entity.Student;

import java.sql.SQLException;
import java.util.List;

public class StudentServiceImpl implements StudentService {
    private final StudentDAO studentDAO = new StudentDAOmpl();

    @Override
    public List<Student> findAll() {
        return studentDAO.findAll();
    }

    @Override
    public boolean save(Student student) throws SQLException {
        return studentDAO.save(student);
    }

    @Override
    public Student findById(Integer id) throws SQLException {
        return studentDAO.findById(id);
    }

    @Override
    public void delete(Integer id) throws SQLException {
        studentDAO.delete(id);
    }
}
