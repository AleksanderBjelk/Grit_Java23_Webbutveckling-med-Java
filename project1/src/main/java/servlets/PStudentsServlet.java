package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@WebServlet(urlPatterns = "/poststudents")
public class PStudentsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("poststudents.html");
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String firstName = req.getParameter("fname");

        String lastName = req.getParameter("lname");

        String town = req.getParameter("town");

        String hobby = req.getParameter("hobby");

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Grit", "root", "")) {

                String sql = "INSERT INTO students (Fname, Lname, town, hobby) VALUES (?, ?, ?, ?)";

                try (PreparedStatement ps = con.prepareStatement(sql)) {

                    ps.setString(1, firstName);

                    ps.setString(2, lastName);

                    ps.setString(3, town);

                    ps.setString(4, hobby);

                    int affectedRows = ps.executeUpdate();
                    if (affectedRows > 0) {
                        resp.sendRedirect("/students");
                    } else {
                        throw new ServletException("Inga rader påverkades, lägg till student misslyckades.");
                    }
                }
            }
        } catch (Exception e) {
            throw new ServletException("Databasfel vid lägg till student", e);
        }
    }
}
