package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(urlPatterns = "/addcourse")
public class AddCourseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<head>");
        out.println("<link rel='stylesheet' href='css/style.css'>");
        out.println("</head>");

        out.println("<body>");
        out.println("<header>" +
                "    <h1 id=\"headertext\">Check Classes</h1>" +
                "</header>" +
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

        out.println("<div class=\"addCourseTableStyle\">");

        out.println("<div class=\"addCourseTableStyle-flex\">");
        String studentsTable = getStudentsTable();
        out.println(studentsTable);
        out.println("</div>");

        out.println("<div class=\"addCourseTableStyle-flex\">");
        out.println("<form id=\"formAddCourse\"action='/addcourse' method='POST'>");
        out.println("Select Student: <select name='studentId'>");

        out.println(getStudentsOptions());
        out.println("</select><br>");
        out.println("Select Course: <select name='courseId'>");
        out.println(getCoursesOptions());
        out.println("</select><br>");
        out.println("<input type='submit' value='Associate'>");
        out.println("</form>");
        out.println("</div>");

        out.println("<div class=\"addCourseTableStyle-flex\">");
        String coursesTable = getCoursesTable();
        out.println(coursesTable);
        out.println("</div>");

        out.println("</div>");

        out.println("</body>");
        out.println("</html>");
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String studentId = req.getParameter("studentId");
        String courseId = req.getParameter("courseId");

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Grit", "root", "")) { //för endast SELECT frågor namn: Aleksander lösenord: password

                String query = "SELECT COUNT(*) FROM attendance WHERE `student.id` = ? AND `course.id` = ?";

                try (PreparedStatement checkSt = con.prepareStatement(query)) {

                    checkSt.setInt(1, Integer.parseInt(studentId));
                    checkSt.setInt(2, Integer.parseInt(courseId));

                    ResultSet rs = checkSt.executeQuery();

                    if (rs.next() && rs.getInt(1) > 0) {

                        out.println("<script type='text/javascript'>");

                        out.println("alert('Den här studenten har redan den kursen, välj en annan.');");
                        out.println("location='/addcourse';");

                        out.println("</script>");

                    } else {

                        String insertQuery = "INSERT INTO attendance (`student.id`, `course.id`) VALUES (?, ?)";

                        try (PreparedStatement insertStmt = con.prepareStatement(insertQuery)) {
                            insertStmt.setInt(1, Integer.parseInt(studentId));
                            insertStmt.setInt(2, Integer.parseInt(courseId));

                            insertStmt.executeUpdate();

                            resp.sendRedirect("/attendance");

                        }
                    }
                }
            }
        } catch (Exception e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");

            out.println("<a href='/addcourse'>Go back to form</a>");
        }
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
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Grit", "root", "");
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

    public String getCoursesTable() {

        StringBuilder table = new StringBuilder("<table>");
        table.append("<tr><th>ID</th>" +
                "<th>Course name</th>" +
                "<th>YHP</th>" +
                "<th>Description</th>");
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Grit", "root", "");
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

    private String getStudentsOptions() {
        StringBuilder options = new StringBuilder();

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Grit", "root", "")) {

                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery("SELECT id, Fname, Lname FROM students");

                while (rs.next()) {

                    options.append("<option value='").append(rs.getInt("id")).append("'>")
                            .append(rs.getString("Fname")).append(" ")

                            .append(rs.getString("Lname")).append("</option>");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return options.toString();
    }

    private String getCoursesOptions() {
        StringBuilder options = new StringBuilder();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Grit", "root", "")) {

                Statement statement = con.createStatement();

                ResultSet rs = statement.executeQuery("SELECT id, name FROM courses");
                while (rs.next()) {
                    options.append("<option value='").append(rs.getInt("id")).append("'>")
                            .append(rs.getString("name")).append("</option>");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return options.toString();
    }
}
