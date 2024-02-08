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
        String cName = req.getParameter("name");

        String cYHP = req.getParameter("yhp");
        String cDescription = req.getParameter("description");

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Grit", "root", "")) { //för endast SELECT frågor namn: Aleksander lösenord: password

                String query = "SELECT COUNT(*) FROM courses WHERE name = ?";

                try (PreparedStatement statement = con.prepareStatement(query)) {
                    statement.setString(1, cName);

                    ResultSet rs = statement.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {

                        out.println("<script type='text/javascript'>");

                        out.println("alert('en kurs med detta namnet finns redan.');");
                        out.println("location='/postcourses';");

                        out.println("</script>");

                    } else {

                        String insertQuery = "INSERT INTO courses (name, YHP, description) VALUES (?, ?, ?)";

                        try (PreparedStatement ps = con.prepareStatement(insertQuery)) {

                            ps.setString(1, cName);
                            ps.setString(2, cYHP);
                            ps.setString(3, cDescription);

                            int affectedRows = ps.executeUpdate();

                            if (affectedRows > 0) {
                                resp.sendRedirect("/courses");
                            } else {
                                out.println("<script type='text/javascript'>");

                                out.println("alert('Misslyckades, försök igen.');");
                                out.println("location='/postcourses';");
                                out.println("</script>");

                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            out.println("<script type='text/javascript'>");

            out.println("alert('Database error: " + e.getMessage() + "');");
            out.println("location='/postcourses';");

            out.println("</script>");
        }
    }

}

