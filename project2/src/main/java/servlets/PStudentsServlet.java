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
import java.sql.ResultSet;

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

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Grit", "root", "")) { //för endast SELECT frågor namn: Aleksander lösenord: password


                String query = "SELECT COUNT(*) FROM students WHERE Fname = ? AND Lname = ? AND hobby = ?";

                try (PreparedStatement statement = con.prepareStatement(query)) {

                    statement.setString(1, firstName);
                    statement.setString(2, lastName);

                    statement.setString(3, hobby);

                    ResultSet rs = statement.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {

                        out.println("<script type='text/javascript'>");

                        out.println("alert('Var snäll att välj en annan hobby och pröva igen');");
                        out.println("window.history.back();");

                        out.println("</script>");

                    } else {

                        String insertQuery = "INSERT INTO students (Fname, Lname, town, hobby) VALUES (?, ?, ?, ?)";

                        try (PreparedStatement insertStmt = con.prepareStatement(insertQuery)) {

                            insertStmt.setString(1, firstName);
                            insertStmt.setString(2, lastName);
                            insertStmt.setString(3, town);
                            insertStmt.setString(4, hobby);

                            insertStmt.executeUpdate();

                            resp.sendRedirect("/students");
                        }
                    }
                }
            }
        } catch (Exception e) {
            out.println("<script type='text/javascript'>");

            out.println("alert('Databasfel: " + e.getMessage() + "');");
            out.println("window.history.back();");

            out.println("</script>");
        }
    }



}
