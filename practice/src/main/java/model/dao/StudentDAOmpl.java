package model.dao;

import model.entity.Student;
import utils.ConnectionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAOmpl implements StudentDAO {

    @Override
    public List<Student> findAll() {
        Connection connection = null;
        List<Student> students = new ArrayList<>();
        connection = ConnectionDB.openConnection();
        try {
            CallableStatement cs = connection.prepareCall("" +
                    "call proc_show_student");
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                Student stu = new Student();
                stu.setId(rs.getInt("id"));
                stu.setName(rs.getString("name"));
                stu.setAddress(rs.getString("address"));
                stu.setEmail(rs.getString("email"));
                stu.setBirthday(rs.getDate("birthday"));
                students.add(stu);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionDB.closeConnection(connection);
        }
        return students;
    }

    @Override
    public boolean save(Student student) throws SQLException {
        Connection con = ConnectionDB.openConnection();
        CallableStatement cs = null;
        int check = 0;
        if (findById(student.getId()) == null) {
            try {
                cs = con.prepareCall("call proc_add_new_student(?,?,?,?)");
                cs.setString(1, student.getName());
                cs.setString(2, student.getEmail());
                cs.setDate(3, student.getBirthday());
                cs.setString(4, student.getAddress());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            cs = con.prepareCall("call proc_update_student(?,?,?,?,?)");
            cs.setInt(1, student.getId());
            cs.setString(2, student.getName());
            cs.setString(3, student.getEmail());
            cs.setDate(4, student.getBirthday());
            cs.setString(5, student.getAddress());
        }
        check = cs.executeUpdate();
        if (check > 0) {
            return true;
        }
        ConnectionDB.closeConnection(con);
        return false;
    }

    @Override
    public Student findById(Integer id) throws SQLException {
        Connection con = null;
        Student stu = new Student();
        con = ConnectionDB.openConnection();
        CallableStatement cs = con.prepareCall("call proc_show_student_by_id(?)");
        cs.setInt(1, id);
        ResultSet rs = cs.executeQuery();
        while (rs.next()) {
            stu.setId(rs.getInt("id"));
            stu.setName(rs.getString("name"));
            stu.setEmail(rs.getString("email"));
            stu.setBirthday(rs.getDate("birthday"));
            stu.setAddress(rs.getString("address"));
        }
        return stu;
    }

    @Override
    public void delete(Integer integer) throws SQLException {
        Connection con = ConnectionDB.openConnection();
        CallableStatement cs = con.prepareCall("call proc_delete_student(?)");
        cs.setInt(1, integer);
        cs.executeUpdate();
    }
}
