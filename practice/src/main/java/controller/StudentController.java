package controller;

import model.entity.Student;
import model.service.StudentService;
import model.service.StudentServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "StudentController", value = "/StudentController")
public class StudentController extends HttpServlet {
    private StudentService studentService = new StudentServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            showListStudent(request, response);
        }
        switch (action) {
            case "add":
                request.getRequestDispatcher("views/add.jsp").forward(request, response);
                break;
            case "delete":
                int idDel = Integer.parseInt(request.getParameter("id"));
                try {
                    studentService.delete(idDel);
                    showListStudent(request, response);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "edit":
                int idEdit = Integer.parseInt(request.getParameter("id"));
                try {
                    Student stu = studentService.findById(idEdit);
                    request.setAttribute("stu", stu);
                    request.getRequestDispatcher("views/edit.jsp").forward(request, response);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            showListStudent(request, response);
        }
        switch (action) {
            case "add":
                try {
                    handleAddNewStudent(request, response);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "edit":
                Student student = new Student();
                student.setId(Integer.parseInt(request.getParameter("id")));
                student.setAddress(request.getParameter("address"));
                student.setBirthday(Date.valueOf(request.getParameter("birthday")));
                student.setName(request.getParameter("name"));
                student.setEmail(request.getParameter("email"));
                try {
                    studentService.save(student);
                    showListStudent(request, response);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }

    private void handleAddNewStudent(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        Student stu = new Student();
        stu.setName(request.getParameter("name"));
        stu.setEmail(request.getParameter("email"));
        stu.setBirthday(Date.valueOf(request.getParameter("birthday")));
        stu.setAddress(request.getParameter("address"));
        if (studentService.save(stu)) {
            System.out.println("??????");
            showListStudent(request, response);
        }
        response.sendRedirect("views/add.jsp?err");
    }

    public void showListStudent(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Student> students = this.studentService.findAll();
        req.setAttribute("list_student", students);
        req.getRequestDispatcher("views/list.jsp").forward(req, resp);
    }
}