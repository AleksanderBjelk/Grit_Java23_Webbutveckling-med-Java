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

@WebServlet(urlPatterns = "/attendance")
public class AttendanceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter out = resp.getWriter();
        out.println("<html>");

        out.println("<head>");
        out.println("<link rel='stylesheet' href='css/style.css'>");
        out.println("</head>");

        out.println("<body>");
        out.println(" <header>" +
                "    <h1 id=\"headertext\">Attendance</h1>\n" +
                "    </header>" +
                "    <nav id=\"nav\">" +
                "        <a href=\"http://localhost:9090\" class=\"current\" title=\"Start\">Start</a>" +
                "        <a href=\"http://localhost:9090/home\" title=\"home\" >Home</a>" +
                "        <a href=\"http://localhost:9090/students\" title=\"students\">Students</a>" +
                "        <a href=\"http://localhost:9090/courses\" title=\"courses\">Courses</a>" +
                "        <a href=\"http://localhost:9090/attendance\" title=\"attendance\">Attendance</a>" +
                "    </nav>");
        String attendancdeTable = getAttendanceTable();
        out.println(attendancdeTable);

        out.println("</body>");
        out.println("</html>");
        System.out.println("GET Request");
    }

    public String getAttendanceTable() {
        StringBuilder table = new StringBuilder("<table>");
        table.append("<tr><th>Attendance ID</th>" +
                "<th>Student Name</th>" +
                "<th>Courses Name</th>" +
                "</tr>");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Grit", "root", "");
            Statement stmt = con.createStatement();

            String query = "SELECT attendance.id, students.Fname, students.Lname, courses.name " +
                    "FROM attendance " +
                    "JOIN students ON attendance.`student.id` = students.id " +
                    "JOIN courses ON attendance.`course.id` = courses.id";

            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                table.append("<tr>");

                table.append("<td>").append(rs.getInt("attendance.id")).append("</td>");

                table.append("<td>").append(rs.getString("students.Fname")).append(" ").append(rs.getString("students.Lname")).append("</td>");

                table.append("<td>").append(rs.getString("courses.name")).append("</td>");


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
