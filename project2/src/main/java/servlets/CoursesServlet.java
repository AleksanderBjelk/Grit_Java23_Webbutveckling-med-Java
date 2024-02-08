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
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet(urlPatterns = "/courses")
public class CoursesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter out = resp.getWriter();
        out.println("<html>");

        out.println("<head>");
        out.println("<link rel='stylesheet' href='css/style.css'>");
        out.println("</head>");

        out.println("<body>");
        out.println(" <header>" +
                "    <h1 id=\"headertext\">Courses</h1>\n" +
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
        
        String coursesTable = getCoursesTable();
        out.println(coursesTable);

        out.println("</body>");
        out.println("</html>");
        System.out.println("GET Request");
    }

    public String getCoursesTable() {

        StringBuilder table = new StringBuilder("<table>");
        table.append("<tr><th>ID</th>" +
                "<th>Course name</th>" +
                "<th>YHP</th>" +
                "<th>Description</th>");
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Grit", "root", ""); //för endast SELECT frågor namn: Aleksander lösenord: password
            Statement statement = con.createStatement();

            ResultSet rs = statement.executeQuery("SELECT * FROM courses");

            while (rs.next()) {
                table.append("<tr>");

                table.append("<td>").append(rs.getInt("id")).append("</td>");

                table.append("<td>").append(rs.getString("name")).append("</td>");

                table.append("<td>").append(rs.getString("YHP")).append("</td>");

                table.append("<td>").append(rs.getString("description")).append("</td>");


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
