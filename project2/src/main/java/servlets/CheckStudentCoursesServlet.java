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

        PrintWriter out = resp.getWriter();


        out.println("<head>");
        out.println("<link rel='stylesheet' href='css/style.css'>");
        out.println("</head>");

        out.println("<body>");
        out.println(" <header>" +
                "    <h1 id=\"headertext\">Check Classes</h1>\n" +
                "    </header>" +
                "    <nav id=\"nav\">" +
                "        <a href=\"http://localhost:9090\" class=\"current\" title=\"Start\">Start</a>" +
                "        <a href=\"http://localhost:9090/students\" title=\"students\">Students</a>" +
                "        <a href=\"http://localhost:9090/courses\" title=\"courses\">Courses</a>" +
                "        <a href=\"http://localhost:9090/attendance\" title=\"attendance\">Attendance</a>" +
                "        <a href=\"http://localhost:9090/poststudents\" title=\"PostStudents\">Post Students</a>" +
                "        <a href=\"http://localhost:9090/postcourses\" title=\"PostCourses\">Post Courses</a>" +
                "        <a href=\"http://localhost:9090/checkstudentcourses\" title=\"CheckStudentCourses\">Check Classes</a>" +
                "        <a href=\"http://localhost:9090/addcourse\" title=\"addCourse\" >Add Course</a>" +
                "    </nav>");

        out.println("<form id=\"checkClassesForm\" action='/checkstudentcourses' method='POST'>");
        out.println("Förnamn: <input type='text' name='fname' required><br>");
        out.println("Efternamn: <input type='text' name='lname' required><br>");
        out.println("<input type='submit' value='Se kurser'>");
        out.println("</form>");

        String studentsTable = getStudentsTable();
        out.println(studentsTable);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
                "        <a href=\"http://localhost:9090/students\" title=\"students\">Students</a>" +
                "        <a href=\"http://localhost:9090/courses\" title=\"courses\">Courses</a>" +
                "        <a href=\"http://localhost:9090/attendance\" title=\"attendance\">Attendance</a>" +
                "        <a href=\"http://localhost:9090/poststudents\" title=\"PostStudents\">Post Students</a>" +
                "        <a href=\"http://localhost:9090/postcourses\" title=\"PostCourses\">Post Courses</a>" +
                "        <a href=\"http://localhost:9090/checkstudentcourses\" title=\"CheckStudentCourses\">Check Classes</a>" +
                "        <a href=\"http://localhost:9090/addcourse\" title=\"addCourse\" >Add Course</a>" +
                "    </nav>");

        out.println("<form id=\"checkClassesForm\" action='/checkstudentcourses' method='POST'>");
        out.println("Förnamn: <input type='text' name='fname' required><br>");
        out.println("Efternamn: <input type='text' name='lname' required><br>");
        out.println("<input type='submit' value='Se kurser'>");
        out.println("</form>");
        out.println("<div class=\"center\">");
        out.println("<a class=\"center\" href=\"http://localhost:9090/checkstudentcourses\"><input type='submit' value='Se elever igen'/></a>");
        out.println("</div>");
        String coursesTable = getStudentCourses(firstName, lastName);
        out.println(coursesTable);

        out.println("</body>");
        out.println("</html>");
    }


    public String getStudentCourses(String firstName, String lastName) {

        StringBuilder table = new StringBuilder("<table =\"center\">");
        table.append("<tr><th>Kurs ID</th><th>Kurs Namn</th></tr>");

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Grit", "root", ""); //för endast SELECT frågor namn: Aleksander lösenord: password

            PreparedStatement ps = con.prepareStatement(
                            "SELECT c.id, c.name FROM courses c JOIN attendance a ON c.id = a.`course.id` " +
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
        table.append("</table class>");
        return table.toString();
    }

    public String getStudentsTable() {

        StringBuilder table = new StringBuilder("<table =\"center\">");
        table.append("<tr><th>ID</th>" +
                "<th>First Name</th>" +
                "<th>Last Name</th>" +
                "<th>Town</th>" +
                "<th>Hobby</th>" +
                "</tr>");
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Grit", "root", ""); //för endast SELECT frågor namn: Aleksander lösenord: password
            Statement statement = con.createStatement();

            ResultSet rs = statement.executeQuery("SELECT * FROM students");

            while (rs.next()) {
                table.append("<tr>");

                table.append("<td>").append(rs.getInt("id")).append("</td>");

                table.append("<td>").append(rs.getString("Fname")).append("</td>");

                table.append("<td>").append(rs.getString("Lname")).append("</td>");

                table.append("<td>").append(rs.getString("town")).append("</td>");

                table.append("<td>").append(rs.getString("hobby")).append("</td>");

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

