package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(urlPatterns = "/checkstudentcourses")
public class CheckStudentCoursesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        String firstName = req.getParameter("fname");
        String lastName = req.getParameter("lname");

        out.println("<head>");
        out.println("<link rel='stylesheet' href='css/style.css'>");
        out.println("</head>");

        out.println("<body>");
        out.println(" <header>" +
                "    <h1 id=\"headertext\">Check Classes</h1>\n" +
                "    </header>" +
                "    <nav id=\"nav\">" +
                "        <a href=\"http://localhost:9090\" class=\"current\" title=\"Start\">Start</a>" +
                "        <a href=\"http://localhost:9090/home\" title=\"home\" >Home</a>" +
                "        <a href=\"http://localhost:9090/students\" title=\"students\">Students</a>" +
                "        <a href=\"http://localhost:9090/courses\" title=\"courses\">Courses</a>" +
                "        <a href=\"http://localhost:9090/attendance\" title=\"attendance\">Attendance</a>" +
                "        <a href=\"http://localhost:9090/poststudents\" title=\"PostStudents\">Post Students</a>" +
                "        <a href=\"http://localhost:9090/postcourses\" title=\"PostCourses\">Post Courses</a>" +
                "        <a href=\"http://localhost:9090/checkstudentcourses\" title=\"CheckStudentCourses\">Check Classes</a>" +
                "    </nav>");

        out.println("<form action='/checkstudentcourses' method='GET'>");
        out.println("Förnamn: <input type='text' name='fname' required><br>");
        out.println("Efternamn: <input type='text' name='lname' required><br>");
        out.println("<input type='submit' value='Se kurser'>");
        out.println("</form>");

        String coursesTable = getStudentCourses(firstName, lastName);
        out.println(coursesTable);

        out.println("</body>");
        out.println("</html>");
    }


    public String getStudentCourses(String firstName, String lastName) {

        StringBuilder table = new StringBuilder("<table>");
        table.append("<tr><th>Kurs ID</th><th>Kurs Namn</th></tr>");

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Grit", "root", "");

            PreparedStatement ps = con.prepareStatement(
                    "SELECT c.id, c.name " +
                            "FROM courses c " +
                            "JOIN attendance a ON c.id = a.`course.id` " +
                            "JOIN students s ON s.id = a.`student.id` " +
                            "WHERE s.Fname = ? AND s.Lname = ?");

            ps.setString(1, firstName);

            ps.setString(2, lastName);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                table.append("<tr>");

                table.append("<td>").append(rs.getInt("id")).append("</td>");

                table.append("<td>").append(rs.getString("name")).append("</td>");

                table.append("</tr>");
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        table.append("</table>");
        return table.toString();
    }
}

