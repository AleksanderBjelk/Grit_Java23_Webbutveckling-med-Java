package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(urlPatterns = "/postcourses")
public class PCoursesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("postcourses.html");

    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String courseName = req.getParameter("name");
        String courseYHP = req.getParameter("yhp");
        String courseDescription = req.getParameter("description");

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Grit", "root", "")) {

                String sql = "INSERT INTO courses (name, YHP, description) VALUES (?, ?, ?)";

                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1, courseName);

                    ps.setString(2, courseYHP);

                    ps.setString(3, courseDescription);

                    int affectedRows = ps.executeUpdate();

                    if (affectedRows > 0) {
                        resp.sendRedirect("/courses");
                    } else {
                        throw new ServletException("Inga rader påverkades, lägg till kurs misslyckades.");
                    }
                }
            }
        } catch (Exception e) {
            throw new ServletException("Databasfel vid lägg till kurs", e);
        }
    }
}

